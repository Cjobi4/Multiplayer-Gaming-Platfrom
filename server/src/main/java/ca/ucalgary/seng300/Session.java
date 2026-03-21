package ca.ucalgary.seng300;

import javax.crypto.SecretKey;
import java.net.Socket;

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
                processRequest(requestType, AESKey);
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
    private void processRequest(int requestType, SecretKey AESKey)
    {
        //handles requests that CAN happen simultaneously (i.e. database reading)
        switch (requestType)
        {
            case 0:

                break;
            case 1:

                break;
        }

        //only one Session can run this block at a time
        synchronized (Session.class)
        {
            //handles requests that CANNOT happen simultaneously (i.e. database writing)
            switch (requestType)
            {
                case 2:

                    break;
                case 3:

                    break;
            }

        }
    }
}
