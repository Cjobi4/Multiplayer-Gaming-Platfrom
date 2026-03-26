package ca.ucalgary.seng300.games.tictactoe;

//import the enums file
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

    //this creates the game with a new board
    public TicTacToeGame(){
        //create a new gameboard
        board = new TicTacToeBoard();

        //set the current player to X
        currentPlayer = 'X';

        //set the value of the winner (string) to nothing because there is no winner at the start of the game
        winner = ' ';

        //set the start of the game to waiting for a player to make a move
        gameState = GameState.TURN_AWAITING_MOVE;

        //this sets the movecount to 0 as no moves by the start of the game
        moveCount = 0;
    }

    //this returns the board that is being used during the game
    public TicTacToeBoard getBoard(){
        //return the current gameboard
        return board;
    }

    //this is my function for making a move and VALIDATING the move!
    public boolean makeMove(int row, int col, char userGameIdentity) {

        //bounds check moved to first in the makeMove function
        //this checks/make sure the users position selected stays within the bounds of the game board
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            //if not return false
            return false;
        }

        //make sure that the user has a valid identity ('X' or 'O')
        if (userGameIdentity != 'X' && userGameIdentity != 'O') {
            //if not, return false
            return false;
        }

        //make sure that the cell the player is trying to get to is not already occupied
        if (!board.isCellEmpty(row, col)) {
            //return false if it is occupied
            return false;
        }


        //after all validation checks
        //we can set the players data ('X' or 'O') onto the board at the specific row and column they want to be at
        board.setCellInfo(row, col, userGameIdentity);
        //return true!
        return true;
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

}
