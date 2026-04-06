package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.MatchRecord;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class is testing the methods from the MatchRecord class
 */
class MatchRecordTest {

    // initial set up
    String username1 = "username1";
    String username2 = "username2";
    GameType gameType1 = GameType.CONNECT4;
    String winner1 = "username1";
    String fakeWinner =  "username2";
    String date1 = "2019-05-05";

    MatchRecord matchRecord = new MatchRecord(username1, username2, gameType1, winner1);

    /**
     * Checking if the getter for isWinner is correctly running
     */
    @Test
    void testIsWinnerSuccessfully() {
        boolean actualWinner = matchRecord.isWinner(winner1);
        assertTrue(actualWinner, "The winner should be: " + winner1);
    }

    /**
     * Checking if the getter for isWinner is correctly running by returning false when fakeWinner is not the winner
     */
    @Test
    void testIsWinnerReturnsFalse() {
        boolean actualWinner = matchRecord.isWinner(fakeWinner);
        assertFalse(actualWinner, "The boolean value should be false");
    }

    /**
     * Checking if the getter for getGameType is correctly running
     */
    @Test
    void testGetGameType() {
        GameType actualGameType = matchRecord.getGameType();
        assertEquals(gameType1, actualGameType, "The game type should be " + gameType1);
    }

    /**
     * Checking if the getter for getDate is correctly running
     */
    @Test
    void testGetDate() {
        // this test should fail
        ZonedDateTime actualDate = ZonedDateTime.parse(matchRecord.getDate());
    }

    /**
     * Checking if toString is correctly running
     */
    @Test
    void testToString() {
        // this test should fail because of date
        String actualString = matchRecord.toString();
        String expectedString = username1 + " " + username2 + " " + gameType1 + " " + winner1 + " " + date1;
        assertEquals(expectedString, actualString, "The string should be " + expectedString);
    }
}