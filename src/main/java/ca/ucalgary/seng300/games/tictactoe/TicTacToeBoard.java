package ca.ucalgary.seng300.games.tictactoe;

//Note*** using the abbreviation "ttt" in place of tic tac toe for some coding aspects

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

    //this is my getter for getting the whole board
    public char[][] getBoard(){
        //return the entire board
        return board;
    }
    //this is my function for checking if a cell is empty
    public boolean isCellEmpty(int row, int col){
        //check boolean (true or false) if a cell is empty
        return board[row][col] == ' ';
    }

    //this is my function for getting the value within a cell (empty, X, O)
    public char getCellInfo(int row, int col) {
        //return the value stored within a specific cell
        return board[row][col];
    }

    //this is my function for setting a cell to a given value (empty, X or O)
    public void setCellInfo(int row, int col, char tttValue) {
        //set a specific cell to a specific value
        board[row][col] = tttValue;
        //ttt = tic tac toe just using an abbreviation for code, will put note at top of doc!
    }

    //ADDING TIE DECETION
    //simple function for checking if the board is full or not
    public boolean isTheBoardFull() {
        //iterate through all rows
        for (int row = 0; row < 3; row++) {

            //iterate through all columns
            for (int col = 0; col < 3; col++) {

                //if there is a column with an empty space
                if (board[row][col] == ' ') {

                    //return false (not a tie)
                    return false;
                }
            }
        }
        //return true (there are no more playable moves)
        return true;
    }


    //methods for db integration

    //These are finalized methods for database integration (HOW THE DATABASE TEAM CAN OBTAIN UPDATE AND SEND OUT TIC TAC TOE GAME BOARDS)
    @Override
    public String toString() {
        //this function converts the current status of the board into a string for storage in the database

        StringBuilder gameSB = new StringBuilder();
        //this initiates a new stringbuilder called sb

        for (int row = 0; row < 3; row++) {
            //iterate through all rows

            for (int col = 0; col < 3; col++) {
                //iterate through all collumns

                gameSB.append(board[row][col]);
                //add the value of a each cell to the string

                if (!(row == 2 && col == 2)) {
                    //validation check for not being the last value/cell of the board

                    gameSB.append(",");
                    //this adds a comma between everything to separate values within the board in the string
                }
            }
        }
        return gameSB.toString();
        //returns the final string represntation of the board
    }

    //This is the fromString function to help with
    public void fromString(String gameBoardString){
        //this function takes the string from the database and reconstructs a game board

        String[] boardValues = gameBoardString.split(",");
        //splits the string from toString into an array with commas as the separators

        int temp = 0;
        //this temp is just the index

        for (int row = 0; row < 3; row++) {
            //iterate through all rows

            for (int col = 0; col < 3; col++) {
                //iterate through all collumns

                board[row][col] = boardValues[temp].charAt(0);
                //assign a value to each cell in the board based on the start of the string

                temp++;
                //move to the next value within the array we created
            }
        }
    }

}
