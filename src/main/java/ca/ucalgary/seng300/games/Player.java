package ca.ucalgary.seng300.games;

/**
 * Player Class
 * <p>
 *     Class containing core player data modification/admin. Player stats and settings are
 *     handled by their respective helper classes.
 *     This class will be modified to be game-player interaction focused, with admin
 *     functionality moved to a different class.
 * </p>
 * @author Jonathan Hooi
 * @email jonathan.hooi@ucalgary.ca
 * @version 0.3, 03/19/2026
 * @implNote Construction of this Class made with respect to Player Profiles Stats Persistence
 * Diagram from Project Iteration 1
 */
public class Player {
    //TODO: figure out access modifiers
    //TODO: figure out if deviation from diagram preferable; currently all String fields
    private String playerID;
    private String username;

    /**
     * Boolean representing if the user being handled by a Player instance is currently logged
     * in or, if needed, was logged in within a buffer period prior.
     */
    private boolean isLoggedIn;

    //TODO: method stub
    public Player() {
        //TODO: figure out where each field will come from
    }
    //TODO: method stub; move to admin handling variant of this class
    void login() {

    }
    //TODO: method stub; move to admin handling variant of this class
    void logout() {

    }
    //TODO: method stub
    GeneralStats getStats(String gameType) {
        //TODO: basic logic here
        return null;  //TODO: TEMP null reference
    }
    //TODO: method stub; move to admin handling variant of this class
    void updateUsername(String oldUsername, String newUsername) {
        //TODO: figure out if any of these parameters are unnecessary
    }
    //TODO: method stub; move to admin handling variant of this class
    void updateEmail(String username, String email) {
        //TODO: figure out if any of these parameters are unnecessary
    }
    //TODO: method stub; move to admin handling variant of this class
    void updatePassword(String username, String password) {
        //TODO: figure out if any of these parameters are unnecessary
    }
}
