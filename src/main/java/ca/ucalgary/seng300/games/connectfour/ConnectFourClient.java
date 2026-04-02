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

public class ConnectFourClient extends Thread {
    private char[][] board;
    private Session playerOne;
    private Session playerTwo;
    private boolean gameIsntOver = true;
    private Session winner = null;

    /**
     * Constructor to initialize the game with two players and a 6x7 board.
     * @param p1 Session for Player 1 (X)
     * @param p2 Session for Player 2 (O)
     */
    public ConnectFourClient(Session p1, Session p2) {
        this.playerOne = p1;
        this.playerTwo = p2;
        this.board = new char[6][7];

        // Initialize the board with empty cells
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                board[r][c] = '.';
            }
        }
    }

    /**
     * The main game loop that executes in its own thread.
     */
    @Override
    public void run() {
        String[] boardCopy = {toString()};

        try {
            // Initial board sync
            playerOne.addRequest(12, boardCopy);
            playerTwo.addRequest(12, boardCopy);
        } catch (Exception e) {
            throw new RuntimeException("Initial connection failed: " + e);
        }

        Request req;

        while (gameIsntOver) {
            // --- PLAYER 1 TURN ---
            handleTurn(playerOne, "P1's turn now", 'X', boardCopy);
            if (!gameIsntOver) break;

            // --- PLAYER 2 TURN ---
            handleTurn(playerTwo, "P2's turn now", 'O', boardCopy);
        }

        // --- GAME END LOGIC ---
        handleGameOver();
    }

    /**
     * Helper to manage a single player's turn cycle.
     */
    private void handleTurn(Session player, String message, char token, String[] boardCopy) {
        try {
            // Notify turn
            Request req = new Request(13, new String[]{message});
            player.addRequest(req);

            // Get move from client (expecting column index as string)
            String moveResult = req.getResult();
            int col = Integer.parseInt(moveResult);

            // Apply move logic
            if (dropPiece(col, token)) {
                boardCopy[0] = toString();
                playerOne.addRequest(12, boardCopy);
                playerTwo.addRequest(12, boardCopy);

                if (checkWin(token)) {
                    winner = player;
                    gameIsntOver = false;
                } else if (isFull()) {
                    gameIsntOver = false;
                }
            }
        } catch (Exception e) {
            // Consider forfeit logic here if message fails
            gameIsntOver = false;
        }
    }

    private void handleGameOver() {
        if (winner != null) {
            winner.addRequest(14, new String[]{"You won!"});
            Session loser = (winner == playerOne) ? playerTwo : playerOne;
            loser.addRequest(14, new String[]{"You lost!"});
        } else {
            playerOne.addRequest(14, new String[]{"It's a draw!"});
            playerTwo.addRequest(14, new String[]{"It's a draw!"});
        }

        // Database Update (Matches your screenshot)
        String winnerName = (winner != null) ? winner.getUsername() : "Draw";
        String currentDate = new java.util.Date().toString();

        // Assuming Database.addMatchResult(p1, p2, winner, date, gameType)
        Database.addMatchResult(
                playerOne.getUsername(),
                playerTwo.getUsername(),
                winnerName,
                currentDate,
                "c4"
        );
    }

    // --- GAME LOGIC METHODS ---

    public boolean dropPiece(int col, char token) {
        if (col < 0 || col >= 7) return false;
        for (int r = 5; r >= 0; r--) {
            if (board[r][col] == '.') {
                board[r][col] = token;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks all possible win conditions for a specific player.
     * @param p The player character to check ('X' or 'O').
     * @return true if the player has four in a row.
     */
    public boolean checkWin(char p) {
        // 1. Horizontal Check
        // We check every row, but only columns 0-3 (since 4, 5, 6 can't start a 4-length win)
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == p && board[r][c+1] == p &&
                        board[r][c+2] == p && board[r][c+3] == p) {
                    return true;
                }
            }
        }

        // 2. Vertical Check
        // We check every column, but only rows 0-2
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 7; c++) {
                if (board[r][c] == p && board[r+1][c] == p &&
                        board[r+2][c] == p && board[r+3][c] == p) {
                    return true;
                }
            }
        }

        // 3. Diagonal Check (Down-Right: \ )
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == p && board[r+1][c+1] == p &&
                        board[r+2][c+2] == p && board[r+3][c+3] == p) {
                    return true;
                }
            }
        }

        // 4. Diagonal Check (Up-Right: / )
        for (int r = 3; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == p && board[r-1][c+1] == p &&
                        board[r-2][c+2] == p && board[r-3][c+3] == p) {
                    return true;
                }
            }
        }

        return false; // No win detected
    }

    public boolean isFull() {
        for (int c = 0; c < 7; c++) if (board[0][c] == '.') return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                sb.append(board[r][c]).append(c == 6 ? "" : " ");
            }
            if (r < 5) sb.append("\n");
        }
        return sb.toString();
    }
}