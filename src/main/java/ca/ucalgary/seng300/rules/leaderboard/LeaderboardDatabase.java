package ca.ucalgary.seng300.rules.leaderboard;

import ca.ucalgary.seng300.shared.models.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LeaderboardDatabase {
    private static ArrayList<LeaderboardEntry> leaderboardTICTACTOE = new ArrayList<>();
    private static ArrayList<LeaderboardEntry> leaderboardCONNECT4 = new ArrayList<>();
    private static ArrayList<LeaderboardEntry> leaderboardCOMBINED = new ArrayList<>();
    protected static ArrayList<MatchRecord> userMatchRecords = new ArrayList<>();

    public static void saveMatchRecordToUser(MatchRecord matchRecord){
        // connect the match record under a player
        // playerID and matchRecord

        if (matchRecord.getGameType() == GameType.TICTACTOE){
            // match under tictactoe
        }

        else if (matchRecord.getGameType() == GameType.CONNECT4){
            // match under connect4
        }

    }

    public static void loadUserMatchRecords(int playerID){
        // load lists of matches of a specific player in database
        // convert them into Match Record arraylist
    }


    // load Leaderboards
    public static void loadLeaderboard(GameType gameType){
        // retrieve leaderboard from the database based on the game type
        ArrayList<String> leaderBoardString;

    }

    // Get entry from leaderboard
    public static LeaderboardEntry getLeaderboardEntry(GameType gameType, int rank){
        loadLeaderboard(gameType);

        if (gameType == GameType.TICTACTOE) return leaderboardTICTACTOE.get(rank);
        else if (gameType == GameType.CONNECT4) return leaderboardCONNECT4.get(rank);
        else return leaderboardCOMBINED.get(rank);
    }

    public static void updateLeaderboard(){
        // when the user record is changed -> update leaderboard in local + database
        //
    }
}
