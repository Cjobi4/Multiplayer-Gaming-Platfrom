package ca.ucalgary.seng300.rules.leaderboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages leaderboard scoring, submission, and queries.
 *
 * <p>Handles score submission and validation, leaderboard ranking
 * calculations, and query/filtering operations. Persists scores via
 * the PersistenceService from the shared contracts layer. Display
 * is handled by the Client/UI leaderboard dashboard.</p>
 *
 * using the shared LeaderboardEntry model.</p>
 */
public class LeaderBoard{

    private Map<String, LeaderboardEntry> tempDatabase = new HashMap(); // hashmap to store leaderboard entries
    // VERY temporary. Only here for the purposes of testing
    // redundant store of UID for querying purposes
    /**
     * Note from Quality-Testing Team:
     * Very basic placeholder so we can complete testing
     * Feel free to change attributes to better suit the final product
     */

    /**
     * Adds a score to the leaderboard
     * @param uid is the id of the user
     * @param newEntry is simply the new entry
     */
    public void submitScore(String uid, LeaderboardEntry newEntry){
        tempDatabase.remove(uid); // removes if already exists
        tempDatabase.put(uid, newEntry); // basic addition function
        // definitely add on more later
    }

    public int getScore(String uid){ // retrieves score of given uid
        LeaderboardEntry toReturn = tempDatabase.getOrDefault(uid, new LeaderboardEntry());

        return toReturn.getScore();
        // default value of -1 if the uid doesn't exist
    }

    public String getName(String uid){
        LeaderboardEntry toReturn = tempDatabase.getOrDefault(uid, new LeaderboardEntry());

        return toReturn.getPlayerName();
    }

    public int getTopScore(){
        List<LeaderboardEntry> copy = new ArrayList<>(); // arraylist of lb entries

        for(LeaderboardEntry entry : tempDatabase.values()){
            copy.add(new LeaderboardEntry(0," ", " ", entry.getScore()));
            // for our copy we only care about score
        }

        copy.sort((a,b) -> Integer.compare(b.getScore(), a.getScore()));
        // sort highest to lowest
        return copy.getFirst().getScore();
    }

    public List<LeaderboardEntry> getTopPlayers(int count){ // retrieves top x players based on score
        List<LeaderboardEntry> copy = new ArrayList<>(); // arraylist of lb entries

        for(LeaderboardEntry entry : tempDatabase.values()){
            copy.add(new LeaderboardEntry(0,entry.getPlayerId(), entry.getPlayerName(), entry.getScore()));
            // for our copy we only care about ID and score
        }

        copy.sort((a,b) -> Integer.compare(b.getScore(), a.getScore()));
        // sort highest to lowest

        if(count > copy.size()){
            return copy; // in the case they request more players than are in the list
        }

        return copy.subList(0, count); // return x amount of players
    }

    /**
     * Helper function for printing out lists of lb entries
     * Should be used in conjunction with getTopPlayers
     */
    public void printTopPlayers(List<LeaderboardEntry> printList){
        for(LeaderboardEntry entry : printList){
            System.out.println("Score: " + entry.getScore() + " Player: " + entry.getPlayerName() + " UID:" + entry.getPlayerId());
        }
    }

}
