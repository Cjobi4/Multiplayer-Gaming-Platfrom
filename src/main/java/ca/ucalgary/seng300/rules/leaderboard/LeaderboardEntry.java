package ca.ucalgary.seng300.rules.leaderboard;

/**
 * Represents a single entry in the leaderboard.
 *
 * <p>Contains a player reference, score, rank, and timestamp. Used by
 * the Rules &amp; Validation leaderboard subsystem and displayed by
 * the Client/UI leaderboard dashboard.</p>
 *
 * <p>TODO: Define fields (player, score, rank, timestamp) and implement
 * comparable ordering for leaderboard sorting.</p>
 */
public class LeaderboardEntry {
    private String username;
    private int wins;
    private int matches;

    public LeaderboardEntry(int playerID, String username, int wins, int matches){
        this.wins = wins;
        this.matches = matches;
        this.username = username;
    }

    // getters
    public String getUsername(){
        return username;
    }
    public int getWins(){
        return wins;
    }
    public int getMatches(){
        return matches;
    }
}
