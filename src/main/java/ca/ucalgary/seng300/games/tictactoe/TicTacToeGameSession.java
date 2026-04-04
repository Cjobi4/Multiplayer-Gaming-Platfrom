package ca.ucalgary.seng300.games.tictactoe;


import ca.ucalgary.seng300.core.identity.client.Session;
import ca.ucalgary.seng300.games.GameState;

//this class represents the server-side tic tac toe game session (extending thread)
public class TicTacToeGameSession extends Thread{

    //TODO replace OBJECT with the real session class once platform core integration is done (weikai)
    //this stores the session for player one
    private Object playerOneSession;

    //TODO replace OBJECT with the real session class once platform core integration is done (weikai)
    //this stores the session for player two
    private Object playerTwoSession;

    //this stores the tic tac toe game logic that the session is using
    private TicTacToeGame game;

    //bool tracker for session activity (still active or not()
    private boolean activeSession;

    //this constructor will create a new serverside tic tac toe game session
    public TicTacToeGameSession(Object player1, Object player2){

        //assign the first players session
        playerOneSession = player1;

        //assign the second players session
        playerTwoSession = player2;

        //create a new tic tac toe game object for a specific match between two players
        game = new TicTacToeGame();

    }

    //this is my getter for returning the first players session
    public Object getPlayerOneSession(){

        //return the first players session
        return playerOneSession;
    }

    //this is my getter for returning the second players session
    public Object getPlayerTwoSession(){

        //return the second players session
        return playerTwoSession;
    }

    //this is my getter for returning the specific game object being used in a match
    public TicTacToeGame getGame(){

        //return the game
        return game;
    }

    //this is my getter for returning session activity
    private boolean isActiveSession() {

        //return the session activity
        return activeSession;
    }

    //function for sending the board state
    public void sendBoardState() {

        //get the board state in a string
        String boardState = game.getBoard().toString();
        //TODO add a real i.e > playerOneSession.addRequest(12, new String[]{boardState}); communication aspect
        System.out.println("send the board state to both players!!!: " + boardState);

    }
    //function for sending the current turn
    public void sendCurrentTurn() {
        //get the current turn in a char
        char getCurrentPlayer = game.getCurrentPlayer();
        //TODO add a real i.e > playerOneSession.addRequest(12, new String[]{boardState}); communication aspect
        System.out.println("send the current turn to both players!!!: " + getCurrentPlayer);
    }

    //function for sending the game state
    public void sendGameState() {
        //get the current game state
        String state = game.getGameState().name();
        //TODO add a real i.e > playerOneSession.addRequest(12, new String[]{boardState}); communication aspect
        System.out.println("send the game state to both players!!!: " + state);
    }

    //function for sending the game result
    public void sendGameResult() {
        //if a player has won, send a win message
        if (game.getGameState() == GameState.PLAYER_WIN) {
            //TODO add a real i.e > playerOneSession.addRequest(12, new String[]{boardState}); communication aspect
            System.out.println("send the winner to both players!!!: " + game.getWinner());

        //if a player has tied send a tie message
        } else if (game.getGameState() == GameState.PLAYER_DRAW) {
            System.out.println("send the draw to both players!!!: " + game.getWinner());
        }
    }

    //NEW FUNCTION
    //helper function for sending updates of the game
    public void sendGameUpdate() {

        //send the current board state
        sendBoardState();

        //send the current turn
        sendCurrentTurn();

        //send the current game state
        sendGameState();
    }

    //this is my function for handling the situation when the game is over
    public void gameOverHandler() {

        //send the game results out
        sendGameResult();

        //set the active session to false
        activeSession = false;

        //interrupt the current thread so the game session will end
        Thread.currentThread().interrupt();
    }

    //TODO PLATFORM TEAM DATABASE HELP ME
    //this function is for processing a move that comes from the db
    //this can be called once incoming move parsing is finished (next function)
    public boolean moveProcessor(int row, int col, char playerSymbol) {

        //valiation to make sure a session is active
        if (!activeSession) {

            //if the session is inactive, return false immediatley
            return false;
        }

        //since the session is active,
        //apply the move using the tic tac toe backend logic (makeMove() function!)
        boolean moveSuccessCheck = game.makeMove(row, col, playerSymbol);

        //if the move failed return false
        if (!moveSuccessCheck) {

            //return false if the move has faield
            return false;
        }

        //since the move was a success,
        //send a game update to both players
        sendGameUpdate();

        //if the game ends (so a win or a tie/draw)
        if (game.getGameState() == GameState.PLAYER_WIN || game.getGameState() == GameState.PLAYER_DRAW) {

            //handle game over situations
            gameOverHandler();
        }

        //return true bc move has been completed/processed successfully
        return true;
    }

    //not complete, will be finished in later ticket
    @Override
    public void run() {
        try{
            //send the initial board state to both players
            sendBoardState();

            //send the initial turn to both players
            sendCurrentTurn();

            //send the initial game state to both players
            sendGameState();

            //store the previous board state so we can compare/detect changes later
            String oldBoardState = game.getBoard().toString();

            //store the previous game state so we can compare/detect changes later
            GameState oldGameState = game.getGameState();

            //loop that runs until game is over
            while (!Thread.currentThread().isInterrupted()) {

                //get the current board state
                String currentBoardState = game.getBoard().toString();

                //get the current game state
                GameState currentGameState = game.getGameState();

                //if there has been a change in the board state since the last checl/turn
                if (!currentBoardState.equals(oldBoardState)) {

                    //send the updated board state
                    sendBoardState();

                    //send the update turn
                    sendCurrentTurn();

                    //set the old board state to the current one (for future use)
                    oldBoardState = currentBoardState;
                }

                //if there has been a change in the game state since the last check/turn
                if (currentGameState != oldGameState) {

                    //send the new game state
                    sendGameState();

                    //set the old game state to the new one
                    oldGameState = currentGameState;
                }

                //if there has been a win or a draw
                if (currentGameState == GameState.PLAYER_WIN || oldGameState == GameState.PLAYER_DRAW) {

                    //send the game result to the players
                    sendGameResult();

                    //interrupt the current thread to cleanly end the game session
                    Thread.currentThread().interrupt();

                    //break the while loop
                    break;
                }
                //this is for pausing the loop a little so it does not run at full speed
                //learnt that here: https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
                Thread.sleep(100);
            }
        }

        catch (InterruptedException interruptedException) {
            //as per Weikai's request added this to close
            //interrupt the current thread to cleanly end session
            Thread.currentThread().interrupt();
        }
    }
}
