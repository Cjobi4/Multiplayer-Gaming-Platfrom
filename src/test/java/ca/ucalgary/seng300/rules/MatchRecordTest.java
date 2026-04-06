package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.MatchRecord;
import ca.ucalgary.seng300.shared.models.Game;
import ca.ucalgary.seng300.shared.models.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchRecordTest {

    // initial set up
    String username1 = "username1";
    String username2 = "username2";
    GameType gameType1 = GameType.CONNECT4;
    String winner1 = "username1";

    MatchRecord matchRecord = new MatchRecord(username1, username2, gameType1, winner1);

    @Test
    void testIsWinner() {
    }

    @Test
    void testGetGameType() {
    }

    @Test
    void testGetDate() {
    }

    @Test
    void testToString() {
    }
}