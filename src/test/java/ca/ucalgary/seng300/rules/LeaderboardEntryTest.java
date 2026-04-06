package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the methods from the LeaderboardEntry class
 */
class LeaderboardEntryTest {

    /**
     * Testing the getUsername method
     * Input: username "Q&T"
     * Expected Output: "Q&T"
     * Actual Output: the value retrieved by getUsername
     */
    @Test
    void testGetUsernameSuccessfully() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry("Q&T", 2, 10);
        String expectedUsername = "Q&T";
        String actualUsername = leaderboardEntry.getUsername();

        assertEquals(expectedUsername, actualUsername, "Username should be: " + expectedUsername);
    }

    /**
     * Testing the getWins method
     * Input: 2
     * Expected Output: 2
     * Actual Output: the value retrieved by getWins
     */
    @Test
    void testGetWinsSuccessfully() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry("Q&T", 2, 10);
        int expectedWins = 2;
        int actualWins = leaderboardEntry.getWins();

        assertEquals(expectedWins, actualWins, "Wins should be: " + expectedWins);
    }

    /**
     * Testing the getMatches method
     * Input: 10
     * Expected Output: 10
     * Actual Output: the value retrieved by getMatches
     */
    @Test
    void testGetMatchesSuccessfully() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry("Q&T", 2, 10);
        int expectedMatches = 10;
        int actualMatches = leaderboardEntry.getMatches();

        assertEquals(expectedMatches, actualMatches, "Matches should be: " + expectedMatches);
    }
}