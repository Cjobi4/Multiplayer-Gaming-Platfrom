package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.Request;
import ca.ucalgary.seng300.Session;
import ca.ucalgary.seng300.Database;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class ConnectFourGameSession extends Thread {

    private Session playerOne;
    private Session playerTwo;
    private ConnectFourGame game;
    private boolean isRunning = true;

    private static final int REQUEST_BOARD_UPDATE = 18;
    private static final int REQUEST_MOVE_PROMPT = 19;
    private static final int REQUEST_GAME_STATE = 20;

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
            // send initial game info
            sendBoardState();
            sendGameState();

            while (isRunning) {

                // determine active player
                Session activeSession = getCurrentPlayerSession();
                char activeToken = game.getCurrentPlayer();

                // prompt only the active player for a move
                Request turnReq = new Request(
                        REQUEST_MOVE_PROMPT,
                        new String[]{"Your Turn", String.valueOf(activeToken)}
                );

                activeSession.addRequest(turnReq);

                // wait for move from client
                String moveResult = turnReq.getResult();

                if (moveResult == null || moveResult.isBlank()) {
                    continue;
                }

                try {
                    int col = Integer.parseInt(moveResult.trim());

                    if (game.makeMove(col, activeToken)) {
                        // after every successful move, update both players
                        sendBoardState();
                        sendGameState();

                        // if game ended, stop loop
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
            e.printStackTrace();
        } finally {
            sendGameResult();
            Thread.currentThread().interrupt();
        }
    }

    public void sendBoardState() {
        try {
            String boardState = game.getBoard().toString();
            playerOne.addRequest(REQUEST_BOARD_UPDATE, new String[]{boardState});
            playerTwo.addRequest(REQUEST_BOARD_UPDATE, new String[]{boardState});
        } catch (Exception e) {
            System.err.println("Sync Error: " + e.getMessage());
        }
    }

    public void sendGameState() {
        try {
            String state = game.getGameState().name();
            playerOne.addRequest(REQUEST_GAME_STATE, new String[]{state});
            playerTwo.addRequest(REQUEST_GAME_STATE, new String[]{state});
        } catch (Exception e) {
            System.err.println("Game State Error: " + e.getMessage());
        }
    }

    public void sendGameResult() {
        try {
            String winnerUsername = null;

            //if 1 of the players won...
            if (game.getGameState() == GameState.PLAYER_WIN) {
                Session winner = (game.getWinner() == 'X') ? playerOne : playerTwo;
                winnerUsername = winner.getUsername();

                //send the results of the game to the players
                if (game.getWinner() == 'X')        //if player 1 won
                {
                    playerOne.addRequest(REQUEST_GAME_STATE, new String[]{GameState.PLAYER_WIN.name()});
                    playerTwo.addRequest(REQUEST_GAME_STATE, new String[]{GameState.PLAYER_LOSE.name()});
                }else       //if player 2 won
                {
                    playerOne.addRequest(REQUEST_GAME_STATE, new String[]{GameState.PLAYER_LOSE.name()});
                    playerTwo.addRequest(REQUEST_GAME_STATE, new String[]{GameState.PLAYER_WIN.name()});
                }
            } else if (game.getGameState() == GameState.PLAYER_DRAW)    //if it was a draw...
            {
                playerOne.addRequest(REQUEST_GAME_STATE, new String[]{"Game Is A Draw"});
                playerTwo.addRequest(REQUEST_GAME_STATE, new String[]{"Game Is A Draw"});
            }

            //hold the date in a string variable
            ZoneId zoneId = ZoneId.of("America/Edmonton");
            ZonedDateTime now = ZonedDateTime.now(zoneId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = now.format(formatter);

            //update the leaderboard and match record
            Database.addMatchResult(
                    playerOne.getUsername(),
                    playerTwo.getUsername(),
                    winnerUsername,
                    date,
                    "c4"
            );
        } catch (Exception e) {
            System.err.println("Reporting Error: " + e.getMessage());
        }
    }
}