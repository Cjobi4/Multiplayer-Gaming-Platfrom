/*
package ca.ucalgary.seng300.games.connectfour;

import ca.ucalgary.seng300.core.identity.client.Session;

*/
/**
 * The Client-side controller for Connect Four.
 * This class handles the communication between the UI and the Game Logic.
 * @author Hoang Khoi Nguyen
 * @email hoangkhoi.nguyen@ucalgary.ca
 * @version 3.0 04/02/2026
 *//*


public class ConnectFourGameSession extends Thread {

    private Session playerOne; // Player 'X'
    private Session playerTwo; // Player 'O'
    private ConnectFourGame game;
    private boolean isRunning = true;

    */
/**
     * Constructor for the server-side game session.
     * @param p1 The Session object for the first player.
     * @param p2 The Session object for the second player.
     *//*

    public ConnectFourGameSession(Session p1, Session p2) {
        this.playerOne = p1;
        this.playerTwo = p2;
        this.game = new ConnectFourGame(); // Initializes the board and rules
    }

    */
/**
     * Returns the Session of the player whose turn it currently is.
     * @return The active player's Session object.
     *//*

    public Session getCurrentPlayerSession() {
        return (game.getCurrentPlayer() == 'X') ? playerOne : playerTwo;
    }

    @Override
    public void run() {
        try {
            // 1. Initial Board Sync (Type 12) to start the match
            syncBoard();

            // 2. Main Game Loop
            while (isRunning && !Thread.currentThread().isInterrupted()) {

                Session activeSession = getCurrentPlayerSession();
                char activeToken = game.getCurrentPlayer();

                // Send Turn Notification (Type 13)
                // Using the Request inner class from your Session file
                Session.Request turnRequest = activeSession.new Request(13, new String[]{"Your turn, Player " + activeToken});
                activeSession.addRequest(13, turnRequest.getParameters());

                // Wait for the player's move via the Request's future result
                String moveResult = turnRequest.getResult();
                int col = Integer.parseInt(moveResult);

                // Authoritative Move Validation and Execution
                if (game.makeMove(col, activeToken)) {
                    syncBoard(); // Update both players after a successful move (Type 12)

                    // Check for terminal states
                    if (game.getGameState() == GameState.PLAYER_WIN ||
                            game.getGameState() == GameState.PLAYER_DRAW) {
                        isRunning = false;
                    }
                }

                // Polling delay consistent with TicTacToe session style
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Connect Four Session Error: " + e.getMessage());
        } finally {
            handleGameOver();
        }
    }

    */
/**
     * Sends the current board state string to both players (Type 12).
     *//*

    private void syncBoard() {
        String boardData = game.getBoard().toString();
        try {
            playerOne.addRequest(12, new String[]{boardData});
            playerTwo.addRequest(12, new String[]{boardData});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */
/**
     * Finalizes the match, notifies players, and records to the database.
     *//*

    private void handleGameOver() {
        try {
            String resultMsg = (game.getGameState() == GameState.PLAYER_DRAW) ?
                    "It's a draw!" : "Winner: " + game.getWinner();

            // Notify players of the result (Type 14)
            playerOne.addRequest(14, new String[]{resultMsg});
            playerTwo.addRequest(14, new String[]{resultMsg});

            // Determine the winner's ID for the database
            int winnerID = 0; // Default for draw
            if (game.getGameState() == GameState.PLAYER_WIN) {
                winnerID = (game.getWinner() == 'X') ? playerOne.getUserID() : playerTwo.getUserID();
            }

            // Persistence: Matches the matchRecord table in your Database.java
            // This would call a method like Database.addMatchResult with winnerID, p1ID, p2ID, and date
            System.out.println("Match recorded: P1(" + playerOne.getUserID() + ") vs P2(" +
                    playerTwo.getUserID() + "). WinnerID: " + winnerID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
