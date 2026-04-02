package ca.ucalgary.seng300.games.tictactoe;

import ca.ucalgary.seng300.core.identity.client.Session;

public class TicTacToeGameSession extends Thread{
    private Session playerOneSession;
    private Session playerTwoSession;
    private TicTacToeGame game;


    public TicTacToeGameSession(Session playerOneSession, Session playerTwoSession){
        this.playerOneSession = playerOneSession;
        this.playerTwoSession = playerTwoSession;
        this.game = new TicTacToeGame();
    }
    public Session getPlayerOneSession(){
        return playerOneSession;
    }

    public Session getPlayerTwoSession(){
        return playerTwoSession;
    }

    public TicTacToeGame getGame(){
        return game;
    }

    @Override
    public void run(){
        //placeholder function for rn will implement in a later ticket
    }
}
