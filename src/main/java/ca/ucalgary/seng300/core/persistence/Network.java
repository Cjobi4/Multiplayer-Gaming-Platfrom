package ca.ucalgary.seng300.core.persistence;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class Network
{
    private static HashMap<String, SecretKey> keyList = new HashMap<String, SecretKey>();
    private static SecretKey AESKey;

    /**
     * Turns the current computer into a working database server. Initializes the database tables and connection with
     * helper functions from the Database class, and begins listening for client connections. All connections are then
     * redirected to the handleClient() function for further processing.
     */
    public static void launchNetwork()
    {
        try
        {
            //first initialize the database
            Database.databaseInitial();

            //then start listening for client connections
            ServerSocket port = new ServerSocket(14001, 100, InetAddress.getByName("0.0.0.0"));
            System.out.println("Server running...");    //for debug (could maybe leave it in, only visible on server terminal)...

            //keep checking for incoming connections
            while(true)
            {
                //if a incoming connection is detected, accept it
                Socket client = port.accept();
                handleClient(client);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClient(Socket client)
    {
        try
        {
            /////////////////////////////////////// CLIENT ID COLLECTION ///////////////////////////////////
            UUID clientID;

            //first check to see if client has a clientID
            int clientIDLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
            byte[] clientIDBytes = client.getInputStream().readNBytes(clientIDLength);
            String clientIDString = new String(clientIDBytes, StandardCharsets.UTF_8);
            System.out.println(clientIDString);     //for debug

            //if they already have one...
            if (!clientIDString.equals("new"))
            {



            }

            //if they didn't have one or there isn't a key associated with this client ID...
            if (clientIDString.equals("new") || !keyList.containsKey(clientIDString))
            {
                //create a new clientID for them
                clientID = UUID.randomUUID();


                //need a flag to let client know if clientID + key pair should be overwritten?
            }else   //if they did have one...
            {
                clientID = UUID.fromString(clientIDString);

                //retrieve their encryption key
                AESKey = keyList.get(clientIDString);
            }
        }
    }
}
