package ca.ucalgary.seng300.rules.leaderboard;

import javafx.css.Match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Manages leaderboard scoring, submission, and queries.
 *
 * <p>Handles score submission and validation, leaderboard ranking
 * calculations, and query/filtering operations. Persists scores via
 * the PersistenceService from the shared contracts layer. Display
 * is handled by the Client/UI leaderboard dashboard.</p>
 *
 * <p>TODO: Implement score submission, ranking, and query operations
 * using the shared LeaderboardEntry model.</p>
 */
public class LeaderBoard {
    // TODO: Implement leaderboard — placeholder for Rules & Validation

    /**
     * update the personal record (as in total wins & matches or just total matches)
     * @param playerID
     */
    public static void updateUserRecord (int playerID){
        // update wins and matches for the user
        updateUserRecord(playerID);
    }

    /**
     * get the leaderboard based on ranking (like the top 10 players)
     * @param rank
     */
    public static void getLeaderboard (int rank){
        // get the leaderboard based on rank

    }

    /**
     * get a player personal record (including total wins + matches)
     * @param playerID
     */
    public static void getUserRecord (int playerID){
        // get the personal record of the player (using their playerID)

    }

    public static ArrayList<MatchRecord> getUserMatchRecords (int playerID, int fromRange, int toRange){
        LeaderboardDatabase.loadUserMatchRecords(playerID);
        return LeaderboardDatabase.userMatchRecords;

    }

    /**
     * use after finish a match (for tracking game history)
     */
    public static void createMatchRecord (int playerOneID, int PlayerTwoID, int winnerID, GameType gameType){
        MatchRecord matchRecord = null;
    }
}
