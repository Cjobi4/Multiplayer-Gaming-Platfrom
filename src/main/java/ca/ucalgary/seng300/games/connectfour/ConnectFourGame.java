package ca.ucalgary.seng300.games.connectfour;

/**
 * Manages the state and rules of a Connect Four game.
 * Coordinates turns, win-checking, and draw conditions.
 * @author Hoang Khoi Nguyen
 * @email hoangkhoi.nguyen@ucalgary.ca
 * @version 2.0 04/01/2026
 */
public class ConnectFourGame {
    private ConnectFourBoard board;
    private char currentPlayer;
    private boolean gameOver;

    /**
     * Initializes a new game with a clean board.
     * Player 'X' always starts first.
     */
    public ConnectFourGame() {
        board = new ConnectFourBoard();
        currentPlayer = 'X';
        gameOver = false;
    }

    /**
     * Executes a move for the current player in the specified column.
     * If the move results in a win or draw, the game state is updated to over.
     * * @param col The column index where the player wants to drop a piece.
     * @return {@code true} if the move was valid and executed; {@code false} otherwise.
     */
    public boolean makeMove(int col) {
        if (gameOver) return false;

        boolean success = board.dropPiece(col, currentPlayer);
        if (success) {
            if (checkWin()) {
                gameOver = true;
            } else if (isDraw()) {
                gameOver = true;
            } else {
                switchPlayer();
            }
        }
        return success;
    }

    /**
     * Checks if the current player has four pieces in a row.
     * * @return {@code true} if a horizontal, vertical, or diagonal line is found.
     */
    public boolean checkWin() {
        return checkHorizontal() || checkVertical() || checkDiagonal();
    }

    /** @return true if 4 in a row horizontally. */
    private boolean checkHorizontal() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols() - 3; col++) {
                char p = currentPlayer;
                if (board.getCell(row, col) == p &&
                        board.getCell(row, col + 1) == p &&
                        board.getCell(row, col + 2) == p &&
                        board.getCell(row, col + 3) == p) {
                    return true;
                }
            }
        }
        return false;
    }

    /** @return true if 4 in a row vertically. */
    private boolean checkVertical() {
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows() - 3; row++) {
                char p = currentPlayer;
                if (board.getCell(row, col) == p &&
                        board.getCell(row + 1, col) == p &&
                        board.getCell(row + 2, col) == p &&
                        board.getCell(row + 3, col) == p) {
                    return true;
                }
            }
        }
        return false;
    }

    /** @return true if 4 in a row diagonally (both directions). */
    private boolean checkDiagonal() {
        char p = currentPlayer;

        // bottom-left → top-right
        for (int row = 3; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols() - 3; col++) {
                if (board.getCell(row, col) == p &&
                        board.getCell(row - 1, col + 1) == p &&
                        board.getCell(row - 2, col + 2) == p &&
                        board.getCell(row - 3, col + 3) == p) {
                    return true;
                }
            }
        }

        // top-left → bottom-right
        for (int row = 0; row < board.getRows() - 3; row++) {
            for (int col = 0; col < board.getCols() - 3; col++) {
                if (board.getCell(row, col) == p &&
                        board.getCell(row + 1, col + 1) == p &&
                        board.getCell(row + 2, col + 2) == p &&
                        board.getCell(row + 3, col + 3) == p) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Alternates the turn between player 'X' and player 'O'.
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    /**
     * Checks if the game has ended in a draw (board is full with no winner).
     * * @return {@code true} if the board is full.
     */
    public boolean isDraw() {
        return board.isFull();
    }

    /** @return The character of the player whose turn it currently is. */
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    /** Prints the board state via the internal board instance. */
    public void printBoard() {
        board.printBoard();
    }
}