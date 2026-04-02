package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.connectfour.ConnectFourBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    void testDropPieceWhenColumnIsFull() {
        ConnectFourBoard board = new ConnectFourBoard();
    }

    @Test
    void testIsFullWhenBoardIsEmpty() {
        ConnectFourBoard board = new ConnectFourBoard();
    }

    @Test
    void testIsFullWhenBoardIsNeitherEmptyNorFull() {
        ConnectFourBoard board = new ConnectFourBoard();
    }

    @Test
    void testIsFullWhenBoardIsFull() {
        ConnectFourBoard board = new ConnectFourBoard();
    }
}
