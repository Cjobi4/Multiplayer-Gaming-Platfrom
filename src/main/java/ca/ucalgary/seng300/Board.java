package ca.ucalgary.seng300;

public class Board {

    /**
     * No piece in board (empty)
     */
    public static final int EMP = 0;
    /**
     * Connect-L Red Piece
     */
    public static final int RED = 0;
    /**
     * Connect-L Blue Piece
     */
    public static final int BLU = 0;

    //Students should enter their functions below here
    /**
     * Create a board using a nested for loop and fill the board with empty slots
     */

    public static int[][] createBoard(int rows, int columns) {
        int[][] board = new int[rows][columns];
        for (int x = 0; x < rows; x++) {         //x is the number of rows of the board
            for (int y = 0; y < columns; y++) {  //y is the number of columns of the board
                board[x][y] = EMP;
            }
        }
        return board;               // return the full board that I just created
    }

    /**
     *          rowCount function
     * count the number of rows in the board
     */

    public static int rowCount(int[][] board) {
        return board.length;            // board.length equals the number of rows in the board
    }

    /**
     *          columnCount function
     * Count the number of column in the board
     */

    public static int columnCount(int[][] board) {
        return board[0].length;          // board[0].length equals the number of columns
    }

    /**
     *          valid function
     * Check if it is out of index or not
     */

    public static boolean valid(int[][] board, int row, int column) {
        return row >= 0 && row < rowCount(board) && column >= 0 && column < columnCount(board); // used in my winning function
    }

    /**
     *          canPlay function
     * Check if there is any empty space on the board
     */

    public static boolean canPlay(int[][] board, int column) {
        return board[0][column] == EMP;   //if the top of any column is empty, then there is always a space to play
    }

    /**
     *          play function
     * Used to place pieces to the designated column
     */

    public static int play(int[][] board, int column, int piece) {
        for (int i = rowCount(board) - 1; i >= 0; i--) { //loop from the bottom of the given column of the board
            if (board[i][column] == EMP) {               //If there is an empty slot
                board[i][column] = piece;               // Place the piece(color) in that slot
                return i;                                  // return the row that the piece was put in
            }
        }
        return -1;     // return -1 if that column is full
    }

    /**
     *          removeLastPlay function
     * Remove the piece that was played in the given column
     */

    public static int removeLastPlay(int[][] board, int column) {
        for (int i = 0; i < rowCount(board); i++) {         //loop the designated column
            if (board[i][column] != EMP) {                  //If it finds a pieces, then that piece must be the last played piece cause it's on top
                board[i][column] = EMP;                     //replace the piece with an empty slot
                return i;                                   //return the row that the piece was removed from
            }
        }
        return -1;      //return -1 if there is no piece in that column to remove from

    }

    /**
     *          full function
     * Used to check if the game is over or not
     */

    public static boolean full(int[][] board) {
        for (int i=0; i< board.length;i++){             // I use a nested loop to loop through the entire board
            for (int j=0; j<board[i].length; j++){
                if(board[i][j] == EMP){                 //If it finds any empty spaces
                    return false;                       //immediately returns false and the game continues
                }
            }
        }
        return true;        // return true if there is no space left to play, game is over.
    }

    /**
     *          winInRow function
     * Used to decide if red or blue has won the game in a given row with an L shape
     */

    public static boolean winInRow(int[][] board, int row, int piece, int length) {
        int col = columnCount(board);
        for(int i = 0; i< col ;i++){       // Iterate through each column in the board
            if(col - length - i < 0){     // Check if the remaining columns are enough for the required length
                return false;
            }
            int count = 0;          // Used to count occurrences of the piece in the length surveyed
            int track = length + i -1;  // Track the last column index for the required length
            for(int j = i; j<= track; j++){
                if (board[row][j] == piece){    // Check if the piece is present in the current row at column j
                    count++;     // Count increased if the piece is found
                }
            }
            if(count == length){     // If the count of pieces in the row matches the required length to win
                if(valid(board,row+1,i)){
                    if(board[row+1][i] == piece){
                        return true;    // Return true if a matching piece is found below the starting index
                    }
                }
                if(valid(board,row-1,i)){
                    if(board[row-1][i] == piece){
                        return true;    // Return true if a matching piece is found above the starting index
                    }
                }
                if(valid(board,row+1,track)){
                    if(board[row+1][track] == piece){
                        return true;   // Return true if a matching piece is found above the ending index
                    }
                }
                if(valid(board,row-1,track)){
                    if(board[row - 1][track] == piece){
                        return true;    // Return true if a matching piece is found below the ending index
                    }
                }
            }
        }
        return false; //return false if no winning sequence is found
    }

