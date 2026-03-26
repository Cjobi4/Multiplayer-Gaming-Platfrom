package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.identity.client.Network;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkTest {

    /*
    Most of these test cases can only test the logic of the functions
    This is because I cannot connect to the server itself to verify
     */
    @BeforeAll
    static void setUpEncryptionKey(){
        Network.setupTestEncryption(); // need to initialize encryption keys
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        byte[] encryptedTrue = Network.encrypt("true");
        byte[] simulatedServerResponse = ByteBuffer.allocate(4 + encryptedTrue.length)
                .putInt(encryptedTrue.length)
                .put(encryptedTrue)
                .array();
        // allocates as much space as needed and fills it with data
        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
        // create the fake server

        Network myNetwork = new Network(stubSocket);

        boolean loginResult = myNetwork.login("testUser", "testPassword");
        assertTrue(loginResult);
    }

    /*
    Testing scenarios where the server returns false
     */
    @Test
    void testFailedLogin() throws Exception{
        byte[] encryptedFalse = Network.encrypt("false");
        byte[] simulatedServerResponse = ByteBuffer.allocate(4 + encryptedFalse.length)
                .putInt(encryptedFalse.length)
                .put(encryptedFalse)
                .array();
        // creates a fake response
        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
        // create the fake server

        Network myNetwork = new Network(stubSocket);

        boolean loginResult = myNetwork.login("testUser", "testPassword");
        assertFalse(loginResult);
    }

    @Test
    void testAccountCreation() throws Exception{
        byte[] encryptedTrue = Network.encrypt("true");
        byte[] simulatedServerResponse = ByteBuffer.allocate(4 + encryptedTrue.length)
                .putInt(encryptedTrue.length)
                .put(encryptedTrue)
                .array();

        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
        // create the fake server

        Network myNetwork = new Network(stubSocket);
        boolean loginResult = myNetwork.registerAccount("testUser", "testPassword");
        assertTrue(loginResult);
    }

    @Test
    void testEncryption() throws Exception{
        String message = "testing message";
        byte[] encryptedMessage = Network.encrypt(message);

        String scrambledMessage = new String(encryptedMessage, StandardCharsets.UTF_8); // turn the encrypted bytes back to string


        assertNotEquals(message, scrambledMessage);
    }

    /*
    Testing that decryption works as intended
     */
    @Test
    void testEncryptionDecryptionSymmetry() throws Exception{
        String message = "testing message";

        byte[] encryptedMessage = Network.encrypt(message); // encrypt the message

        String decryptedMessage = Network.decrypt(encryptedMessage); // decrypt the message

        Assertions.assertEquals(message, decryptedMessage);
    }


}
