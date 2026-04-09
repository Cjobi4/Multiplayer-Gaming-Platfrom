package ca.ucalgary.seng300.rules.leaderboard;

/**
 * for holding a user wins and matches data
 */
public class UserRecord {
    private String username;
    private int winsTTT;
    private int matchesTTT;
    private int winsC4;
    private int matchesC4;
    private int totalWins;
    private int totalMatches;

    public UserRecord(String username, int winsTTT, int matchesTTT, int winsC4, int matchesC4){
        this.username = username;
        this.winsTTT = winsTTT;
        this.matchesTTT = matchesTTT;
        this.winsC4 = winsC4;
        this.matchesC4 = matchesC4;
        this.totalWins = winsTTT + winsC4;
        this.totalMatches = matchesTTT + matchesC4;
    }

    // getters
    public int getWinsTTT(){
        return winsTTT;
    }
    public int getWinsC4 (){
        return winsC4;
    }
    public int getMatchesTTT(){
        return matchesTTT;
    }
    public int getMatchesC4(){
        return matchesC4;
    }
    public int getTotalWins(){
        return totalWins;
    }
    public int getTotalMatches(){
        return totalMatches;
    }
    public boolean isEmpty(){
        if (username == null || username.isEmpty()) return true;
        return false;
    }
}