    /**
     *          winInColumn function
     * Used to decide if red or blue has won the game in a given column with an L shape
     */

    public static boolean winInColumn(int[][] board, int col, int piece, int length) {
        int row = rowCount(board);
        for(int i = 0; i< row ;i++){    // Iterate through each row in the board
            if(row - length - i < 0){  // Check if the remaining rows are enough for the required length
                return false;
            }
            int count = 0;            // Used to count occurrences of the piece in the length surveyed
            int track = length + i - 1;   // Track the last row index for the required length
            for(int j = i; j <= track; j++){
                if (board[j][col] == piece){   // Check if the piece is present in the current row at column
                    count++;               // Count increased if the piece is found
                }
            }
            if(count == length){        // If the count of pieces in the column matches the required length to win
                if(valid(board,i,col+1)){
                    if(board[i][col+1] == piece){  //Move diagonally bottom-right
                        return true;  //Return true if a matching piece is found to the right of the starting index
                    }
                }
                if(valid(board,i,col - 1)){
                    if(board[i][col - 1] == piece){
                        return true;    //Return true if a matching piece is found to the left of the starting index
                    }
                }
                if(valid(board,track,col - 1)){
                    if(board[track][col - 1] == piece){
                        return true;  //Return true if a matching piece is found to the left of the ending index
                    }
                }
                if(valid(board,track,col + 1)){
                    if(board[track][col + 1] == piece){
                        return true;  // Return true if a matching piece is found to the right side of the ending index
                    }
                }
            }
        }
        return false;
    }

    /**
     *          winInCDiagonalBackSlash function
     * Used to decide if red or blue has won the game in back slash (\) dimension with an L shape
     */

    public static boolean winInDiagonalBackslash(int[][] board, int piece, int length) {

        // Iterate through all possible starting positions in the board
        // The loop makes sure there are enough rows and columns left to form a backslash shape of the required length
        for (int row = 0; row <= rowCount(board) - length; row++) {
            for (int col = 0; col <= columnCount(board) - length; col++) {
                int count = 0;      // Used to count occurrences of the piece in the length surveyed
                int track = row + length - 1;   // Last row index of the backslash
                int track2 = col + length - 1;   // Last row index of the backslash


                // Check for a diagonal (\) pattern from the current position
                for (int i = 0; i < length; i++) {
                    if (board[row + i][col + i] == piece) {
                        count++;          // Count increased if the piece is found
                    }

                    if (count == length) {

                        // Check if there are valid L-shape positions connected to the backslash start and end position
                        if (valid(board, track - 1, track2 + 1)) {
                            if (board[track - 1][track2 + 1] == piece) {
                                return true;    // Return true if there is a piece at the top of ending position
                            }
                        }
                        if (valid(board, track + 1, track2 - 1)) {
                            if (board[track + 1][track2 - 1] == piece) {
                                return true;    // Return true if there is a piece at the bottom of ending position
                            }
                        }
                        if (valid(board, row - 1, col + 1)) {
                            if (board[row - 1][col + 1] == piece) {
                                return true;    // Return true if there is a piece at the top of starting position
                            }
                        }
                        if (valid(board, row + 1, col - 1)) {
                            if (board[row + 1][col - 1] == piece) {
                                return true;   // Return true if there is a piece at the bottom of starting position
                            }
                        }
                    }
                }
            }
        }
        return false;   // If no winning sequence is found, return false
    }

    /**
     *          winInCDiagonalForwardSlash function
     * Used to decide if red or blue has won the game in forward slash (/) dimension with an L shape
     */

