package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.shared.models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkTest {

    // TODO rewrite tests to reflect updates in main code
    /*
    Most of these test cases can only test the logic of the functions
    This is because I cannot connect to the server itself to verify
     */
    @BeforeAll
    static void setUpEncryptionKey() throws Exception {
        Network.setupTestEncryption();
    }

//    @Test
//    void testSuccessfulLogin() throws Exception {
//        byte[] encryptedTrue = Network.encrypt("true");
//        byte[] simulatedServerResponse = ByteBuffer.allocate(4 + encryptedTrue.length)
//                .putInt(encryptedTrue.length)
//                .put(encryptedTrue)
//                .array();
//        // allocates as much space as needed and fills it with data
//        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
//        // create the fake server
//
//        Network myNetwork = new Network(stubSocket);
//
//        boolean loginResult = myNetwork.login("testUser", "testPassword");
//        assertTrue(loginResult);
//    }

    /*
    Testing scenarios where the server returns false
     */
//    @Test
//    void testFailedLogin() throws Exception{
//        byte[] encryptedFalse = Network.encrypt("false");
//        byte[] simulatedServerResponse = ByteBuffer.allocate(4 + encryptedFalse.length)
//                .putInt(encryptedFalse.length)
//                .put(encryptedFalse)
//                .array();
//        // creates a fake response
//        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
//        // create the fake server
//
//        Network myNetwork = new Network(stubSocket);
//
//        boolean loginResult = myNetwork.login("testUser", "testPassword");
//        assertFalse(loginResult);
//    }

//    @Test
//    void testAccountCreation() throws Exception{
//        byte[] encryptedTrue = Network.encrypt("true");
//        byte[] simulatedServerResponse = ByteBuffer.allocate(4 + encryptedTrue.length)
//                .putInt(encryptedTrue.length)
//                .put(encryptedTrue)
//                .array();
//
//        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
//        // create the fake server
//
//        Network myNetwork = new Network(stubSocket);
//        boolean loginResult = myNetwork.registerAccount("testUser", "testPassword");
//        assertTrue(loginResult);
//    }

    /*
    Testing that password length is properly enforced
     */
//    @Test
//    void testPasswordLengthValidation() throws Exception{
//        StubSocket stubSocket = new StubSocket(new byte[0]); // intialize with empty byte
//        Network myNetwork = new Network(stubSocket);
//
//        //short password
//        assertFalse(myNetwork.registerAccount("testUser", "short"));
//        //long password
//        assertFalse(myNetwork.registerAccount("testUser", "verylongpasswordsolongitshouldntbevalid"));
//    }

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

    /*
    Tests the methods used to get games from the database
     */
//    @Test
//    void testPullingGames() throws Exception{
//        String gameOne = "game_01^Test Game 1^test game numero uno^test 1 tag 1`test 1 tag 2^i dunno black^randomurl.com^true";
//        String gameTwo = "game_02^Test Game 2^test game numero dos^test 2 tag 1`test 2 tag 2^i dunno white^mayberandomurl.com^false";
//        // the fake games we will be using
//
//        byte[] encryptedOne = Network.encrypt(gameOne);
//        byte[] encryptedTwo = Network.encrypt(gameTwo);
//
//        int totalSpace = (4 + encryptedOne.length) + (4 + encryptedTwo.length);
//        // figure out how much space to allocate
//
//        byte[] toSend = ByteBuffer.allocate(totalSpace)
//                .putInt(encryptedOne.length) //
//                .put(encryptedOne)
//                .putInt(encryptedTwo.length)
//                .put(encryptedTwo)
//                .array();
//        // create a packet to send
//
//        // create the fake server with this information
//        StubSocket stubSocket = new StubSocket(toSend);
//        Network myNetwork = new Network(stubSocket);
//
//        myNetwork.getGames();
//
//        Game one = GameRegistry.getInstance().findById("game_01");
//        Game two = GameRegistry.getInstance().findById("game_02");
//
//        assertEquals("Test Game 1",one.getTitle());
//        assertEquals("Test Game 2",two.getTitle());
//    }
}
