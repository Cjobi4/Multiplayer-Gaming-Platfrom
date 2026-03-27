package ca.ucalgary.seng300.games.connectfour;

public class connectFour {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private char[][] board;
    private char currentPlayer;

    public void ConnectFour() {
        board = new char[ROWS][COLS];
        currentPlayer = 'X'; // Player 1 = X, Player 2 = O
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = '.'; // empty cell
            }
        }
    }

    public boolean dropPiece(int col) {
        if (col < 0 || col >= COLS) return false;

        // place from bottom up
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == '.') {
                board[row][col] = currentPlayer;
                return true;
            }
        }
        return false; // column full
    }

    public boolean checkWin() {
        return checkHorizontal() || checkVertical() || checkDiagonal();
    }

    private boolean checkHorizontal() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == currentPlayer &&
                        board[row][col + 1] == currentPlayer &&
                        board[row][col + 2] == currentPlayer &&
                        board[row][col + 3] == currentPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVertical() {
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if (board[row][col] == currentPlayer &&
                        board[row + 1][col] == currentPlayer &&
                        board[row + 2][col] == currentPlayer &&
                        board[row + 3][col] == currentPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal() {
        // bottom-left to top-right
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == currentPlayer &&
                        board[row - 1][col + 1] == currentPlayer &&
                        board[row - 2][col + 2] == currentPlayer &&
                        board[row - 3][col + 3] == currentPlayer) {
                    return true;
                }
            }
        }

        // top-left to bottom-right
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == currentPlayer &&
                        board[row + 1][col + 1] == currentPlayer &&
                        board[row + 2][col + 2] == currentPlayer &&
                        board[row + 3][col + 3] == currentPlayer) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == '.') return false;
        }
        return true;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6"); // column indices
    }
}


