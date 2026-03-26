package ca.ucalgary.seng300;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Session extends Thread
{
    private SecretKey AESKey = null;
    private int userID;
    private String username = null;
    private Socket client;
    private boolean loggedIn;
    private LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

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
        byte messageBytes[];

        //depending on the request type...
        switch (requestType)
        {
            case 0:

                break;
            case 1:     //if it was a register account request...
                //collect the username
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String newUsername = new String(messageBytes, StandardCharsets.UTF_8);

                //collect the password
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String newPassword = new String(messageBytes, StandardCharsets.UTF_8);

                //only one Session can run this block at a time
                synchronized (Session.class)
                {
                    //check if it was successful
                    userID = Database.createAccount(newUsername, newPassword, Thread.currentThread());
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
            case 2:     //if it was a login request...
                //collect the username
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String usernameInput = new String(messageBytes, StandardCharsets.UTF_8);

                //collect the password
                messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                messageBytes = client.getInputStream().readNBytes(messageLength);
                String passwordInput = new String(messageBytes, StandardCharsets.UTF_8);

                //check if it was successful
                userID = Database.checkLoginCredentials(usernameInput, passwordInput, Thread.currentThread());

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
            case 3:     //if it was a logout request
                Database.logOut(userID);

                //close the connection with the client
                Thread.currentThread().interrupt();
                break;
            case 4:     //if it was a request for the game info
                //collect the game info from the database
                ResultSet rs = Database.getAllGames();

                //go through the results
                if (rs.next())
                {
                    do
                    {
                        //send each game info data to the client
                        byte[] gameInfo = rs.getString(2).getBytes(StandardCharsets.UTF_8);
                        client.getOutputStream().write(ByteBuffer.allocate(4).putInt(gameInfo.length).array());
                        client.getOutputStream().write(gameInfo);
                        System.out.println("game info sent");  //for debug
                    } while (rs.next());
                } else //if something went wrong and no game info was found, notify client
                {
                    client.getOutputStream().write(-1);
                }
                break;
        }
    }
}
