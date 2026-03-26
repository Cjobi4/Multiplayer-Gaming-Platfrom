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


}
