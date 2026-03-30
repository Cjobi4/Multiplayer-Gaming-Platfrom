package ca.ucalgary.seng300.games.connectfour;

public class connectFourGame {

    private connectFourBoard board;
    private char currentPlayer;

    public void ConnectFourGame() {
        board = new connectFourBoard();
        currentPlayer = 'X';
    }

    public boolean makeMove(int col) {
        return board.dropPiece(col, currentPlayer);
    }

    public boolean checkWin() {
        return checkHorizontal() || checkVertical() || checkDiagonal();
    }

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

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public boolean isDraw() {
        return board.isFull();
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void printBoard() {
        board.printBoard();
    }
}