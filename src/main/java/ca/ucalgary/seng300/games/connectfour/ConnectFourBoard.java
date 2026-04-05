package ca.ucalgary.seng300.games.connectfour;

/**
 * Represents the physical game board for Connect Four.
 * Handles the storage of pieces and logic for dropping pieces into columns.
 * @author Hoang Khoi Nguyen
 * @email hoangkhoi.nguyen@ucalgary.ca
 * @version 2.0 04/01/2026
 */
public class ConnectFourBoard {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private char[][] grid;

    /**
     * Constructs a new ConnectFourBoard and initializes it with empty cells.
     */
    public ConnectFourBoard() {
        grid = new char[ROWS][COLS];
        initialize();
    }

    /**
     * Fills the board grid with the '.' character to represent empty spaces.
     */
    private void initialize() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = '.';
            }
        }
    }

    /**
     * Attempts to drop a player's piece into a specific column.
     * The piece will occupy the lowest available row in that column.
     * * @param col    The index of the column (0-indexed).
     * @param player The character representing the player ('X' or 'O').
     * @return {@code true} if the piece was successfully dropped;
     * {@code false} if the column is full or out of bounds.
     */
    public boolean dropPiece(int col, char player) {
        if (col < 0 || col >= COLS) return false;

        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][col] == '.') {
                grid[row][col] = player;
                return true;
            }
        }
        return false; // column full
    }

    /**
     * Retrieves the character at a specific grid coordinate.
     * * @param row The row index.
     * @param col The column index.
     * @return The character at the specified location.
     */
    public char getCell(int row, int col) {
        return grid[row][col];
    }

    /** @return The number of rows on the board. */
    public int getRows() {
        return ROWS;
    }

    /** @return The number of columns on the board. */
    public int getCols() {
        return COLS;
    }

    /**
     * Checks if the board is completely full of pieces.
     * * @return {@code true} if no empty spaces remain; {@code false} otherwise.
     */
    public boolean isFull() {
        for (int col = 0; col < COLS; col++) {
            if (grid[0][col] == '.') return false;
        }
        return true;
    }

    /**
     * Prints the current state of the board to the standard output.
     */
    public void printBoard() {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6");
    }
}

