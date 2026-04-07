package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.rules.leaderboard.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaderBoardTest {
    private static LeaderBoardNetworkStub stubNetwork;

    /**
     * BeforeAll is setting up the stub network in place of the actual network
     * For the ease of testing
     * @throws Exception
     */
    @BeforeAll
    static void setUp() throws Exception {
        stubNetwork = new LeaderBoardNetworkStub();

        Field instanceField = Network.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, stubNetwork);
    }

    /**
     * Testing the getLeaderBoard method
     * Input: 3 entries
     * Expected Output: 3 entries in descending order of wins --> neocity, john doe, jane doe
     */
    @Test
    void testGetLeaderboard() {
        List<LeaderboardEntry> data = new ArrayList<>();

        // setting up the entries first and then adding them
        LeaderboardEntry leaderboardEntry1 = new LeaderboardEntry("jane doe", 1, 1);
        LeaderboardEntry leaderboardEntry2 = new LeaderboardEntry("john doe", 2, 20);
        LeaderboardEntry leaderboardEntry3 = new LeaderboardEntry("neocity", 7, 8);
        data.add(leaderboardEntry1);
        data.add(leaderboardEntry2);
        data.add(leaderboardEntry3);

        stubNetwork.setResponse(data);

        List <LeaderboardEntry> result = LeaderBoard.getLeaderboard(GameType.CONNECT4);

        // should be organized in descending order
        assertEquals(data, result, "The input list should be the same as the output.");
        assertEquals("neocity", result.get(0).getUsername(), "First place on leaderboard should be: neocity");
        assertEquals("john doe", result.get(1).getUsername(),  "Second place on leaderboard should be: john doe");
        assertEquals("jane doe", result.get(2).getUsername(), "Last place on leaderboard should be: jane doe");
    }

    /**
     * Testing the getLeaderBoard method
     * Input: 3 entries with all equal wins
     * Expected Output: 3 entries in order of input --> jane doe, neocity, john doe
     */
    @Test
    void testGetLeaderboardWhenWinsAreEqual() {
        List<LeaderboardEntry> data = new ArrayList<>();

        // setting up the entries first and then adding them
        LeaderboardEntry leaderboardEntry1 = new LeaderboardEntry("jane doe", 1, 1);
        LeaderboardEntry leaderboardEntry2 = new LeaderboardEntry("neocity", 1, 8);
        LeaderboardEntry leaderboardEntry3 = new LeaderboardEntry("john doe", 1, 20);
        data.add(leaderboardEntry1);
        data.add(leaderboardEntry2);
        data.add(leaderboardEntry3);

        stubNetwork.setResponse(data);

        List <LeaderboardEntry> result = LeaderBoard.getLeaderboard(GameType.CONNECT4);

        // should be organized in descending order
        assertEquals(data, result, "The input list should be the same as the output.");
        assertEquals("jane doe", result.get(0).getUsername(), "First place on leaderboard should be: jane doe");
        assertEquals("neocity", result.get(1).getUsername(),  "Second place on leaderboard should be: neocity");
        assertEquals("john doe", result.get(2).getUsername(), "Last place on leaderboard should be: john doe");
    }

    /**
     * Testing the getUserRecord method
     * Input: username1
     * Expected Output: user record of username1
     */
    @Test
    void testGetUserRecord() {
        String username1 = "jane doe";
        String username2 = "john doe";

        // populating the match record
        MatchRecord test1 = new MatchRecord(username1, username2, GameType.CONNECT4, username1);
        MatchRecord test2 = new MatchRecord(username1, username2, GameType.CONNECT4, username2);
        MatchRecord test3 = new MatchRecord(username1, username2, GameType.TICTACTOE, username1);
        List<MatchRecord> records = List.of(test1, test2, test3);

        stubNetwork.setResponse(records);

        UserRecord result = LeaderBoard.getUserRecord(username1);

        assertEquals(1, result.getWinsTTT());
        assertEquals(1, result.getWinsC4());
        assertEquals(3, result.getTotalMatches());
        assertEquals(2, result.getTotalWins());
    }

    /**
     * Testing the getMatchRecord method
     * Input: username1
     * Expected Output: match records of username1 in descending order of date
     */
    @Test
    void testGetUserMatchRecords() {
        String username1 = "jane doe";
        String username2 = "john doe";

        // populating the match record
        MatchRecord test1 = new MatchRecord(username1, username2, GameType.CONNECT4, username1, "2026-04-01 10:00:00");
        MatchRecord test2 = new MatchRecord(username1, username2, GameType.CONNECT4, username2, "2026-04-01 10:05:00");
        MatchRecord test3 = new MatchRecord(username1, username2, GameType.CONNECT4, username1, "2026-04-01 10:10:00");
        List<MatchRecord> records = new ArrayList<>(List.of(test1, test2, test3));

        stubNetwork.setResponse(records);
        List<MatchRecord> result = LeaderBoard.getUserMatchRecords(username1);

        // most recent date should be first
        assertEquals("2026-04-01 10:10:00", result.get(0).getDate());
        assertEquals("2026-04-01 10:05:00", result.get(1).getDate());
        assertEquals("2026-04-01 10:00:00", result.get(2).getDate());
    }

    /**
     * Testing getUserRecord when the user doesn't have any records
     * Input: testUser1
     * Expected Output: 0 values for TTTWins, C4Wins, Total Matches, and Total Wins
     *
     * f a user record doesn't exist, one should be created with values of 0 initialized
     */
    @Test
    void testWhenUserRecordDoesNotExist() {
        stubNetwork.setResponse(new ArrayList<MatchRecord>()); // empty !
        UserRecord result = LeaderBoard.getUserRecord("testUser1");

        assertEquals(0, result.getWinsTTT());
        assertEquals(0, result.getWinsC4());
        assertEquals(0, result.getTotalMatches());
        assertEquals(0, result.getTotalWins());
    }

    /**
     * Testing getMatchRecords when math records for the user don't exist
     * Input: testUser1
     * Expected Output: empty match record
     */
    @Test
    void testWhenMatchRecordDoesNotExist() {
        stubNetwork.setResponse(new ArrayList<MatchRecord>());
        List<MatchRecord> result = LeaderBoard.getUserMatchRecords("testUser");
        assertTrue(result.isEmpty(), "There should be no match record for this user.");
    }
}