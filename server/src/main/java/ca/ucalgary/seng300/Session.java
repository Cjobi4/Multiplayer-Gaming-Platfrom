package ca.ucalgary.seng300;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Session extends Thread
{
    private SecretKey AESKey = null;
    private int userID;
    private String username = null;
    private Socket client;
    private boolean loggedIn;

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
     * Runs immediately after the new Session thread is created. Handles the connection with the client.
     */
    public void run()
    {
        //establish an encryption key with the client
        AESKey = Network.establishEncryption(client);

        try
        {
            int requestType;

            //then start listening for incoming requests
            while (true)
            {
                //see what type of request it is
                requestType = client.getInputStream().read();

                //int messageLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                //byte[] message = client.getInputStream().readNBytes(messageLength);

                //process the request
                try
                {
                    processRequest(requestType, AESKey);
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) //if an exception happens, it will kill the thread automatically
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Reads the request type and executes the required actions. Also uses synchronization to prevent multiple Sessions
     * from editing the Database at the same time.
     * @param requestType The integer corresponding to the request type.
     * @param AESKey The encryption key currently in use for this session.
     */
    private void processRequest(int requestType, SecretKey AESKey) throws Exception
    {
        int messageLength;
        byte messageBytes[];

        //handles requests that CAN happen simultaneously (i.e. database reading)
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

                //check if it was successful
                userID = Database.createAccount(newUsername, newPassword);

                if (userID != -1)
                {
                    //if it was, save the username for the session
                    username = newUsername;
                    loggedIn = true;
                }   //otherwise do nothing
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
                userID = Database.checkLoginCredentials(usernameInput, passwordInput);

                if (userID != -1)
                {
                    //if it was, save the username for the session
                    username = usernameInput;
                    loggedIn = true;
                }   //otherwise do nothing
                break;
        }

        //only one Session can run this block at a time
        synchronized (Session.class)
        {
            //handles requests that CANNOT happen simultaneously (i.e. database writing)
            switch (requestType)
            {
                case 3:

                    break;
                case 4:

                    break;
            }

        }
    }
}
