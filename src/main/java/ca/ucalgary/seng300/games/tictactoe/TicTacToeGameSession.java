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

        return game;
    }

    public void sendBoardState() {
        String boardState = game.getBoard().toString();
        //TODO add a real i.e > playerOneSession.addRequest(12, new String[]{boardState}); communication aspect
        System.out.println("send the board state to both players!!!: " + boardState);

    }

    public void sendCurrentTurn() {
        char getCurrentPlayer = game.getCurrentPlayer();
        //TODO add a real i.e > playerOneSession.addRequest(12, new String[]{boardState}); communication aspect
        System.out.println("send the current turn to both players!!!: " + getCurrentPlayer);
    }


    public void sendGameState() throws Exception {
        String state = game.getGameState().name();

    }

    //not complete, will be finished in later ticket
    @Override
    public void run(){
        //placeholder function for rn will implement in a later ticket
    }
}
