package ca.ucalgary.seng300.games.tictactoe;


//make smth to tell the player that it is their turn or the game is over or whatever that stuff
//make the client side part
//
//
//import the enums file
import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.games.GameState;

//this is for game/move logic for tic tac toe
public class TicTacToeGame {

    //this is the board we are going to use for the game
    private TicTacToeBoard board;

    //this variable helps keep track of what player's turn it is
    private char currentPlayer;

    //this variable stores the winner of the game (X, O, ' ')
    private char winner;

    //this stores the current state of the game using the GameState enum
    private GameState gameState;

    //This counts the total number of moves made within the game
    private int moveCount;

    private boolean myTurn;

    //this creates the game with a new board
    public TicTacToeGame(){
        //create a new gameboard
        board = new TicTacToeBoard();

        //set the first player to 'X' (X always goes first)
        currentPlayer = 'X';

        //set the value of the winner (string) to nothing because there is no winner at the start of the game
        winner = ' ';

        //set the start of the game to waiting for a player to make a move
        gameState = GameState.TURN_AWAITING_MOVE;

        //this sets the movecount to 0 as no moves by the start of the game
        moveCount = 0;

        myTurn = false;
    }

    public boolean getMyTurn()
    {
        return myTurn;
    }

    public void setTurn(boolean turn)
    {
        this.myTurn = turn;
    }

    //this returns the board that is being used during the game
    public TicTacToeBoard getBoard(){
        //return the current gameboard
        return board;
    }

    //this is my function for making a move and VALIDATING the move!
    //change to accept a session object
    //check the session object (git userID check it to compare who is playing, make sure a move request is from who (make sure who is playing))
    //
    public boolean makeMove(int row, int col) throws Exception {
        //adding gameState integration now (ticket 177)

        //if the game is over (win or tie), do not allow any more moves
        if (gameState == GameState.PLAYER_WIN || gameState == GameState.PLAYER_DRAW) {
            return false;
        }

        //update the gamestate to show that a move is being validated
        gameState = GameState.TURN_AWAITING_MOVE;

        //bounds check moved to first in the makeMove function
        //this checks/make sure the users position selected stays within the bounds of the game board
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            //GAMESTATE INFO
            gameState = GameState.TURN_AWAITING_MOVE;
            //if not return false
            return false;
        }

        //make sure that the cell the player is trying to get to is not already occupied
        if (!board.isCellEmpty(row, col)) {
            //GAMESTATE INFO
            gameState = GameState.TURN_AWAITING_MOVE;
            //return false if it is occupied
            return false;
        }

        //gamestate can now be set to taking a turn (making a move) post all validation checks
        gameState = GameState.TURN_AWAITING_MOVE;

        //after all validation checks
        //we can set the players data ('X' or 'O') onto the board at the specific row and column they want to be at
        // board.setCellInfo(row, col, userGameIdentity);

        //incrament the movecount by 1 after each turn
        moveCount++;

        //now check if the users move has ended the game or not (win tie)
        gameState = GameState.TURN_CHECK_END_CONDITIONS;

        //since hte game is still running, set the state back to waiting for the next move
        gameState = GameState.TURN_AWAITING_MOVE;

        Network.getInstance().queueRequest(Network.SEND_MOVE_TTT, new String[]{row + " " + col});
        System.out.println("Not users turn");


        //return true!
        return true;
    }


    //FOR SANMEET
    //this function converts the full tic tac toe game state into a string for database storage
    public String toDataString() {

        //this combines the board string, current player, winner, game state, and move count into one string
        return board.toString() + "|" + currentPlayer + "|" + winner + "|" + gameState.name() + "|" + moveCount;
    }

    //this function restores the full tic tac toe game state from a database string
    public void fromDataString(String gameDataString) {

        //split the full game data string using | as the separator
        String[] specificGameItem = gameDataString.split("\\|");

        //restore the board using the first part of the string
        board.fromString(specificGameItem[0]);

        //restore the current player from the second part
        currentPlayer = specificGameItem[1].charAt(0);

        //restore the winner from the third part
        winner = specificGameItem[2].charAt(0);

        //restore the game state using the enum value from the fourth part
        gameState = GameState.valueOf(specificGameItem[3]);

        //restore the move count from the fifth part
        moveCount = Integer.parseInt(specificGameItem[4]);
    }
    //GETTERS for new functions

    //this getter gets the current player (X, O, ' ')
    public char getCurrentPlayer() {
        //return the current player (char)
        return currentPlayer;
    }

    //this getter gets the winner (whoever is the winner gets pulled here [X, O, ' '])
    public char getWinner() {
        //return the winner (char)
        return winner;
    }

    //this getter gets the current gameState
    public GameState getGameState() {
        //return the gamestate (uses ENUM file)
        return gameState;
    }

    //this getter gets the integer move count
    public int getMoveCount() {
        //this returns the move count (int)
        return moveCount;
    }


    //RULES AND VALIDAITON WORK :D

    //this is my function for checking if there is a win
    public boolean validateWin(char userGameIdentity) {
        //loop through all the rows
        for (int row = 0; row < 3; row++) {
            //if all three cells within a row are the same user identity ('X' or 'O') then the user has won!
            if (board.getCellInfo(row, 0) == userGameIdentity && board.getCellInfo(row, 1) == userGameIdentity && board.getCellInfo(row, 2) == userGameIdentity) {
                //return true aka a win
                return true;
            }
        }

        //loop through all columns
        for (int col = 0; col < 3; col++) {
            //if all three cells within a column are the same user identity ('X' or 'O') then the user has won!
            if (board.getCellInfo(0, col) == userGameIdentity && board.getCellInfo(1, col) == userGameIdentity && board.getCellInfo(2, col) == userGameIdentity) {
                //return true aka a win
                return true;
            }
        }

        //this is a diagonal check to see if the user has won in a diagonal
        if (board.getCellInfo(0, 2) == userGameIdentity && board.getCellInfo(1, 1) == userGameIdentity && board.getCellInfo(2, 0) == userGameIdentity) {
            return true;
        }

        //this is our second diagonal check (there are two diagonals, top left to bottom right and top right to bottom left) to see if the user has won
        if (board.getCellInfo(0, 0) == userGameIdentity && board.getCellInfo(1, 1) == userGameIdentity && board.getCellInfo(2, 2) == userGameIdentity) {
            return true;
        }

        //if none of these have occured, the user has NOT won!
        return false;
    }

    //another validation check (GAME TIE STATUS)
    //function to check if the game is a tie
    public boolean checkGameTie() {

        //if the game board is full and the winner is ' ' (no winner)
        if (board.isTheBoardFull() && winner == ' ') {

            //return true (the game is a tie)
            return true;
        }

        //otherwise return false (the game is NOT a tie)
        return false;
    }

    /*
    Method to help with testing, allows you to manually set the board
     */
    public void setBoard(TicTacToeBoard newBoard){
        this.board = newBoard;
    }

    /*
    Testing function, to manipulate game state
     */
    public void setGameState(GameState newState){
        this.gameState = newState;
    }
}