package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.identity.client.Network;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

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

    // this BeforeAll is effectively a test in itself, which tests if a connection can be made

    /*
    If the BeforeAll fails, then NONE of these tests are run
     */
    @Test
    void testSuccessfulAndFailedAccountCreation() throws Exception {
        //attempt to connect to the server
        Network clientNet = Network.getInstance("127.0.0.1", 14001);

        // test creating an account
        String[] testAcc = {"testusername", "password123"};

        // send request to the server
        int result = (Integer) clientNet.queueRequest(Network.CREATE_ACCOUNT, testAcc).get();

        // check the server sends back what we need
        Assertions.assertEquals(1, result);

        int secondResult = (Integer) clientNet.queueRequest(Network.CREATE_ACCOUNT, testAcc).get();
        Assertions.assertNotEquals(1, secondResult); // should fail because existing username
    }

    @Test
    void testDataRetrieval() throws Exception {

    }

    @Test
    void testShutdown() throws Exception {

    }

    // Matchmaking tests should be an entity of their own, so I will not be testing them.

}
