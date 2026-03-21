package ca.ucalgary.seng300;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
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
import java.util.Hashtable;
import java.util.UUID;

public class Network
{
    private static Hashtable<UUID, SecretKey> keyList = new Hashtable<UUID, SecretKey>();
    private static SecureRandom sRan;

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

            //initialize the Secure Random generator for nonces
            sRan = new SecureRandom();

            //then start listening for client connections
            ServerSocket port = new ServerSocket(14001, 100, InetAddress.getByName("0.0.0.0"));
            System.out.println("Server running...");    //for debug (could maybe leave it in, only visible on server terminal)...

            //keep checking for incoming connections
            while(true)
            {
                //if a incoming connection is detected, accept it
                Socket client = port.accept();

                //create a new thread to handle the client
                Session newSesh = new Session(client);
                newSesh.start();

                /*new Thread(new Runnable()
                {
                    @Override
                    public void run() {handleClient(client);}
                }).start();*/
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Establishes a secure AES key between both the server and client. If the server-client already had one, use that.
     * Otherwise, if not, create a new clientID and AES key for use. Return the created/found AES key for use with the
     * client.
     * @param client The client computer the connection is being established with.
     * @return An AES key (of type SecretKey) for use with the client, or null if one could not be created or found.
     */
    public static SecretKey establishEncryption(Socket client)
    {
        try
        {
            UUID clientID;
            SecretKey AESKey;
            KeyAgreement serverKeyAgreement = KeyAgreement.getInstance("DH");

            //first check to see if client has a clientID
            int clientIDLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
            byte[] clientIDBytes = client.getInputStream().readNBytes(clientIDLength);
            String clientIDString = new String(clientIDBytes, StandardCharsets.UTF_8);
            System.out.println(clientIDString);     //for debug


            //if they didn't have one or there isn't a key associated with this client ID...
            if (clientIDString.equals("new") || !keyList.containsKey(clientIDString))
            {
                //let client know they're getting assigned a new clientID
                client.getOutputStream().write(0);

                //create a new clientID for them
                clientID = UUID.randomUUID();

                //create a public key and send it to the client
                byte[] serverPublicKey = generatePublicKey(serverKeyAgreement);
                client.getOutputStream().write(ByteBuffer.allocate(4).putInt(serverPublicKey.length).array());
                client.getOutputStream().write(serverPublicKey);
                System.out.println("public key sent");  //for debug

                //take the client's public key...
                int keyLength = ByteBuffer.wrap(client.getInputStream().readNBytes(4)).getInt();
                byte[] clientPublicKey = client.getInputStream().readNBytes(keyLength);
                System.out.println("public key received");  //for debug

                //...and create the shared secret and use it to make the AES key
                AESKey = generateAESKey(clientID, generateSharedKey(serverKeyAgreement, clientPublicKey));   //automatically added to the keyList
                System.out.println("shared key generated"); //for debug

                //need a flag to let client know if clientID + key pair should be overwritten?
                /*if server and client connect so client has clientID and then server is restarted, clientID record is
                lost only on server side. Client still thinks has valid clientID. Need way to tell client to disregard
                current clientID and accept a new one.
                 */

                //finally encrypt the newly created clientID and give it to the client
                clientIDBytes = encrypt(clientIDString, AESKey);
                client.getOutputStream().write(ByteBuffer.allocate(4).putInt(clientIDBytes.length).array());
                client.getOutputStream().write(clientIDBytes);
                System.out.println("clientID: " + clientID.toString()); //for debug
            }else   //if they did have one...
            {
                clientID = UUID.fromString(clientIDString);

                //retrieve their encryption key
                AESKey = keyList.get(clientID);

                //let client know their key was accepted
                client.getOutputStream().write(1);
            }

            //if everything worked, return the AES key being used
            return AESKey;
        } catch (Exception e)
        {
            //if something went wrong and no AES key is made/found, return null
            return null;
        }
    }


    /**
     * Creates a public-private key pair for the Diffie-Hellman protocol. The public key contains the modulus 'm', the
     * 'g' value, and the 'g^a' value. The private key is the 'a' value. The public key is returned (and later sent
     * to the client), while the private key is saved in the serverKeyAgreement for later use.
     * @param serverKeyAgreement The KeyAgreement object that handles the key exchange
     * @return The public key in byte[] format.
     * @throws Exception If the Diffie-Hellman protocol can't be found, will throw an Exception
     */
    private static byte[] generatePublicKey(KeyAgreement serverKeyAgreement) throws Exception
    {
        //create a public-private key pair
        KeyPairGenerator serverKeyPairGen = KeyPairGenerator.getInstance("DH"); //DH = Diffie-Hellman
        serverKeyPairGen.initialize(512);
        KeyPair serverKeyPair = serverKeyPairGen.generateKeyPair();

        //handles exchange protocol
        serverKeyAgreement.init(serverKeyPair.getPrivate());

        //turn the public key into a byte array and send
        return serverKeyPair.getPublic().getEncoded();
    }

    /**
     * Using the client's public key and the private key previously created in generatePublicKey(), creates a shared
     * secret (matching byte[] between the client and server). In Diffie-Hellman terms, this function applies the
     * server's 'a' value to g^b, before applying modulus 'm' to the whole expression.
     * @param serverKeyAgreement The KeyAgreement object that handles the key exchange
     * @param clientPubKeyBytes The public key of the client in byte[] format
     * @return The shared secret, in byte[] format
     * @throws Exception If the Diffie-Hellman protocol can't be found, will throw an Exception
     */
    private static byte[] generateSharedKey(KeyAgreement serverKeyAgreement, byte[] clientPubKeyBytes) throws Exception
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
     * @throws Exception If the SHA-256 algorithm can't be found, will throw an Exception
     */
    private static SecretKey generateAESKey(UUID clientID, byte[] sharedSecret) throws Exception
    {
        //use the shared secret to generate a hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(sharedSecret);

        //then use the first 16 bytes of that hash to make the key
        byte[] shortHash = new byte[16];
        System.arraycopy(hash, 0, shortHash, 0, 16);
        SecretKey AESKey = new SecretKeySpec(shortHash, "AES");

        //add it to the list
        keyList.put(clientID, AESKey);

        return AESKey;
    }

    /**
     * Given a plaintext in string format, encrypts it and converts it into a byte[] format for transmission. The first
     * 12 bytes is the nonce, an extra string of bits that ensures the same messages sent multiple times will always
     * result in different ciphertexts. The encrypted message is then appended onto the end of this nonce.
     * @param plainText The string to be encrypted with AES
     * @param AESKey The SecretKey the message should be encrypted with
     * @return The string encrypted with AES and in byte[] format, preceded by 12 bytes of nonce.
     * @throws Exception If the AES GCM mode algorithm can't be found, will throw an Exception
     */
    private static byte[] encrypt(String plainText, SecretKey AESKey) throws Exception
    {
        //makes 12 byte long nonce according to NIST standards
        byte[] nonce = new byte[12];
        sRan.nextBytes(nonce);
        GCMParameterSpec gcmParamSpec = new GCMParameterSpec(128, nonce);

        //create a cipher for the CURRENT AESKey
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, AESKey, gcmParamSpec);

        //the encrypted plaintext WITHOUT the nonce
        byte[] cipherTextTag = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        //add the nonce to the start of the message
        byte[] message = new byte[nonce.length + cipherTextTag.length];
        System.arraycopy(nonce, 0, message, 0, 12);
        System.arraycopy(cipherTextTag, 0, message, 12, cipherTextTag.length);

        return message;
    }

