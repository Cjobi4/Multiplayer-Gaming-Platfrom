package ca.ucalgary.seng300.games.connectfour;

import ca.ucalgary.seng300.games.GameState;
import ca.ucalgary.seng300.core.identity.client.Session;

/**
 * The Client-side controller for Connect Four.
 * This class handles the communication between the UI and the Game Logic.
 * @author Hoang Khoi Nguyen
 * @email hoangkhoi.nguyen@ucalgary.ca
 * @version 3.0 04/02/2026
 */

public class ConnectFourClient extends Thread{
    private ConnectFourGame game;
    private char myToken; // 'X' or 'O'

    public ConnectFourClient(char token) {
        this.game = new ConnectFourGame();
        this.myToken = token;
    }

    /**
     * Called when a user clicks a column in the GUI.
     * @param column The column index (0-6).
     * @return A message string to display in the GUI status bar.
     */
    public String handleUserMove(int column) {
        // 1. Check if it's actually this client's turn
        if (game.getCurrentPlayer() != myToken) {
            return "Wait for your opponent's move!";
        }

        // 2. Attempt the move
        boolean success = game.makeMove(column, myToken);

        if (!success) {
            return "Invalid move: " + game.getGameState().getDescription();
        }

        // 3. Check for end-game states
        GameState state = game.getGameState();
        if (state == GameState.PLAYER_WIN) {
            return "Game Over: " + (game.getWinner() == myToken ? "You Won!" : "You Lost!");
        } else if (state == GameState.PLAYER_DRAW) {
            return "Game Over: It's a Draw!";
        }

        return "Move accepted. Waiting for opponent...";
    }

    /**
     * Used by the GUI to redraw the board.
     * @return The 6x7 grid as a 2D array.
     */
    public char[][] getBoardData() {
        ConnectFourBoard board = game.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        char[][] data = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                data[r][c] = board.getCell(r, c);
            }
        }
        return data;
    }

    public GameState getStatus() {
        return game.getGameState();
    }
}