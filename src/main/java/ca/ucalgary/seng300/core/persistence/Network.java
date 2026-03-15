package ca.ucalgary.seng300.core.persistence;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Network
{
    private static KeyAgreement serverKeyAgreement = null;
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
                /*if server and client connect so client has clientID and then server is restarted, clientID record is
                lost only on server side. Client still thinks has valid clientID. Need way to tell client to disregard
                current clientID and accept a new one.

                 */
            }else   //if they did have one...
            {
                clientID = UUID.fromString(clientIDString);

                //retrieve their encryption key
                AESKey = keyList.get(clientIDString);
            }
        }   //missing catch statment
    }


    /**
     * Creates a public-private key pair for the Diffie-Hellman protocol. The public key contains the modulus 'm', the
     * 'g' value, and the 'g^a' value. The private key is the 'a' value. The public key is returned (and later sent
     * to the client), while the private key is saved in the serverKeyAgreement for later use.
     * @return The public key in byte[] format.
     * @throws Exception If the Diffie-Hellman protocol can't be found, will throw a NoSuchAlgorithmException
     */
    private static byte[] generatePublicKey() throws Exception
    {
        //create a public-private key pair
        KeyPairGenerator serverKeyPairGen = KeyPairGenerator.getInstance("DH"); //DH = Diffie-Hellman
        serverKeyPairGen.initialize(512);
        KeyPair serverKeyPair = serverKeyPairGen.generateKeyPair();

        //handles exchange protocol
        serverKeyAgreement = KeyAgreement.getInstance("DH");
        serverKeyAgreement.init(serverKeyPair.getPrivate());

        //turn the public key into a byte array and send
        return serverKeyPair.getPublic().getEncoded();
    }

    /**
     * Using the client's public key and the private key previously created in generatePublicKey(), creates a shared
     * secret (matching byte[] between the client and server). In Diffie-Hellman terms, this function applies the
     * server's 'a' value to g^b, before applying modulus 'm' to the whole expression.
     * @param clientPubKeyBytes The public key of the client in byte[] format
     * @return The shared secret, in byte[] format
     * @throws Exception If the Diffie-Hellman protocol can't be found, will throw a NoSuchAlgorithmException
     */
    private static byte[] generateSharedKey(byte[] clientPubKeyBytes) throws Exception
    {
        //server converts client's public key back into readable format
        KeyFactory serverKeyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec2 = new X509EncodedKeySpec(clientPubKeyBytes);
        PublicKey clientPublicKey = serverKeyFactory.generatePublic(x509KeySpec2);

        //calculate the shared secret using the client's public key and the server's private key
        serverKeyAgreement.doPhase(clientPublicKey, true);
        byte[] sharedSecret = serverKeyAgreement.generateSecret();

        System.out.println("Shared key: " + Arrays.toString(sharedSecret));    //for debug

        return sharedSecret;
    }

    /**
     * Using the shared secret created from the Diffie-Hellman protocol (specifically generateSharedKey()'s result),
     * creates an AES key for encryption and decryption. The new AES key is then saved into the keyList with the client's
     * clientID.
     * @param clientID  The clientID of the client computer the AES key is being generated for.
     * @param sharedSecret The shared secret between the client and server computers.
     * @throws Exception If the SHA-256 algorithm can't be found, will throw a NoSuchAlgorithmException
     */
    public static void generateAESKey(String clientID, byte[] sharedSecret) throws Exception
    {
        //use the shared secret to generate a hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(sharedSecret);

        //then use the first 16 bytes of that hash to make the key
        byte[] shortHash = new byte[16];
        System.arraycopy(hash, 0, shortHash, 0, 16);
        AESKey = new SecretKeySpec(shortHash, "AES");

        //add it to the list
        keyList.put(clientID, AESKey);
    }
}
