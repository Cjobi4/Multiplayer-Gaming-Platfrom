package ca.ucalgary.seng300.core;

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

    @Test
    void testStartSession(){

    }

    @Test
    void testEndSession(){

    }


    @Test
    void testSubmitScore(){

    }

    @Test
    void testGetDuration(){

    }
}
