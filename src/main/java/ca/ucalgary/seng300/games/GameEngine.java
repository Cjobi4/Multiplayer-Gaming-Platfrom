package main.java.ca.ucalgary.seng300.games;

/**
 * Game Engine Interface
 * <p>
 *     Interface desc. here...
 * </p>
 * @author Jonathan Hooi
 * @email jonathan.hooi@ucalgary.ca
 * @version 0.1 03/19/2026
 * @implNote Construction of this Class made with respect to Game and Game Engine Diagram
 * from Project Iteration 1
 */
public interface GameEngine {
    public Object createInitialState(Player[] players);
    public boolean validateMove(GameState stateMove, Move move);
    public Object applyMove(GameState stateMove, Move move);
    public boolean evaluateEnd(GameState state);
}
