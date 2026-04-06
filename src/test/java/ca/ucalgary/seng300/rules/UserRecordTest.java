package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.rules.leaderboard.UserRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class focuses on testing methods for the UserRecord class
 */
class UserRecordTest {

   // setting this up once
    UserRecord userRecord = new UserRecord("user1", 5, 6, 3, 7);

    /**
     * testing the method getWinsTTT
     * Input: 5
     * Expected Output: 5
     * Actual Output: the value from getWinsTTT
     */
    @Test
    void testGetWinsTTT() {
        int expectedWinsTTT = 5;
        int actualWinsTTT = userRecord.getWinsTTT();

        assertEquals(expectedWinsTTT, actualWinsTTT, "Wins for TTT should be: " + expectedWinsTTT);
    }

    /**
     * testing the method getWinsC4
     * Input: 3
     * Expected Output: 3
     * Actual Output: the value from getWinsC4
     */
    @Test
    void testGetWinsC4() {
        int expectedWinsC4 = 3;
        int actualWinsC4 = userRecord.getWinsC4();

        assertEquals(expectedWinsC4, actualWinsC4, "Wins for C4 should be: " + expectedWinsC4);
    }

    /**
     * testing the method getMatchesTTT
     * Input: 6
     * Expected Output: 5
     * Actual Output: the value from getMatchesTTT
     */
    @Test
    void testGetMatchesTTT() {
        int expectedMatches = 6;
        int actualMatches = userRecord.getMatchesTTT();

        assertEquals(expectedMatches, actualMatches, "Matches for TTT should be: " + expectedMatches);
    }

    /**
     * testing the method getMatchesC4
     * Input: 7
     * Expected Output: 7
     * Actual Output: the value from getMatchesC4
     */
    @Test
    void testGetMatchesC4() {
        int expectedMatches = 7;
        int actualMatches = userRecord.getMatchesC4();

        assertEquals(expectedMatches, actualMatches, "Matches for C4 should be: " + expectedMatches);
    }

    /**
     * testing the method getTotalWins
     * Input: NA
     * Expected Output: 5 + 3 (from TTTWins and C4Wins)
     * Actual Output: the value from getTotalWins
     */
    @Test
    void testGetTotalWins() {
        int expectedTotalWins = 8;
        int actualTotalWins = userRecord.getTotalWins();

        assertEquals(expectedTotalWins, actualTotalWins, "Total wins should be: " + expectedTotalWins);
    }

    /**
     * testing the method getTotalMatches
     * Input: NA
     * Expected Output: 7 + 6 (from TTTMatches and C4Matches)
     * Actual Output: the value from getTotalMatches
     */
    @Test
    void testGetTotalMatches() {
        int expectedTotalMatches = 13;
        int actualTotalMatches = userRecord.getTotalMatches();

        assertEquals(expectedTotalMatches, actualTotalMatches,  "Total matches should be: " + expectedTotalMatches);
    }
}