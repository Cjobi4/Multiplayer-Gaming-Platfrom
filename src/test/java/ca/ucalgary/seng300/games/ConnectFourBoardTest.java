package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.connectfour.ConnectFourBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class focuses on testing for the methods and logic in the ConnectFourBoard.java class
 */
public class ConnectFourBoardTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * This is set up in order to help get the content of the printBoard method tests
     */
    @BeforeEach
    public void setUpStreams(){
        System.setOut(new PrintStream(outContent));
    }

    /**
     * This is set up in order to help get the content of the printBoard method tests
     */
    @AfterEach
    public void restoreStreams(){
        System.setOut(originalOut);
    }

    /**
     * Testing the ConnectFourBoard method
     * Input : NA
     * Expected Output: A board of size 6x7 with cells initialized to '.'
     * Actual Output: the board created by the ConnectFourBoard method
     */
    @Test
    void testConnectFourBoardSetsUp6x7() {
        ConnectFourBoard board = new ConnectFourBoard();
        int expectedRows = 6;
        int expectedColumns = 7;

        int actualRows = board.getRows();
        int actualColumns = board.getCols();

        // checking if the actual board is initialized to have '.' in each cell
        for (int row = 0; row < expectedRows; row++) {
            for (int col = 0; col < expectedColumns; col++) {
                assertEquals('.', board.getCell(row, col), "Cell at (" + row + ", " + col + ") should be initialized to '.'");
            }
        }

        assertEquals(expectedColumns, actualColumns, "Columns should be equal to expected number of rows: " + expectedRows);
        assertEquals(expectedRows, actualRows, "Rows should be equal to expected number of rows: " + expectedRows);
    }

    /**
     * Testing the dropPieceMethod
     * Input: Column 5 and player 'X' into dropPiece
     * Expected Output: boolean true
     * Actual Output: boolean statement about whether the piece could be added or not
     */
    @Test
    void testDropPieceWhenColumnIsEmpty() {
        ConnectFourBoard board = new ConnectFourBoard();
        char player = 'X';
        board.dropPiece(5, player);

        char expectedPiece = 'X';
        char actualPiece = board.getCell(5, 5);

        assertTrue(board.dropPiece(5, player), "The board is empty, thus the piece should be dropped.");
        assertEquals(expectedPiece, actualPiece, "The piece should be dropped and should show as the player.");
    }

    /**
     * Testing the dropPieceMethod
     * Input: fill the whole board with player 'O' pieces
     * Expected Output: boolean false
     * Actual Output: boolean statement about whether the piece could be added or not
     */
    @Test
    void testDropPieceWhenColumnIsFull() {
        ConnectFourBoard board = new ConnectFourBoard();
        char player1 = 'X';
        char player2 = 'O';

        //filling the board with player 'O'
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                board.dropPiece(col, player2);
            }
        }

        // now, check if player X's piece can be dropped when the column/board is full
        boolean expectedReturn = false;
        boolean actualReturn = board.dropPiece(6, player1);

        assertEquals(expectedReturn, actualReturn, "Should not be able to add player X's piece when the column/board is full");
    }

    /**
     * Testing the dropPieceMethod logic
     * Input: adding a piece to an out-of-bounds column
     * Expected Output: boolean false
     * Actual Output: boolean statement about whether the piece could be added or not
     */
    @Test
    void testDropPieceWhenOutOfBounds() {
        ConnectFourBoard board = new ConnectFourBoard();
        char player = 'X';

        assertFalse(board.dropPiece(7, player), "The chosen column is out of bounds, thus the method dropPiece(col, player) should return as false.");
    }

    /**
     * Testing isFull method
     * Input: empty board
     * Expected Output: false
     * Actual Output: boolean value of board.isFull()
     */
    @Test
    void testIsFullWhenBoardIsEmpty() {
        ConnectFourBoard board = new ConnectFourBoard();
        boolean expectedReturn = false;
        boolean actualReturn = board.isFull();

        assertEquals(expectedReturn, actualReturn, "The board should not be full.");
    }

    /**
     * Testing isFull method
     * Input: partially filled board
     * Expected Output: false
     * Actual Output: boolean value of board.isFull()
     */
    @Test
    void testIsFullWhenBoardIsNeitherEmptyNorFull() {
        ConnectFourBoard board = new ConnectFourBoard();
        char player1 = 'X';
        char player2 = 'O';

        // filling one column completely but leaving the rest with just a few pieces
        board.dropPiece(0, player1);
        board.dropPiece(0, player2);
        board.dropPiece(0, player1);
        board.dropPiece(0, player2);
        board.dropPiece(0, player1);
        board.dropPiece(0, player2);

        board.dropPiece(1, player1);
        board.dropPiece(1, player2);

        board.dropPiece(2, player1);
        board.dropPiece(2, player2);

        board.dropPiece(3, player1);

        board.dropPiece(4, player2);

        board.dropPiece(5, player1);
        board.dropPiece(5, player2);

        board.dropPiece(6, player1);
        board.dropPiece(6, player2);

        // the actual test
        boolean expectedReturn = false;
        boolean actualReturn = board.isFull();
        assertEquals(expectedReturn, actualReturn, "The board should not be full.");
    }

    /**
     * Testing isFull method
     * Input: filled board
     * Expected Output: true
     * Actual Output: boolean value of board.isFull()
     */
    @Test
    void testIsFullWhenBoardIsFull() {
        ConnectFourBoard board = new ConnectFourBoard();
        char player = 'O';

        //filling the board with player 'O'
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                board.dropPiece(col, player);
            }
        }

        boolean expectedReturn = true;
        boolean actualReturn = board.isFull();

        assertEquals(expectedReturn, actualReturn, "The board should be full.");
    }

    /**
     * Testing the printBoard method
     * Input: empty board
     * Expected Output: A board of size 6x7 with cells initialized to '.'
     * Actual Output: board printed by the printBoard method
     */
    @Test
    void testPrintBoardWhenBoardIsEmpty() {
        ConnectFourBoard board = new ConnectFourBoard();
        board.printBoard();

        String expectedOutput = ". . . . . . . \n" +
                                ". . . . . . . \n" +
                                ". . . . . . . \n" +
                                ". . . . . . . \n" +
                                ". . . . . . . \n" +
                                ". . . . . . . \n" +
                                "0 1 2 3 4 5 6\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput, "The board should be empty.");
    }

    /**
     * Testing the printBoard method
     * Input: partially full board
     * Expected Output: A board of size 6x7 with some of the cells filled
     * Actual Output: board printed by the printBoard method
     */
    @Test
    void testPrintBoardWhenBoardIsPartiallyFull(){
        ConnectFourBoard board = new ConnectFourBoard();
        char player1 = 'X';
        char player2 = 'O';

        // adding pieces to the board
        board.dropPiece(0, player1);
        board.dropPiece(0, player2);
        board.dropPiece(0, player1);
        board.dropPiece(0, player2);
        board.dropPiece(0, player1);
        board.dropPiece(0, player2);

        board.dropPiece(1, player1);
        board.dropPiece(1, player2);

        board.dropPiece(2, player1);
        board.dropPiece(2, player2);

        board.dropPiece(3, player1);

        board.dropPiece(4, player2);

        board.dropPiece(5, player1);
        board.dropPiece(5, player2);

        board.dropPiece(6, player1);
        board.dropPiece(6, player2);

        // what the board should look like
        String expectedOutput = "O . . . . . . \n" +
                                "X . . . . . . \n" +
                                "O . . . . . . \n" +
                                "X . . . . . . \n" +
                                "O O O . . O O \n" +
                                "X X X X O X X \n" +
                                "0 1 2 3 4 5 6\n";
        board.printBoard();
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput, "The board should be partially full.");
    }
}
