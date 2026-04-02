package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.connectfour.ConnectFourBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectFourBoardTest {
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
                assertEquals('.', board.getCell(row, col), "Cell at (" + row + ", " + col + ") should be initialized to ','");
            }
        }

        assertEquals(expectedColumns, actualColumns, "Columns should be equal to expected number of rows: " + expectedRows);
        assertEquals(expectedRows, actualRows, "Rows should be equal to expected number of rows: " + expectedRows);
    }

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

    @Test
    void testDropPieceWhenOutOfBounds() {
        ConnectFourBoard board = new ConnectFourBoard();
        char player = 'X';

        assertFalse(board.dropPiece(7, player), "The chosen column is out of bounds, thus the method dropPiece(col, player) should return as false.");
    }

    @Test
    void testIsFullWhenBoardIsEmpty() {
        ConnectFourBoard board = new ConnectFourBoard();
        boolean expectedReturn = false;
        boolean actualReturn = board.isFull();

        assertEquals(expectedReturn, actualReturn, "The board should not be full.");
    }

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

    @Test
    void testPrintBoardWhenBoardIsEmpty() {
        ConnectFourBoard board = new ConnectFourBoard();
    }

    @Test
    void testPrintBoardWhenBoardIsFull() {
        ConnectFourBoard board = new ConnectFourBoard();
    }

    @Test
    void testPrintBoardWhenBoardIsPartiallyFull(){
        ConnectFourBoard board = new ConnectFourBoard();
    }
}
