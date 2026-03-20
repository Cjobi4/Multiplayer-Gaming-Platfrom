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
    private int rank;
    private String playerId;
    private String playerName;
    private int score;

    public LeaderboardEntry(int rank, String uid, String userName, int score){
        this.rank = rank;
        this.playerId = uid;
        this.playerName = userName;
        this.score = score;
    }

    public LeaderboardEntry(){ // default or dummy constructor
        this.rank = 0;
        this.playerId = "N/A";
        this.playerName = "N/A";
        this.score = -1;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public int compareTo(LeaderboardEntry e)
    {
        return Integer.compare(e.score, this.score);
    }

    public int getScore(){
        return this.score;
    }

    public String getPlayerId(){
        return this.playerId;
    }

    public String toDisplay()
    {
        return rank + ". " + playerName + " - " + score;
    }
}
