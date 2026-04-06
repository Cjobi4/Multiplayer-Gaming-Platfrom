package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.GameState;

import ca.ucalgary.seng300.games.tictactoe.TicTacToeGame;

//this class represents the server-side tic tac toe game session (extending thread)
public class TTTServerSession extends Thread{

    //request type constants (temporary until confirmed with platform core)
    //TEMPORARY TILL HELP
    private static final int REQUEST_BOARD_UPDATE = 12;
    private static final int REQUEST_MOVE_PROMPT = 13;
    private static final int REQUEST_GAME_STATE = 14;

    //this stores the session for player one
    private Session playerOneSession;

    //this stores the session for player two
    private Session playerTwoSession;

    //this stores the tic tac toe game logic that the session is using
    private TicTacToeGame game;

    //bool tracker for session activity (still active or not()
    private boolean activeSession;

    //this constructor will create a new serverside tic tac toe game session
    public TicTacToeGameSession(Session player1, Session player2){

        //assign the first players session
        playerOneSession = player1;

        //assign the second players session
        playerTwoSession = player2;

        //create a new tic tac toe game object for a specific match between two players
        game = new TicTacToeGame();

        //set the session to active when the match begins
        activeSession = true;
    }

    //this is my getter for returning the first players session
    public Session getPlayerOneSession(){

        //return the first players session
        return playerOneSession;
    }

    //this is my getter for returning the second players session
    public Session getPlayerTwoSession(){

        //return the second players session
        return playerTwoSession;
    }

    //this is my getter for returning the specific game object being used in a match
    public TicTacToeGame getGame(){

        //return the game
        return game;
    }

    //this is my getter for returning session activity
    public boolean isActiveSession() {

        //return the session activity
        return activeSession;
    }

    //function for sending the board state
    public void sendBoardState() throws Exception {

        //get the board state in a string
        String boardState = game.getBoard().toString();

        //send the board state to player one
        playerOneSession.addRequest(REQUEST_BOARD_UPDATE, new String[]{boardState});

        //send the board state to player two
        playerTwoSession.addRequest(REQUEST_BOARD_UPDATE, new String[]{boardState});
    }

    //function for sending the current turn
    public void sendCurrentTurn() throws Exception {

        //get the current turn in a char
        char getCurrentPlayer = game.getCurrentPlayer();

        //send the current turn to player one
        playerOneSession.addRequest(REQUEST_MOVE_PROMPT, new String[]{"Current turn: " + getCurrentPlayer});

        //send the current turn to player two
        playerTwoSession.addRequest(REQUEST_MOVE_PROMPT, new String[]{"Current turn: " + getCurrentPlayer});
    }

    //function for sending the game state
    public void sendGameState() throws Exception {

        //get the current game state
        String state = game.getGameState().name();

        //send the game state to player one
        playerOneSession.addRequest(REQUEST_GAME_STATE, new String[]{state});

        //send the game state to player two
        playerTwoSession.addRequest(REQUEST_GAME_STATE, new String[]{state});
    }

    //function for sending the game result
    public void sendGameResult() throws Exception {

        //if a player has won, send a win message
        if (game.getGameState() == GameState.PLAYER_WIN) {

            //send the winner to player one
            playerOneSession.addRequest(REQUEST_GAME_STATE, new String[]{"The Winner Is: " + game.getWinner()});

            //send the winner to player two
            playerTwoSession.addRequest(REQUEST_GAME_STATE, new String[]{"The Winner Is: " + game.getWinner()});

            //if a player has tied send a tie message
        } else if (game.getGameState() == GameState.PLAYER_DRAW) {

            //send the draw to player one
            playerOneSession.addRequest(REQUEST_GAME_STATE, new String[]{"Game Is A Draw"});

            //send the draw to player two
            playerTwoSession.addRequest(REQUEST_GAME_STATE, new String[]{"Game Is A Draw"});
        }
    }

    //NEW FUNCTION
    //helper function for sending updates of the game
    public void sendGameUpdate() throws Exception {

        //send the current board state
        sendBoardState();

        //send the current turn
        sendCurrentTurn();

        //send the current game state
        sendGameState();
    }

    //this is my function for handling the situation when the game is over
    public void gameOverHandler() throws Exception {

        //send the game results out
        sendGameResult();

        //set the active session to false
        activeSession = false;

        //interrupt the current thread so the game session will end
        Thread.currentThread().interrupt();
    }

    //this function gets the active players session based on whose turn it is
    public Session getActivePlayerSession() {

        //if it is X's turn, return player one
        if (game.getCurrentPlayer() == 'X') {
            return playerOneSession;
        }

        //otherwise return player two
        return playerTwoSession;
    }

    //this function parses a move string into row and column values
    public int[] parseMove(String moveString) {

        //remove leading/trailing spaces from the move string
        String cleanedMove = moveString.trim();

        //split the move string using comma, ^, or spaces
        String[] moveParts = cleanedMove.split("[,\\^\\s]+");

        //make sure there are at least two values
        if (moveParts.length < 2) {
            throw new IllegalArgumentException("Invalid move from client!!!!!!!!!!");
        }

        //convert the first value into the row
        int row = Integer.parseInt(moveParts[0]);

        //convert the second value into the column
        int col = Integer.parseInt(moveParts[1]);

        //return both values in an int array
        return new int[]{row, col};
    }

    //this function is for processing a move that comes from the server/session flow
    public boolean moveProcessor(int row, int col, char playerSymbol) throws Exception {

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

    //placeholder function to represent where incoming move requests from platform core team are handeled
    public void recieveMoveHandler() throws Exception {

        //get the session of the player whose turn it currently is
        Session activePlayerSession = getActivePlayerSession();

        //ask the active player for their move and wait for the result
        String moveString = activePlayerSession.addRequestAndWait(REQUEST_MOVE_PROMPT, new String[]{"Your Turn", String.valueOf(game.getCurrentPlayer())});

        //if no move came back, do nothing this loop
        if (moveString == null || moveString.isBlank()) {
            return;
        }

        //parse the move string into row and column
        int[] parsedMove = parseMove(moveString);

        //process the move using the current active player symbol
        moveProcessor(parsedMove[0], parsedMove[1], game.getCurrentPlayer());
    }

    @Override
    public void run() {
        try{
            //send the initial board state to both players
            sendBoardState();

            //send the initial turn to both players
            sendCurrentTurn();

            //send the initial game state to both players
            sendGameState();

            //loop that runs until game is over
            while (!Thread.currentThread().isInterrupted() && activeSession) {

                //handle the incoming move from the active player
                recieveMoveHandler();

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

        catch (Exception exception) {
            //print the exception for debugging
            exception.printStackTrace();

            //interrupt the current thread to cleanly end session
            Thread.currentThread().interrupt();
        }
    }
}