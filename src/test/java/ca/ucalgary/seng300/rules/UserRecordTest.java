package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.rules.leaderboard.UserRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRecordTest {

   // setting this up once
    UserRecord userRecord = new UserRecord("user1", 5, 6, 3, 7);

    @Test
    void testGetWinsTTT() {
        int expectedWinsTTT = 5;
        int actualWinsTTT = userRecord.getWinsTTT();

        assertEquals(expectedWinsTTT, actualWinsTTT, "Wins for TTT should be: " + expectedWinsTTT);
    }

    @Test
    void testGetWinsC4() {
        int expectedWinsC4 = 3;
        int actualWinsC4 = userRecord.getWinsC4();

        assertEquals(expectedWinsC4, actualWinsC4, "Wins for C4 should be: " + expectedWinsC4);
    }

    @Test
    void testGetMatchesTTT() {
        int expectedMatches = 6;
        int actualMatches = userRecord.getMatchesTTT();

        assertEquals(expectedMatches, actualMatches, "Matches for TTT should be: " + expectedMatches);
    }

    @Test
    void testGetMatchesC4() {
        int expectedMatches = 7;
        int actualMatches = userRecord.getMatchesC4();

        assertEquals(expectedMatches, actualMatches, "Matches for C4 should be: " + expectedMatches);
    }

    @Test
    void testGetTotalWins() {
        int expectedTotalWins = 8;
        int actualTotalWins = userRecord.getTotalWins();

        assertEquals(expectedTotalWins, actualTotalWins, "Total wins should be: " + expectedTotalWins);
    }

    @Test
    void testGetTotalMatches() {
        int expectedTotalMatches = 13;
        int actualTotalMatches = userRecord.getTotalMatches();

        assertEquals(expectedTotalMatches, actualTotalMatches,  "Total matches should be: " + expectedTotalMatches);
    }
}