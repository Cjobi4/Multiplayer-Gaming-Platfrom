package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.shared.models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkTest {

    /*
    Most of these test cases can only test the logic of the functions
    This is because I cannot connect to the server itself to verify
     */
    @BeforeAll
    static void setUpEncryptionKey() throws Exception {
        Network.setupTestEncryption();
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        byte[] simulatedServerResponse = new byte[]{1};
        // for this function, we don't read a response, but rather the raw byte data
        // as such we simply make a '1' response
        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
        // create the fake server

        Network myNetwork = new Network(stubSocket);

        int loginResult = myNetwork.login("testUser", "testPassword");
        assertEquals(1, loginResult);
    }

    /*
    Testing scenarios where the server returns false
     */
    @Test
    void testFailedLogin() throws Exception{
        byte[] simulatedServerResponse = new byte[]{0}; // 0 for failed login
        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
        // create the fake server

        Network myNetwork = new Network(stubSocket);

        int loginResult = myNetwork.login("testUser", "testPassword");
        assertEquals(0, loginResult);
    }

    @Test
    void testAccountCreation() throws Exception{
        byte[] simulatedServerResponse = new byte[]{1}; // 1 for successful creation
        StubSocket stubSocket = new StubSocket(simulatedServerResponse);
        // create the fake server

        Network myNetwork = new Network(stubSocket);

        int loginResult = myNetwork.login("testUser", "testPassword");
        assertEquals(1, loginResult); //success

        simulatedServerResponse = new byte[]{0}; // 0 for failed creation due to taken username
        stubSocket = new StubSocket(simulatedServerResponse);
        myNetwork = new Network(stubSocket);

        loginResult = myNetwork.registerAccount("testUser", "testPassword");
        assertEquals(0, loginResult);

        simulatedServerResponse = new byte[]{2}; // 2 for failed due to invalid credentials
        stubSocket = new StubSocket(simulatedServerResponse);
        myNetwork = new Network(stubSocket);

        loginResult = myNetwork.registerAccount("testUser", "testPassword");
        assertEquals(2, loginResult);

        simulatedServerResponse = new byte[]{3}; // 0 for failed creation
        stubSocket = new StubSocket(simulatedServerResponse);
        myNetwork = new Network(stubSocket);

        loginResult = myNetwork.registerAccount("testUser", "testPassword");
        assertEquals(3, loginResult);
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

    /*
    Tests the methods used to get games from the database
     */
    @Test
    void testPullingGames() throws Exception{
        String gameOne = "game_01^Test Game 1^test game numero uno^test 1 tag 1`test 1 tag 2^i dunno black^randomurl.com^true";
        String gameTwo = "game_02^Test Game 2^test game numero dos^test 2 tag 1`test 2 tag 2^i dunno white^mayberandomurl.com^false";
        // the fake games we will be using

        byte[] encryptedOne = Network.encrypt(gameOne);
        byte[] encryptedTwo = Network.encrypt(gameTwo);

        int totalSpace = (4 + encryptedOne.length) + (4 + encryptedTwo.length);
        // figure out how much space to allocate

        byte[] toSend = ByteBuffer.allocate(totalSpace)
                .putInt(encryptedOne.length) //
                .put(encryptedOne)
                .putInt(encryptedTwo.length)
                .put(encryptedTwo)
                .array();
        // create a packet to send

        // create the fake server with this information
        StubSocket stubSocket = new StubSocket(toSend);
        Network myNetwork = new Network(stubSocket);

        myNetwork.getGames();

        Game one = GameRegistry.getInstance().findById("game_01");
        Game two = GameRegistry.getInstance().findById("game_02");

        assertEquals("Test Game 1",one.getTitle());
        assertEquals("Test Game 2",two.getTitle());
    }
}
