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
    private int playerID;
    private String playerName;
    private int wins;
    private int matches;

    public LeaderboardEntry(int rank, int id, String name, int wins, int matches){
        this.rank = rank;
        this.playerID = id;
        this.playerName = name;
        this.wins = wins;
        this.matches = matches;
    }

    /*
    Dummy leaderboard entry for testing purposes
     */
    public LeaderboardEntry(){
        this.rank = 0;
        this.playerID = 0;
        this.playerName = "N/A";
        this.wins = 0;
        this.matches = 0;
    }

    public int compareTo(LeaderboardEntry e)
    {
        return Integer.compare(e.wins, this.wins);
    }

    public String toDisplay(){
        return String.format("%d. %s: %d wins", rank, playerName, wins);
    }


    // getters
    public int getPlayerID(){
        return playerID;
    }
    public int getRank(){
        return rank;
    }
    public String getPlayerName(){
        return playerName;
    }
    public int getWins(){
        return wins;
    }
    public int getMatches(){
        return matches;
    }
}
