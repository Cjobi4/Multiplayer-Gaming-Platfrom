package ca.ucalgary.seng300.games.connectfour;

/**
 * Represents the physical 6x7 game board for Connect Four.
 * Handles piece storage, dropping logic, and state reporting for the GUI.
 * * @author Hoang Khoi Nguyen
 * 
 * @email hoangkhoi.nguyen@ucalgary.ca
 * @version 3.0 04/02/2026
 */
public class ConnectFourBoard {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private char[][] grid;

    /**
     * Constructs a new board and initializes it with empty cells ('.').
     */
    public ConnectFourBoard() {
        grid = new char[ROWS][COLS];
        initialize();
    }

    /**
     * Fills the board grid with '.' to represent empty spaces.
     */
    private void initialize() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = '.';
            }
        }
    }

    /**
     * Attempts to drop a piece into a column.
     * 
     * @param col    The index of the column (0-6).
     * @param player The character representing the player ('X' or 'O').
     * @return true if the piece was successfully dropped; false if column is full.
     */
    public boolean dropPiece(int col, char player) {
        if (col < 0 || col >= COLS)
            return false;

        // Check from bottom to top for the first empty spot
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][col] == '.') {
                grid[row][col] = player;
                return true;
            }
        }
        return false;
    }

    /**
     * Validation check for the Game class and GUI.
     * 
     * @param col The column index.
     * @return true if the top row of the column is already filled.
     */
    public boolean isColumnFull(int col) {
        if (col < 0 || col >= COLS)
            return true;
        return grid[0][col] != '.';
    }

    /**
     * Checks if the entire board is full (Draw condition).
     */
    public boolean isFull() {
        for (int col = 0; col < COLS; col++) {
            if (grid[0][col] == '.')
                return false;
        }
        return true;
    }

    /**
     * Retrieves the piece at a specific location for win-checking.
     */
    public char getCell(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return ' '; // Return space for out of bounds to avoid crashes
        }
        return grid[row][col];
    }

    /**
     * Sets a specific cell to a value.
     */
    public void setCell(int row, int col, char value) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            grid[row][col] = value;
        }
    }

    /**
     * Prints the current board state to standard output,
     * including column index labels along the bottom row.
     */
    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                sb.append(grid[i][j]).append(' ');
            }
            sb.append('\n');
        }
        for (int j = 0; j < COLS; j++) {
            if (j > 0) {
                sb.append(' ');
            }
            sb.append(j);
        }
        sb.append('\n');
        System.out.print(sb);
    }

    public int getRows() {
        return ROWS;
    }

    public int getCols() {
        return COLS;
    }

    /**
     * Converts the board to a single String for network transmission.
     * Used by the Server to send board updates to the Client.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                sb.append(grid[i][j]);
                if (j < COLS - 1)
                    sb.append(" ");
            }
            if (i < ROWS - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Reconstructs the 6x7 board from a single String.
     * Use this when loading a game from the Database or receiving a Server update.
     * 
     * @param gameBoardString The string representation of the board.
     */
    public void fromString(String gameBoardString) {
        String[] boardValues = gameBoardString.replace("\n", " ").split(" ");
        int temp = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (temp < boardValues.length) {
                    grid[r][c] = boardValues[temp].charAt(0);
                    temp++;
                }
            }
        }
    }
}