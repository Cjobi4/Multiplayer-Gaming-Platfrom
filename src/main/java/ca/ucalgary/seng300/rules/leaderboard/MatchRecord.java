package ca.ucalgary.seng300.rules.leaderboard;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Create a match record after a match for both players
 * Or when need to get data from the database (easier when we have getters)
 */
public class MatchRecord {
    private String username1;
    private String username2;
    private GameType gameType;
    private String date;
    private String winner;

    /**
     *
     * @param username1
     * @param username2
     * @param gameType
     * @param winner
     */
    public MatchRecord(String username1, String username2, GameType gameType, String winner){
        this.username1 = username1;
        this.username2 = username2;
        this.gameType = gameType;
        this.winner = winner;

        ZoneId zoneId = ZoneId.of("America/Edmonton");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = now.format(formatter);
    }

    /**
     * Create Match Record when retrieving database (for easier getting data)
     */
    public MatchRecord (String username1, String username2, GameType gameType, String winnerID, String date){
        this.username1 = username1;
        this.username2 = username2;
        this.gameType = gameType;
        this.winner = winnerID;
        this.date = date;

    }

    public boolean isWinner(String username){
        return username.equals(winner);
    }


    public GameType getGameType(){
        return gameType;
    }
    public String getDate(){
        return this.date;
    }
    public String getPlayerOne(){ return this.username1; } // basic getters
    public String getPlayerTwo(){ return this.username2; }

    /**
     * to store in the database (under the userID, one user can have multiple Match Record)
     * @return
     */
    public String toString(){
        // matchID + playerOneID + playerTwoID + gameType + winnerID + date
        return username1 + " " + username2 + " " + gameType + " " + winner + " " + date;
    }

    public boolean isEmtpy() {
        if (username1 == null || username1.isEmpty()) return true;
        else if (username2 == null || username2.isEmpty()) return true;
        else return winner == null || winner.isEmpty();
    }
}
