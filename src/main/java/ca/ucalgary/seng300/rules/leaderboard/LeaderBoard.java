package ca.ucalgary.seng300.rules.leaderboard;

import ca.ucalgary.seng300.core.identity.client.Network;
import javafx.css.Match;

import java.util.*;

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
     * get the leaderboard based on ranking (like the top 10 players)
     */
    public static List<LeaderboardEntry> getLeaderboard (GameType gameType){
        // get the leaderboard based on rank
        try {
            List<List<LeaderboardEntry>> leaderboard = (List<List<LeaderboardEntry>>) Network.getInstance().queueRequest(Network.GET_LEADERBOARD, null).get();
            if (gameType == GameType.TICTACTOE) return leaderboard.get(0);
            else if (gameType == GameType.CONNECT4) return leaderboard.get(1);
            else return leaderboard.get(2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param username
     * @return the record (username, TTT wins, TTT matches, C4 wins, C4 matches, total wins, total matches)
     */
    public static UserRecord getUserRecord (String username){
        try {
            List<MatchRecord> records = (List<MatchRecord>) Network.getInstance().queueRequest(Network.GET_MATCH_RECORD, new String[]{username}).get();
            int winsTTT = 0;
            int winsC4 = 0;
            int matchesTTT = 0;
            int matchesC4 = 0;

            for (MatchRecord record : records){
                if (record.getGameType() == GameType.TICTACTOE){
                    matchesTTT++;
                    if (record.isWinner(username)){
                        winsTTT++;
                    }
                }
                else if (record.getGameType() == GameType.CONNECT4){
                    matchesC4++;
                    if (record.isWinner(username)){
                        winsC4++;
                    }
                }
            }
            return new UserRecord(username, winsTTT, matchesTTT, winsC4, matchesC4);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    /**
     *
     * @param username
     * @return the Match Records from newest to oldest order
     */
    public static List<MatchRecord> getUserMatchRecords (String username){
        try {

            List<MatchRecord> records = (List<MatchRecord>) Network.getInstance().queueRequest(Network.GET_MATCH_RECORD, new String[]{username}).get();
            records.sort(Comparator.comparing(MatchRecord::getDate).reversed());

            return records;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