    public static boolean winInDiagonalForwardSlash(int[][] board, int piece, int length) {

        // Iterate through all possible starting positions in the board
        // The loop makes sure there are enough rows and columns left to form a forward slash shape of the required length
        for (int row = rowCount(board) - 1; row >= length - 1; row--) {
            for (int col = 0; col < columnCount(board) - length; col++) {
                int count = 0;              // Used to count occurrences of the piece in the length surveyed
                int track = row - length + 1; // Last row index of the forward slash
                int track2 = col + length - 1; // Last column index of the forward slash

                // Check for a diagonal (/) pattern from the current position
                for (int i = 0; i < length; i++) {
                    if (board[row - i][col + i] == piece) {  // Move diagonally up-right
                        count++;      // Count increased if the piece is found
                    }
                    if (count == length) {

                        // Check if there are valid L-shape positions connected to the forward slash start and end position
                        if (valid(board, track - 1, track2 - 1)) {
                            if (board[track - 1][track2 - 1] == piece) {
                                return true;        //return true if there is a piece at the top-left of ending position
                            }
                        }
                        if (valid(board, track + 1, track2 + 1)) {
                            if (board[track + 1][track2 + 1] == piece) {
                                return true;    //return true if there is a piece at the bottom-right of ending position
                            }
                        }
                        if (valid(board, row + 1, col + 1)) {
                            if (board[row + 1][col + 1] == piece) {
                                return true;   //return true if there is a piece at the bottom-right of starting position
                            }
                        }
                        if (valid(board, row - 1, col - 1)) {
                            if (board[row - 1][col - 1] == piece) {
                                return true;   //return true if there is a piece at the top-left of starting position
                            }
                        }
                    }
                }
            }
        }
        return false;  // If no winning sequence is found, return false
    }

    /**
     *          hint function
     * Used to give user hints about a winning move or opponent's winning move
     */

    public static int[] hint(int[][] board, int piece, int length) {
        int col = columnCount(board);
        int row = rowCount(board);

        //loop from the bottom of the board and check for every empty slots that can be played next
        for(int i = 0; i<col; i++){
            for(int j = row-1; j>=0;j--){
                if(board[j][i] == EMP){  //If the slot is found
                    board[j][i] = piece;       //Place a piece at that position
                    if(won(board,piece,length)){    //If placing that piece at that position ended winning the game for either the player or the opponent
                        board[j][i] = EMP;          //Remove the piece that has been played
                        return new int[]{j,i};      //Return the position {i,j} of the move that could end the game
                    } else {                    //else if no move is considered winning move
                        board[j][i] = EMP;    //just simply remove the piece that has been placed in for testing
                    }
                }
            }
        }
        return new int[]{-1,-1};    //If no winning move is found then return {-1,-1}
    }

    //Students should enter their functions above here
    /**
     * Is there a win in given board in any row of board
     *
     * @param board The 2D array board of size rows (dimension 1) and columns (dimension 2)
     * @param piece The piece to look for length in a row for any row
     * @return True if there is length in any row, False otherwise
     */
    private static boolean winInAnyRow(int[][] board, int piece, int length) {
        for (int row = 0; row < board.length; row++) {
            if (winInRow(board, row, piece, length)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is there a win in given board in any column of board
     *
     * @param board The 2D array board of size rows (dimension 1) and columns (dimension 2)
     * @param piece The piece to look for length in a row for any column
     * @return True if there is length in any column, False otherwise
     */
    private static boolean winInAnyColumn(int[][] board, int piece, int length) {
        for (int col = 0; col < board[0].length; col++) {
            if (winInColumn(board, col, piece, length)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is there a win in given board in any diagonal of board
     *
     * @param board The 2D array board of size rows (dimension 1) and columns (dimension 2)
     * @param piece The piece to look for length in a row for any diagonal
     * @return True if there is length in any diagonal /\, False otherwise
     */
    private static boolean winInAnyDiagonal(int[][] board, int piece, int length) {
        return winInDiagonalBackslash(board, piece, length) || winInDiagonalForwardSlash(board, piece, length);
    }

    /**
     * Has the given piece won the board
     *
     * @param board The 2D array board of size rows (dimension 1) and columns (dimension 2)
     * @param piece The piece to check for a win
     * @return True if piece has won
     */
    public static boolean won(int[][] board, int piece, int length) {
        return winInAnyRow(board, piece, length) || winInAnyColumn(board, piece, length) || winInAnyDiagonal(board, piece, length);
    }

    /**
     * This function determines if the game is complete due to a win or tie by either player
     *
     * @param board The 2D array board of size rows (dimension 1) and columns (dimension 2)
     * @return True if game is complete, False otherwise
     */
    public static boolean isGameOver(int[][] board, int length) {
        return full(board) || won(board, RED, length) || won(board, BLU, length);
    }
}

