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
    private int userID;
    private String username = null;
    private int winrate;
    private Socket client;
    private boolean loggedIn;
    private LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

    //////////////// REQUEST TYPES ////////////////
    private final int PING = 0;
    private final int CREATE_ACCOUNT = 1;
    private final int LOGIN = 2;
    private final int LOGOUT = 3;
    private final int GET_GAME_LIST = 4;
    private final int GET_LEADERBOARD = 5;
    private final int GET_MATCH_RECORD = 6;

    /**
     * Class constructor, creates a new session object to handle the client.
     * @param soc The socket the client is connecting to
     */
    Session(Socket soc)
    {
        client = soc;
        userID = -1;    //invalid userID, placeholder to show client is not signed in yet
        loggedIn = false;
    }

    /**
     * inner class for sending and receiving requests to the client FROM THE SERVER (server initiated transmissions)
     */
    class Request
    {
        CompletableFuture<String> future;
        private int type;
        private String[] parameters;

        //constructer
        Request(int t, String[] p)
        {
            type = t;
            parameters = p;
        }

        //type getter
        public int getType()
        {
            return type;
        }

        //parameter getter
        public String[] getParameters()
        {
            return parameters;
        }

        //returns the result of the future (must handle exception)
        public String getResult() throws Exception
        {
            return future.get();
        }
    }

    /**
     * Adds a request to the queue for the session to execute.
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

    @Override
    public void run()
    {
        //establish an encryption key with the client
        AESKey = Network.establishEncryption(client);

        try
        {
            int requestType;
            client.setSoTimeout(5000);  //server pauses checking every 5 seconds

            //then start listening for incoming requests
            while (true)
            {
                try //listen for incoming transmissions
                {
                    //see what type of request it is
                    requestType = client.getInputStream().read();

                    //int messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                    //byte[] message = client.getInputStream().readNBytes(messageLength);

                    //process the request
                    processRequest(requestType, AESKey, null);

                } catch (SocketTimeoutException e)  //if nothing is currently found, go through the queue
                {
                    Request req = requestQueue.poll(1, TimeUnit.MILLISECONDS);

                    //if the queue isn't empty
                    if (req != null)
                    {
                        //handle the request
                        processRequest(req.getType(), AESKey, req);
                    }
                }

            }

        } catch (Exception e) //if an exception happens, it will kill the thread automatically
        {
            throw new RuntimeException(e);
        }
    }

    /*@Override //maybe not necessary? perhaps notify client of connection closing first?
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh)
    {
        //if something goes wrong, kill the thread
        Thread.currentThread().interrupt();
    }*/

    /**
     * Getter for the Session's userID.
     * @return The Session's userID.
     */
    public int getUserID()
    {
        return userID;
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
        ResultSet rs;
        StringBuilder sbuild;

        //depending on the request type...
        switch (requestType)
        {
            case PING:

                break;
            case CREATE_ACCOUNT:     //if it was a register account request...
                //collect the username
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String newUsername = Network.decrypt(messageBytes, AESKey);

                //collect the password
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String newPassword = Network.decrypt(messageBytes, AESKey);

                //check to see if the password meets the password requirements
                if (newPassword.length() < 8 || newPassword.length() > 18)
                {
                    //if it doesn't pass don't make an account
                    client.getOutputStream().write(0);
                    break;
                }

                //only one Session can run this block at a time
                synchronized (Session.class)
                {
                    //check if it was successful
                    userID = Database.createAccount(newUsername, newPassword, this);
                }

                //if it was, save the username for the session
                if (userID != -1)
                {
                    username = newUsername;
                    loggedIn = true;
                    //notify the client of success
                    client.getOutputStream().write(1);
                }else //otherwise notify of failure
                {
                    client.getOutputStream().write(0);
                }
                break;
            case LOGIN:     //if it was a login request...
                //collect the username
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String usernameInput = Network.decrypt(messageBytes, AESKey);

                //collect the password
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String passwordInput = Network.decrypt(messageBytes, AESKey);

                //check to see if the password meets the password requirements
                if (passwordInput.length() < 8 || passwordInput.length() > 18)
                {
                    //if it doesn't pass don't bother asking database
                    client.getOutputStream().write(0);
                    break;
                }

                //check if it was successful
                userID = Database.checkLoginCredentials(usernameInput, passwordInput, this);

                //if it was, save the username for the session
                if (userID != -1)
                {
                    username = usernameInput;
                    loggedIn = true;

                    //notify the client of success
                    client.getOutputStream().write(1);
                }else //otherwise notify of failure
                {
                    client.getOutputStream().write(0);
                }
                break;
            case LOGOUT:     //if it was a logout request
                Database.logOut(userID);

                //close the connection with the client
                TimeUnit.SECONDS.sleep(2);      //give some time, make sure client disconnects first before closing session thread
                Thread.currentThread().interrupt();
                ///////check this might not need
                break;
            case GET_GAME_LIST:     //if it was a request for the game info
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
            case GET_LEADERBOARD:     //if it was a request for leaderboard data
                //collect the leaderboard entries from the database
                rs = Database.getAllLeaderboardEntries();

                //go through the results
                if (rs != null && rs.next())
                {
                    sbuild = new StringBuilder();

                    //go through each entry...
                    do
                    {
                        //turn each entry into a single string
                        for (int i = 1; i <= 6; i++)
                        {
                            sbuild.append(rs.getString(i));
                            sbuild.append("^");     //use ^ as separators
                        }

                        //send the formatted leaderboard entry to the client
                        messageBytes = Network.encrypt(sbuild.toString(), AESKey);
                        client.getOutputStream().write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
                        client.getOutputStream().write(messageBytes);
                        System.out.println("leaderboard entry sent");  //for debug

                        //notify the client of success
                        client.getOutputStream().write(1);
                    } while (rs.next());
                } else //if something went wrong and no leaderboard data was found, notify client
                {
                    client.getOutputStream().write(0);
                }
                break;
            case GET_MATCH_RECORD:    //if it was a request for match record's from a specific user
                //collect the desired userid
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                message = Network.decrypt(messageBytes, AESKey);

                try
                {
                    //collect the match records with matching userids from the database
                    rs = Database.getMatchRecord(Integer.parseInt(message));
                } catch (NumberFormatException e)       //if an invalid userid was entered, notify client
                {
                    client.getOutputStream().write(0);
                    break;
                }

                //if the inputed userid was valid, go through the results
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
        }
    }
}
