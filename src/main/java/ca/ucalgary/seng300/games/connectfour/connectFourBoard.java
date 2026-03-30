package ca.ucalgary.seng300.games.connectfour;

public class connectFourBoard {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private char[][] grid;

    public connectFourBoard() {
        grid = new char[ROWS][COLS];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = '.';
            }
        }
    }

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

    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public int getRows() {
        return ROWS;
    }

    public int getCols() {
        return COLS;
    }

    public boolean isFull() {
        for (int col = 0; col < COLS; col++) {
            if (grid[0][col] == '.') return false;
        }
        return true;
    }

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

