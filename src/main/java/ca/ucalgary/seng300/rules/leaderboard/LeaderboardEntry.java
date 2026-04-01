package ca.ucalgary.seng300.rules.leaderboard;

import java.util.Objects;

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

    public LeaderboardEntry(int playerID, String playerName, int wins, int matches){
        this.wins = wins;
        this.playerID = playerID;
        this.matches = matches;
        this.playerName = playerName;
    }
    public int compareTo(LeaderboardEntry e)
    {
        return Integer.compare(e.wins, this.wins);
    }

    public String toDisplay(){
        return String.format("%d. %s: %d wins", rank, playerName, wins);
    }

    // Used for testing, particular in assertEquals
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardEntry that = (LeaderboardEntry) o;
        return playerID == that.playerID && wins == that.wins && matches == that.matches && Objects.equals(playerName, that.playerName);
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
