package ca.ucalgary.seng300.rules;

//TODO: Test base leaderboard whenever finished

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaderboardLogicTest {

    @BeforeEach
    //TODO: Create "test" leaderboards with a few entries

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
