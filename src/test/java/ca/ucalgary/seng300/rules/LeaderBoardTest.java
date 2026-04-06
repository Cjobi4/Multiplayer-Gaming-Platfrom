package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    @Test
    void testGetUserRecord() {
    }

    @Test
    void testGetUserMatchRecords() {
    }

    @Test
    void testWhenUserRecordDoesNotExist() {
    }

    @Test
    void testWhenMatchRecordDoesNotExist() {
    }
}