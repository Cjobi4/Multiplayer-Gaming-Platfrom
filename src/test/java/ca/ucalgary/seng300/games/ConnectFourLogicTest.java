package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.connectfour.connectFourBoard;
import ca.ucalgary.seng300.games.connectfour.connectFourGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectFourLogicTest {

    @Test
    void testConnectFourBoardSetsUp6x7() {
        connectFourBoard board = new connectFourBoard();
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
    void testConnectFourVerticalLeftMostEdgeWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }
    @Test
    void testConnectFourVerticalRightMostEdgeWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }
    @Test
    void testBackslashDiagonalWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

    @Test
    void testForwardslashDiagonalWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

    @Test
    void testHorizontal1stRowWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

    @Test
    void testHorizontal2ndRowWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

    @Test
    void testHorizontal3rdRowWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

    @Test
    void testHorizontal4thRowWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

    @Test
    void testHorizontal5thRowWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

    @Test
    void testHorizontal6thRowWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }

}
