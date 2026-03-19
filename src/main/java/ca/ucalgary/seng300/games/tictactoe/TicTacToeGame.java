package ca.ucalgary.seng300.games.tictactoe;

//this is for game/move logic for tic tac toe
public class TicTacToeGame {

    //this is the board we are going to use for the game
    private TicTacToeBoard board;

    //this creates the game with a new board
    public TicTacToeGame(){
        //create a new gameboard
        board = new TicTacToeBoard();
    }

    //this returns the board that is being used during the game
    public TicTacToeBoard getBoard(){
        //return the current gameboard
        return board;
    }

    //this is my function for making a move and VALIDATING the move!
    public boolean makeMove(int row, int col, char userGameIdentity) {
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

        //this checks/make sure the users position selected stays within the bounds of the game board
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            //if not return false
            return false;
        }

        //after all validation checks
        //we can set the players data ('X' or 'O') onto the board at the specific row and column they want to be at
        board.setCellInfo(row, col, userGameIdentity);
        //return true!
        return true;
    }

}
