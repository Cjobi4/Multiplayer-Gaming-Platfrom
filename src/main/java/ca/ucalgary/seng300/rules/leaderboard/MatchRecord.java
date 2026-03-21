package ca.ucalgary.seng300.rules.leaderboard;

public class MatchRecord {
    private int playerID;
    private GameType gameType;
    private int gameID;
    private boolean win;

    public MatchRecord(int playerID, GameType gameType, int gameID, boolean win){
        this.playerID = playerID;
        this.gameType = gameType;
        this.gameID = gameID;
        this.win = win;
    }

    public int getPlayerID(){
        return playerID;
    }

    public int getGameID(){
        return gameID;
    }

    public GameType getGameType(){
        return gameType;
    }





}
