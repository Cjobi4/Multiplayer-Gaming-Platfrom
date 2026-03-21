package main.java.ca.ucalgary.seng300.games;

/**
 * Game Controller Class
 * <p>
 *     Controller Class for Games, handling game-respective movement placement calls, turn
 *     switching between (in)active players, and game termination logic.
 * </p>
 * @author Jonathan Hooi
 * @email jonathan.hooi@ucalgary.ca
 * @version 0.1.1 03/20/2026
 * @implNote Construction of this Class made with respect to Player Profiles Stats Persistence
 * Diagram from Project Iteration 1
 */
public class GameController implements GameEngine {
    private Player currentActivePlayer;
    private String turnOrder;

    //TODO: Constructor method stub
    GameController() {

    }

    //TODO: method stub
    public void makeMove() {

    }
    //TODO: method stub
    @Override
    public Object createInitialState(Player[] players) {
        return null;
    }
    //TODO: method stub
    @Override
    public boolean validateMove(GameState stateMove, Move move) {
        return false;
    }
    //TODO: method stub
    @Override
    public Object applyMove(GameState stateMove, Move move) {
        return null;
    }
    //TODO: method stub
    @Override
    public boolean evaluateEnd(GameState state) {
        return false;
    }
}
