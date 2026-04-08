package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.Request;
import ca.ucalgary.seng300.Session;
import ca.ucalgary.seng300.Database;

import java.util.Date;

/**
 * The server-side controller for a Tic-Tac-Toe game session between two players.
 * Manages the game loop, validates moves received from clients, broadcasts board
 * state updates to both players, and reports the final result to the database.
 *
 * Follows the same session lifecycle as {@link ConnectFourGameSession}:
 * send initial board state, prompt the active player for a move, validate and
 * apply the move, check end conditions, and repeat until the game terminates.
 */
public class TicTacToeGameSession extends Thread {

    private Session playerOne;
    private Session playerTwo;
    private TicTacToeGame game;
    private boolean isRunning = true;

    /**
     * Constructs a new TicTacToeGameSession for the two matched players.
     *
     * @param p1 the session for player one (plays as 'X')
     * @param p2 the session for player two (plays as 'O')
     */
    public TicTacToeGameSession(Session p1, Session p2) {
        this.playerOne = p1;
        this.playerTwo = p2;
        this.game = new TicTacToeGame();
    }

    /**
     * Returns the session of the player whose turn it currently is.
     *
     * @return the active player's session
     */
    private Session getCurrentPlayerSession() {
        return (game.getCurrentPlayer() == 'X') ? playerOne : playerTwo;
    }

    /**
     * Main game loop. Sends the initial board state, then repeatedly prompts
     * the active player for a move, validates it, applies it, and checks for
     * win or draw conditions. Runs until the game terminates or the thread is
     * interrupted.
     */
    @Override
    public void run() {
        try {
            sendBoardState();

            while (isRunning && !Thread.currentThread().isInterrupted()) {

                // Determine the active player and their token
                Session activeSession = getCurrentPlayerSession();
                char activeToken = game.getCurrentPlayer();

                // Create a move-prompt request (type 13) for the active player
                Request turnReq = new Request(13, new String[]{"P" + (activeToken == 'X' ? "1" : "2") + "'s turn"});

                // Add the request to the session queue
                activeSession.addRequest(turnReq);

                // Block until the client responds with a move
                String moveResult = turnReq.getResult();

                try {
                    // Parse the move: expected format is "row,col"
                    String[] parts = moveResult.split(",");
                    assert parts.length == 2 : "Move must contain exactly two values separated by a comma";

                    int row = Integer.parseInt(parts[0].trim());
                    int col = Integer.parseInt(parts[1].trim());

                    if (game.makeMove(row, col, activeToken)) {
                        // Broadcast updated board to both players (type 12)
                        sendBoardState();

                        // Check if the game has ended
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

    /**
     * Broadcasts the current board state to both players as a type 12 request.
     * The board is serialized via {@link TicTacToeBoard#toString()}.
     */
    public void sendBoardState() {
        try {
            String boardState = game.getBoard().toString();
            playerOne.addRequest(12, new String[]{boardState});
            playerTwo.addRequest(12, new String[]{boardState});
        } catch (Exception e) {
            System.err.println("Sync Error: " + e.getMessage());
        }
    }

    /**
     * Sends the final game result to both players as a type 14 request and
     * records the match outcome in the database.
     */
    public void sendGameResult() {
        try {
            int winnerID = 0;
            if (game.getGameState() == GameState.PLAYER_WIN) {
                Session winner = (game.getWinner() == 'X') ? playerOne : playerTwo;
                Session loser = (game.getWinner() == 'X') ? playerTwo : playerOne;
                winnerID = Integer.parseInt(winner.getUsername());

                // Type 14 notifications
                winner.addRequest(14, new String[]{"You won!"});
                loser.addRequest(14, new String[]{"You lost!"});
            } else {
                playerOne.addRequest(14, new String[]{"Draw!"});
                playerTwo.addRequest(14, new String[]{"Draw!"});
            }

            // Record the match in the database
            Database.addMatchResult(
                    playerOne.getUsername(),
                    playerTwo.getUsername(),
                    String.valueOf(winnerID),
                    new Date().toString(),
                    "ttt"
            );
        } catch (Exception e) {
            System.err.println("Reporting Error: " + e.getMessage());
        }
    }
}
