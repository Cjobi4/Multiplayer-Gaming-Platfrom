package ca.ucalgary.seng300.games.tictactoe;

import ca.ucalgary.seng300.core.identity.client.Session;

//this class represents the server-side tic tac toe game session (extending thread)
public class TicTacToeGameSession extends Thread{

    //this stores the session for player one
    private Session playerOneSession;

    //this stores the session for player two
    private Session playerTwoSession;

    //this stores the tic tac toe game logic that the session is using
    private TicTacToeGame game;

    //this constructor will create a new serverside tic tac toe game session
    public TicTacToeGameSession(Session playerOneSession, Session playerTwoSession){

        //assign the first players session
        this.playerOneSession = playerOneSession;

        //assign the second players session
        this.playerTwoSession = playerTwoSession;

        //create a new game object for a specific match between two players
        this.game = new TicTacToeGame();
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
        return game;
    }


    //not complete, will be finished in later ticket
    @Override
    public void run(){
        //placeholder function for rn will implement in a later ticket
    }
}
