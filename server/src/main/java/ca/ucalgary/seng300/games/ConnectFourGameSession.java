package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.Request;
import ca.ucalgary.seng300.Session;
import ca.ucalgary.seng300.Database;
import ca.ucalgary.seng300.games.GameState;
import ca.ucalgary.seng300.games.ConnectFourGame;

import java.util.Date;

/**
 * The Client-side controller for Connect Four.
 * This class handles the communication between the UI and the Game Logic.
 * @author Hoang Khoi Nguyen
 * @email hoangkhoi.nguyen@ucalgary.ca
 * @version 4.0 04/02/2026
 */

public class ConnectFourGameSession extends Thread {

    private Session playerOne;
    private Session playerTwo;
    private ConnectFourGame game;
    private boolean isRunning = true;

    public ConnectFourGameSession(Session p1, Session p2) {
        this.playerOne = p1;
        this.playerTwo = p2;
        this.game = new ConnectFourGame();
    }

    private Session getCurrentPlayerSession() {
        return (game.getCurrentPlayer() == 'X') ? playerOne : playerTwo;
    }

    @Override
    public void run() {
        try {
            // Initializing Game Setup
            sendBoardState();

            while (isRunning && !Thread.currentThread().isInterrupted()) {

                // 1. Determine Active Player & Await Move
                Session activeSession = getCurrentPlayerSession();
                char activeToken = game.getCurrentPlayer();

                // Create the Request instance from the session
                Request turnReq = new Request(13, new String[]{"P" + (activeToken == 'X' ? "1" : "2") + "'s turn"});

                // Add the request to the session queue
                activeSession.addRequest(turnReq);

                // 2. Turn Awaiting Move: Block until move is received
                String moveResult = turnReq.getResult();

                try {
                    // 3. Turn Validating & Applying Move
                    int col = Integer.parseInt(moveResult);

                    if (game.makeMove(col, activeToken)) {
                        // Update board for both players (Type 12)
                        sendBoardState();

                        // 4. Turn Check End Conditions
                        if (game.getGameState() == GameState.PLAYER_WIN ||
                                game.getGameState() == GameState.PLAYER_DRAW) {
                            isRunning = false;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid move format: " + moveResult);
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Game Session Error: " + e.getMessage());
        } finally {
            sendGameResult();
            Thread.currentThread().interrupt();
        }
    }

    public void sendBoardState() {
        try {
            String boardState = game.getBoard().toString();
            playerOne.addRequest(12, new String[]{boardState});
            playerTwo.addRequest(12, new String[]{boardState});
        } catch (Exception e) {
            System.err.println("Sync Error: " + e.getMessage());
        }
    }

    public void sendGameResult() {
        try {
            int winnerID = 0;
            if (game.getGameState() == GameState.PLAYER_WIN) {
                Session winner = (game.getWinner() == 'X') ? playerOne : playerTwo;
                Session loser = (game.getWinner() == 'X') ? playerTwo : playerOne;
                winnerID = Integer.parseInt(winner.getUsername());

                // Type 14 Notifications
                winner.addRequest(14, new String[]{"You won!"});
                loser.addRequest(14, new String[]{"You lost!"});
            } else {
                playerOne.addRequest(14, new String[]{"Draw!"});
                playerTwo.addRequest(14, new String[]{"Draw!"});
            }

            // Adding the match data to the Database
            Database.addMatchResult(
                    playerOne.getUsername(),
                    playerTwo.getUsername(),
                    String.valueOf(winnerID),
                    new Date().toString(),
                    "c4"
            );
        } catch (Exception e) {
            System.err.println("Reporting Error: " + e.getMessage());
        }
    }
}