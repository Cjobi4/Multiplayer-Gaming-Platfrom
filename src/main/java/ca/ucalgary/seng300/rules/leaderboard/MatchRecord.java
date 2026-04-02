package ca.ucalgary.seng300.rules.leaderboard;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Create a match record after a match for both players
 * Or when need to get data from the database (easier when we have getters)
 */
public class MatchRecord {
    private int playerOneID;
    private int playerTwoID;
    private GameType gameType;
    private String date;
    private int winnerID;

    /**
     *
     * @param playerOneID int ID of player 1
     * @param playerTwoID int ID of player 2
     * @param gameType
     * @param winnerID int ID of the winner
     */
    public MatchRecord(int playerOneID, int playerTwoID, GameType gameType, int winnerID){
        this.playerOneID = playerOneID;
        this.playerTwoID = playerTwoID;
        this.gameType = gameType;
        this.winnerID = winnerID;

        ZoneId zoneId = ZoneId.of("Canada/Edmonton");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = now.format(formatter);
    }

    /**
     * Create Match Record when retrieving database (for easier getting data)
     */
    public MatchRecord (int playerOneID, int playerTwoID, GameType gameType, int winnerID, String date){
        this.playerOneID = playerOneID;
        this.playerTwoID = playerTwoID;
        this.gameType = gameType;
        this.winnerID = winnerID;
        this.date = date;

    }

    public boolean isWinner(int playerID){
        return playerID == winnerID;
    }


    public GameType getGameType(){
        return gameType;
    }
    public String getDate(){
        return this.date;
    }

    /**
     * to store in the database (under the userID, one user can have multiple Match Record)
     * @return
     */
    public String toString(){
        // matchID + playerOneID + playerTwoID + gameType + winnerID + date
        return playerOneID + " " + playerTwoID + " " + gameType + " " + winnerID + " " + date;
    }
}
