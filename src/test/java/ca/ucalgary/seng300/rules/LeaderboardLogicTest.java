package ca.ucalgary.seng300.rules;


import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaderboardLogicTest {

    private LeaderBoard testLeaderboard; // create testLeaderBoard

    @BeforeEach
    void makeDummyBoard(){ // populate testLeaderBoard

    }

    @Test
    void addScore(){

    }

    @Test
    void requestTopScoreDistinct(){
        //TODO: Test case for when only one top score
    }

    @Test
    void requestTopScoreSimilar(){
        //TODO: Case in which two matching top scores
    }

    @Test
    void requestUserScore(){
        //TODO: Requesting specific score based on UID
    }

    @Test
    void deleteScore(){
        //TODO: Delete a score based on certain UID
    }


}
