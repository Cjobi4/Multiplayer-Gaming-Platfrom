package ca.ucalgary.seng300.rules.leaderboard;

import ca.ucalgary.seng300.shared.models.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LeaderboardDatabase {
    private static ArrayList<LeaderboardEntry> leaderboardTICTACTOE = new ArrayList<>();
    private static ArrayList<LeaderboardEntry> leaderboardCONNECT4 = new ArrayList<>();
    private static ArrayList<LeaderboardEntry> leaderboardCOMBINED = new ArrayList<>();

    public static void saveMatchRecordToUser(MatchRecord matchRecord){
        // connect the match record to a player
        // playerID and matchRecord
        int playerID = matchRecord.getPlayerID();

        if (matchRecord.getGameType() == GameType.TICTACTOE){
            // match under tictactoe
        }

        else if (matchRecord.getGameType() == GameType.CONNECT4){
            // match under connect4
        }

    }

    public static void loadScoreEntryFromUser(int playerID){
        // load lists of matches of a specific player in database
    }


    // load Leaderboards
    public static void loadLeaderboard(GameType gameType){
        // retrieve leaderboard from the database based on the game type

    }

    // Get entry from leaderboard
    public static LeaderboardEntry getLeaderboardEntry(GameType gameType, int rank){
        loadLeaderboard(gameType);

        if (gameType == GameType.TICTACTOE){
            return leaderboardTICTACTOE.get(rank);

        }

        else if (gameType == GameType.CONNECT4){
            return leaderboardCONNECT4.get(rank);
        }
        else if (gameType == GameType.COMBINED){
            return leaderboardCOMBINED.get(rank);
        }
        return null;
    }


}
