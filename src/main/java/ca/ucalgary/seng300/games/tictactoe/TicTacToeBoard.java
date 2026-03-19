package ca.ucalgary.seng300.games.tictactoe;

public class TicTacToeBoard {
    //this is my board class for the tic tac toe game!

    private char[][] board;
    //This is my character 2D array for the game board (cells will hold empty= ' ', X, or O)

    public TicTacToeBoard(){
    //this is my constructor that initializes the gameboard

        board = new char[3][3];
        //this creates a 3 by 3 grid

        for (int row = 0; row < 3; row++) {
            //this is my for loop for looping through each row of the board

            for (int col = 0; col < 3; col++) {
                //this is my for loop for looping through each column of the board

                board[row][col] = ' ';
                //this sets each slot of the game board as empty
            }
        }
    }
}
