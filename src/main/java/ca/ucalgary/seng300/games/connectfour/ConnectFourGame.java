package ca.ucalgary.seng300.games.connectfour;

import ca.ucalgary.seng300.games.GameState;

/**
 * Manages the state and rules of a Connect Four game.
 * Coordinates turns, win-checking, and draw conditions.
 * 
 * @author Hoang Khoi Nguyen
 * @email hoangkhoi.nguyen@ucalgary.ca
 * @version 3.0 04/02/2026
 */
public class ConnectFourGame {
    private ConnectFourBoard board;
    private char currentPlayer;
    private char winner;
    private GameState gameState;
    private int moveCount;

    /**
     * Initializes a new Connect Four game in the INITIALIZING state.
     */
    public ConnectFourGame() {
        board = new ConnectFourBoard();
        currentPlayer = 'X';
        winner = ' ';
        moveCount = 0;
        gameState = GameState.GAME_INITIALIZING;
    }

    /**
     * Processes a move from a specific user.
     * 
     * @param col              The column index (0-6).
     * @param userGameIdentity The character ('X' or 'O') of the moving user.
     * @return true if the move was valid and applied; false otherwise.
     */
    public boolean makeMove(int col, char userGameIdentity) {
        // Validation logic
        if (gameState == GameState.PLAYER_WIN || gameState == GameState.PLAYER_DRAW)
            return false;
        if (userGameIdentity != currentPlayer)
            return false;

        gameState = GameState.TURN_VALIDATING_MOVE;
        if (col < 0 || col >= board.getCols() || board.isColumnFull(col))
            return false;

        // Apply move
        gameState = GameState.TURN_APPLY_MOVE;

        if (board.dropPiece(col, userGameIdentity)) {
            moveCount++;
            gameState = GameState.TURN_CHECK_END_CONDITIONS;

            if (validateWin(userGameIdentity)) {
                winner = userGameIdentity;
                gameState = GameState.PLAYER_WIN;
                return true;
            }

            if (board.isFull()) {
                gameState = GameState.PLAYER_DRAW;
                return true;
            }

            // Switch turn
            gameState = GameState.TURN_DETERMINE_ACTIVE_PLAYER;
            switchTurn();
            gameState = GameState.TURN_AWAITING_MOVE;
            return true;
        }
        return false;
    }

    /**
     * Alternates the turn between player 'X' and player 'O'.
     */
    public void switchTurn() {
        // If the player currently is X,
        if (currentPlayer == 'X') {
            // make the current player O
            currentPlayer = 'O';

            // If the player currently is O
        } else {
            // make the current player X
            currentPlayer = 'X';
        }
    }

    /**
     * Convenience alias for switchTurn, retained for backward compatibility with
     * tests.
     */
    public void switchPlayer() {
        switchTurn();
    }

    /**
     * Processes a move for the current player.
     * Delegates to makeMove(int, char) using the internally tracked currentPlayer.
     * 
     * @param col The column index (0-6).
     * @return true if the move was valid and applied; false otherwise.
     */
    public boolean makeMove(int col) {
        return makeMove(col, currentPlayer);
    }

    /**
     * Checks if either player has achieved four-in-a-row.
     * 
     * @return true if a win is detected for either player.
     */
    public boolean checkWin() {
        return validateWin('X') || validateWin('O');
    }

    /**
     * Checks if a player has achieved four-in-a-row.
     * 
     * @param p The player character to check.
     * @return true if a win is detected.
     */
    public boolean validateWin(char p) {
        return checkHorizontal(p) || checkVertical(p) || checkDiagonal(p);
    }

    /** @return true if 4 in a row horizontally. */
    private boolean checkHorizontal(char p) {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols() - 3; c++) {
                if (board.getCell(r, c) == p && board.getCell(r, c + 1) == p &&
                        board.getCell(r, c + 2) == p && board.getCell(r, c + 3) == p)
                    return true;
            }
        }
        return false;
    }

    /** @return true if 4 in a row vertically. */
    private boolean checkVertical(char p) {
        for (int c = 0; c < board.getCols(); c++) {
            for (int r = 0; r < board.getRows() - 3; r++) {
                if (board.getCell(r, c) == p && board.getCell(r + 1, c) == p &&
                        board.getCell(r + 2, c) == p && board.getCell(r + 3, c) == p)
                    return true;
            }
        }
        return false;
    }

    /** @return true if 4 in a row diagonally. */
    private boolean checkDiagonal(char p) {
        // Down-Right
        for (int r = 0; r < board.getRows() - 3; r++) {
            for (int c = 0; c < board.getCols() - 3; c++) {
                if (board.getCell(r, c) == p && board.getCell(r + 1, c + 1) == p &&
                        board.getCell(r + 2, c + 2) == p && board.getCell(r + 3, c + 3) == p)
                    return true;
            }
        }
        // Up-Right
        for (int r = 3; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols() - 3; c++) {
                if (board.getCell(r, c) == p && board.getCell(r - 1, c + 1) == p &&
                        board.getCell(r - 2, c + 2) == p && board.getCell(r - 3, c + 3) == p)
                    return true;
            }
        }
        return false;
    }

    // Getters
    public ConnectFourBoard getBoard() {
        return board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char getWinner() {
        return winner;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getMoveCount() {
        return moveCount;
    }
}