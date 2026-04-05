package ca.ucalgary.seng300;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Session extends Thread
{
    private SecretKey AESKey = null;
    private String username = null;
    private Socket client;
    private boolean loggedIn;
    private boolean inTttQueue;
    private boolean inC4Queue;
    private LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

    //////////////// REQUEST TYPES ////////////////
    //private final int PING = 0;
    //private final int CREATE_ACCOUNT = 1;
    //private final int LOGIN = 2;
    //private final int LOGOUT = 3;
    //private final int GET_GAME_LIST = 4;
    //private final int GET_LEADERBOARD = 5;
    //private final int GET_MATCH_RECORD = 6;
    //private final int JOIN_TTT_QUEUE = 7;
    //private final int LEAVE_TTT_QUEUE = 8;
    //private final int JOIN_C4_QUEUE = 9;
    //private final int LEAVE_C4_QUEUE = 10;
    //private final int GET_USER_LIST = 11;
    //private final int MATCH_FOUND = 12;
    //private final int KICKED_FROM_QUEUE = 13;
    //private final int MATCH_ACCEPTED = 14;
    //private final int MATCH_REJECTED = 15;

    /**
     * Class constructor, creates a new session object to handle the client.
     * @param soc The socket the client is connecting to
     */
    Session(Socket soc)
    {
        client = soc;
        loggedIn = false;
        inTttQueue = false;
        inC4Queue = false;
    }

    /**
     * Adds a request to the queue for the Session to execute. This should be used when NO response is needed. A Request
     * object is automatically made from the input and given to the Session.
     * @param type The request type, in int form
     * @param parameters The parameters for the request, in a String array with each parameter under a different index
     * @throws Exception NullPointerException if the request is null, or InterruptedException if is interrupted while
     * waiting
     */
    public void addRequest(int type, String[] parameters) throws Exception
    {
        Request req = new Request(type, parameters);
        requestQueue.put(req);
    }

    /**
     * Adds a request to the queue for the Session to execute. This should be used when a result IS needed. The Request
     * object MUST be created before being passed into the function for result retrieval.
     * @param req The Request object to be added into the queue.
     * @throws Exception NullPointerException if the request is null, or InterruptedException if is interrupted while
     * waiting
     */
    public void addRequest(Request req) throws Exception
    {
        requestQueue.put(req);
    }

    @Override
    public void run()
    {
        //establish an encryption key with the client
        AESKey = Network.establishEncryption(client);

        try
        {
            int requestType;
            Request req;

            //then start listening for incoming requests
            while (true)
            {
                try //listen for incoming transmissions
                {
                    client.setSoTimeout(3000);  //server pauses checking every 3 seconds

                    //see what type of request it is
                    requestType = client.getInputStream().read();

                    //int messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                    //byte[] message = client.getInputStream().readNBytes(messageLength);

                    //process the request
                    processRequest(requestType, AESKey, null);

                } catch (SocketTimeoutException e)  //if nothing is currently found, go through the queue
                {
                    //if the queue isn't empty
                    if (!requestQueue.isEmpty())
                    {
                        //handle the request
                        req = requestQueue.take();
                        processRequest(req.getType(), AESKey, req);
                    }
                }

            }

        } catch (Exception e) //if an exception happens, it will kill the thread automatically
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * If something goes wrong with the Session thread and an uncaught expection is thrown, shut down the Session. Log
     * out and leave any matchmaking queues before doing closing.
     * @param eh Unused UncaughtExceptionHandler
     */
    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh)
    {
        //if something goes wrong, log out
        if (loggedIn)
        {
            Database.logOut(username);
        }

        //if in any matchmaking queues, exit
        if (inTttQueue)
        {
            Database.getTttMatchmaker().leaveMQueue(this);
        }else if (inC4Queue)
        {
            Database.getC4Matchmaker().leaveMQueue(this);
        }

        //close the thread
        Thread.currentThread().interrupt();
    }

    /**
     * Getter for the Session's username.
     * @return The Session's username.
     */
    public String getUsername()
    {
        return username;
    }


    /**
     * Reads the request type and executes the required actions. Also uses synchronization to prevent multiple Sessions
     * from editing the Database at the same time.
     * @param requestType The integer corresponding to the request type.
     * @param AESKey The encryption key currently in use for this session.
     * @param req The request from requestQueue that is being processed. If this did not originate from a request, req
     *            should be passed as null.
     */
    private void processRequest(int requestType, SecretKey AESKey, Request req) throws Exception
    {
        int messageLength;
        byte[] messageBytes;
        String message;
        int result;
        ResultSet rs;
        StringBuilder sbuild;

        //make sure the connection doesn't time out while waiting for response
        client.setSoTimeout(10000);

        //these requests don't need the user to be logged in
        switch (requestType) {
            case 0:

                break;
            case 1:     //if it was a register account request...
                //collect the username
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String newUsername = Network.decrypt(messageBytes, AESKey);

                //collect the password
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String newPassword = Network.decrypt(messageBytes, AESKey);

                //check to see if the password meets the password requirements, and see if the username has restricted special characters
                if (newPassword.length() < 8 || newPassword.length() > 18
                        || newUsername.contains("`") || newUsername.contains("^"))
                {
                    //if it doesn't pass don't make an account
                    client.getOutputStream().write(2);
                    break;
                }

                //only one Session can run this block at a time
                synchronized (Session.class) {
                    //check if it was successful
                    result = Database.createAccount(newUsername, newPassword, this);
                }

                //if it was, save the username for the session
                if (result == 1) {
                    username = newUsername;
                    loggedIn = true;

                    //notify the client of success
                    client.getOutputStream().write(1);
                } else if (result == 0) //if the username was already taken
                {
                    client.getOutputStream().write(0);
                } else //otherwise notify of failure
                {
                    client.getOutputStream().write(3);
                }
                break;
            case 2:     //if it was a login request...
                //collect the username
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String usernameInput = Network.decrypt(messageBytes, AESKey);

                //collect the password
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String passwordInput = Network.decrypt(messageBytes, AESKey);

                //check to see if the password meets the password requirements, and see if the username has restricted special characters
                if (passwordInput.length() < 8 || passwordInput.length() > 18
                        || usernameInput.contains("`") || usernameInput.contains("^"))
                {
                    //if it doesn't pass don't make an account
                    client.getOutputStream().write(0);
                    break;
                }

                //check if it was successful
                result = Database.checkLoginCredentials(usernameInput, passwordInput, this);

                //if it was, save the username for the session
                if (result == 1) {
                    username = usernameInput;
                    loggedIn = true;

                    //notify the client of success
                    client.getOutputStream().write(1);
                } else //otherwise notify of failure
                {
                    client.getOutputStream().write(0);
                }
                break;
        }

        //these requests require the user to be logged in
        if (loggedIn)
        {
            switch (requestType)
            {
                case 3:     //if it was a logout request
                    Database.logOut(username);

                    //close the connection with the client
                    TimeUnit.SECONDS.sleep(2);      //give some time, make sure client disconnects first before closing session thread
                    Thread.currentThread().interrupt();
                    ///////check this might not need
                    break;
                case 4:     //if it was a request for the game info
                    //collect the game info from the database
                    rs = Database.getAllGames();

                    //go through the results
                    if (rs != null && rs.next())
                    {
                        //go through each game data...
                        do
                        {
                            //send the game data to the client
                            messageBytes = Network.encrypt(rs.getString(2), AESKey);
                            client.getOutputStream().write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
                            client.getOutputStream().write(messageBytes);
                            System.out.println("game info sent");  //for debug
                        } while (rs.next());

                        //notify the client of success
                        client.getOutputStream().write(1);
                    } else //if something went wrong and no game info was found, notify client
                    {
                        client.getOutputStream().write(0);
                    }
                    break;
                case 5:     //if it was a request for leaderboard data
                    //collect the game to be queried
                    messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                    messageBytes = client.getInputStream().readNBytes(messageLength);
                    message = Network.decrypt(messageBytes, AESKey);

                    //collect the leaderboard entries from the database for that game
                    rs = Database.getAllLeaderboardEntries(message);

                    //go through the results
                    if (rs != null && rs.next())
                    {
                        sbuild = new StringBuilder();

                        //go through each entry...
                        do
                        {
                            //send the username separately
                            messageBytes = Network.encrypt(rs.getString(1), AESKey);
                            client.getOutputStream().write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
                            client.getOutputStream().write(messageBytes);

                            //then turn the wins/losses into a single string
                            sbuild.append(rs.getString(message + "Wins"));
                            sbuild.append("^");
                            sbuild.append(rs.getString(message + "MatchesPlayed"));

                            //send the formatted leaderboard entry to the client
                            messageBytes = Network.encrypt(sbuild.toString(), AESKey);
                            client.getOutputStream().write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
                            client.getOutputStream().write(messageBytes);
                            System.out.println("leaderboard entry sent");  //for debug
                        } while (rs.next());

                        //notify the client of success
                        client.getOutputStream().write(1);
                    } else //if something went wrong and no leaderboard data was found, notify client
                    {
                        client.getOutputStream().write(0);
                    }
                    break;
                case 6:    //if it was a request for match record's from a specific user
                    //collect the desired username
                    messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                    messageBytes = client.getInputStream().readNBytes(messageLength);
                    message = Network.decrypt(messageBytes, AESKey);

                    //collect the match records with matching usernames from the database
                    rs = Database.getMatchRecord(message);

                    //if the inputted username was valid, go through the results
                    if (rs != null && rs.next())
                    {
                        sbuild = new StringBuilder();

                        //go through each match record...
                        do
                        {
                            //turn each match record into a single string
                            for (int i = 2; i <= 6; i++)
                            {
                                sbuild.append(rs.getString(i));
                                sbuild.append("^");
                            }

                            //remove the trailing ^ symbol
                            sbuild.deleteCharAt(sbuild.length());

                            //send the formatted match records to the client
                            messageBytes = Network.encrypt(sbuild.toString(), AESKey);
                            client.getOutputStream().write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
                            client.getOutputStream().write(messageBytes);
                            System.out.println("match record sent");  //for debug

                            //notify the client of success
                            client.getOutputStream().write(1);
                        } while (rs.next());
                    } else //if something went wrong and no match records were found, notify client
                    {
                        client.getOutputStream().write(0);
                    }
                    break;
                case 7:     //if it was a request to join the Tic-tac-toe matchmaking queue...
                    //join the queue and notify if successful
                    inTttQueue = Database.getTttMatchmaker().joinMQueue(this);

                    if (inTttQueue)
                    {
                        client.getOutputStream().write(1);
                    }else
                    {
                        client.getOutputStream().write(0);
                    }
                    break;
                case 8:     //if it was a request to leave the Tic-tac-toe matchmaking queue...
                    //leave the queue and notify if successful
                    inTttQueue = !Database.getTttMatchmaker().leaveMQueue(this);

                    if (!inTttQueue)
                    {
                        client.getOutputStream().write(1);
                    }else
                    {
                        client.getOutputStream().write(0);
                    }
                    break;
                case 9:     //if it was a request to join the Connect-4 matchmaking queue...
                    //join the queue and notify if successful
                    inC4Queue = Database.getC4Matchmaker().joinMQueue(this);

                    if (inC4Queue)
                    {
                        client.getOutputStream().write(1);
                    }else
                    {
                        client.getOutputStream().write(0);
                    }
                    break;
                case 10:     //if it was a request to leave the Connect-4 matchmaking queue...
                    //leave the queue and notify if successful
                    inC4Queue = !Database.getC4Matchmaker().leaveMQueue(this);

                    if (!inC4Queue)
                    {
                        client.getOutputStream().write(1);
                    }else
                    {
                        client.getOutputStream().write(0);
                    }
                    break;
                case 11:    //if it was a request of all logged-in users for matchmaking...
                    //get the list of players from the database and send it to the client
                    messageBytes = Network.encrypt(Database.getLoggedInUsers(), AESKey);
                    client.getOutputStream().write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
                    client.getOutputStream().write(messageBytes);
                    System.out.println("player list sent");  //for debug
                    break;
            }
        }
    }
}