    /**
     * Given an encrypted cipherText in byte[] format, decrypts it back into the original plaintext.
     * @param cipherText The AES encrypted message in byte[] format
     * @param AESKey The SecretKey the message was encrypted with
     * @return The decrypted plaintext in string format
     * @throws Exception If the AES GCM mode algorithm can't be found, will throw an Exception. Or if the message has
     * been altered AFTER the encryption was performed (i.e. some bits are lost/changed), the message will not be
     * decrypted properly and will throw an exception.
     */
    private static String decrypt(byte[] cipherText, SecretKey AESKey) throws Exception
    {
        //collect the nonce (first 12 bytes of the message)
        byte[] nonce = new byte[12];
        System.arraycopy(cipherText, 0, nonce, 0, 12);

        //separate the encrypted plaintext from the nonce
        byte[] cipherTextMessage = new byte[cipherText.length - 12];
        System.arraycopy(cipherText, 12, cipherTextMessage, 0, cipherText.length - 12);

        //create a cipher for the AES GCM algorithm
        GCMParameterSpec gcmParamSpec = new GCMParameterSpec(128, nonce);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, AESKey, gcmParamSpec);

        //decrypt the message
        byte[] plainText = cipher.doFinal(cipherTextMessage);

        //and convert it back into a string
        return new String(plainText, StandardCharsets.UTF_8);
    }
}
