package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.rules.leaderboard.MatchRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SessionTest {

    /*
    Tests that require an active connection with the main server
    For this reason, we cannot simply fake an intended output
     */
    @BeforeAll
    static void establishConnection(){
        boolean isServerAvailable = false; // assume we can't connect to the server

        try(Socket testConnect = new Socket("127.0.0.1", 14001)){
            isServerAvailable = true; // connection could be established
        }catch(IOException e){
            isServerAvailable = false; // connection could not be established
        }

        assumeTrue(isServerAvailable, "Tests could not be run, no connection to the server.");
        // if no connection is made, the rest of the tests will be skipped as they WILL fail with no connection
    }


    /**
    If the BeforeAll fails, then NONE of these tests are run
     */


    @Test
    void testSuccessfulAndFailedAccountCreation() throws Exception {
        //attempt to connect to the server
        Network connect = Network.getInstance("127.0.0.1", 14001);
        // test creating an account
        String[] testAcc = {"testuser_" + System.currentTimeMillis(), "password123"};
        // simply generates a unique username because we actually do edit the database when running this test

        // send request to the server
        int result = (Integer) connect.queueRequest(Network.CREATE_ACCOUNT, testAcc).get();

        // check the server sends back what we need
        Assertions.assertEquals(1, result);

        int secondResult = (Integer) connect.queueRequest(Network.CREATE_ACCOUNT, testAcc).get();
        Assertions.assertNotEquals(1, secondResult); // should fail because existing username
    }

    /**
    Testing the retrieval of games from the database
     */
    @Test
    void testGameListRetrieval() throws Exception {
        try {
            Network connect = Network.getInstance("127.0.0.1", 14001);

            // queue request for game list
            connect.queueRequest(Network.GET_GAME_LIST, null).get();

            // amount of games in the game registry
            int gameCount = GameRegistry.getInstance().ListAll().size();

            // after a successful retrieval there should be more than 0 games
            Assertions.assertTrue(gameCount > 0);

        } catch (Exception e) {
            System.out.println("Error with retrieving Game List: " + e);
        }
    }

    @Test
    void testLeaderboardRetrieval() throws Exception {
        Network connect = Network.getInstance("127.0.0.1", 14001);

        // queue request for TTT leaderboard
        // cast the CompletableFuture result back into a List
        List<LeaderboardEntry> board =
                (List) connect.queueRequest(Network.GET_LEADERBOARD, new String[]{"ttt"}).get();

        // assert something was sent back
        Assertions.assertNotNull(board);
    }

    @Test
    void testMatchRecordRetrieval() throws Exception {
        Network connect = Network.getInstance("127.0.0.1", 14001);

        // test username to search for
        String usernameToSearch = "testusername";

        List<MatchRecord> records = connect.getMatchRecords(usernameToSearch);

        // assert something was sent back
        Assertions.assertNotNull(records);
    }

    @Test
    void testShutdown() throws Exception {
        Network connect = Network.getInstance("127.0.0.1", 14001);

        // send the logout byte to the server
        connect.queueRequest(Network.LOGOUT, null).get();
        // may be impossible to check for a logout but here just in case
    }

    // Matchmaking tests should be an entity of their own, so I will not be testing them.

}
