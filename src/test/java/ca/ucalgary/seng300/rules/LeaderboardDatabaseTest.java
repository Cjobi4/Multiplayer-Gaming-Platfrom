package ca.ucalgary.seng300.rules;

//TODO: Test database dependent leaderboard functions whenever finished

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LeaderboardDatabaseTest {

    @BeforeAll
    public static void setConnection(){
        //TODO: Initiate connection with main database
    }

    @Test
    void requestLeaderboard(){
        //TODO: Grab leaderboard from Database
    }

    @Test
    void testSendAndConfirmNewScore(){
        //TODO: Sends a score + UID to database, then confirms it was added
    }

    @AfterEach
    void cleanUpDatabase(){
        //TODO: Remove added scores based on test UID
    }

    @AfterAll
    public static void disconnect(){
        //TODO: Close connection with database
    }
}
