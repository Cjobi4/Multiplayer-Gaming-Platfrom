package ca.ucalgary.seng300.rules;


import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Very rough skeleton of tests, will increase in volume and complexity down the line.

 * Depends on LeaderBoard class, which I made a stub for to support this version of tests.
 */

public class LeaderboardLogicTest {

    /*
    Currently, these tests are not valid due to how the leaderboard itself is not complete.
    Will later modify and make sure they work whenever able to
     */
    private LeaderBoard testLeaderboard = new LeaderBoard(); // create testLeaderBoard

    /*
    @BeforeEach
    void makeDummyBoard(){ // base LeaderboardEntries
        LeaderboardEntry basicEntry = new LeaderboardEntry(0, 1, "basic", 4, 10);
        LeaderboardEntry lowestScore = new LeaderboardEntry(0, 2, "lowest", 1, 10);
        LeaderboardEntry middleScore = new LeaderboardEntry(0, 3, "medium", 5, 10);
        LeaderboardEntry highestScore = new LeaderboardEntry(0, 4, "highest", 10, 10);

        testLeaderboard.submitScore(1, basicEntry);
        testLeaderboard.submitScore(2, lowestScore);
        testLeaderboard.submitScore(3, middleScore);
        testLeaderboard.submitScore(4, highestScore);
        // add the entries to the leaderboard
    }

    @Test
    void addScore(){
        LeaderboardEntry toAdd = new LeaderboardEntry(0, 5, "test", 7, 15);
        testLeaderboard.submitScore(5, toAdd);

        Assertions.assertEquals(7, testLeaderboard.getScore(5));
    }

    @Test
    void requestTopScoreDistinct(){
        Assertions.assertEquals(10, testLeaderboard.getTopScore());
    }

    /**
     * Tests the modification of a score
     */
    /*
    @Test
    void modifyScore(){
        LeaderboardEntry rewrite = new LeaderboardEntry(0, 1, "newBasic", 6, 12);
        testLeaderboard.submitScore(1, rewrite);
        // modify most parameters of the basic entry

        Assertions.assertEquals(6, testLeaderboard.getScore(1));
        Assertions.assertEquals("newBasic", testLeaderboard.getName(1));
    }

    @Test
    void requestUserScore(){
        Assertions.assertEquals(5, testLeaderboard.getScore(3));
    }

    @Test
    void requestTopFour(){
        List<LeaderboardEntry> testList = testLeaderboard.getTopPlayers(4);

        Assertions.assertEquals(10, testList.getFirst().getWins());
        Assertions.assertEquals(5, testList.get(1).getWins());
        Assertions.assertEquals(4, testList.get(2).getWins());
        Assertions.assertEquals(1, testList.get(3).getWins());
    }
    */

}
