package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


public class TicTacToeBoardTest {

    /*
    Testing that every cell upon initialization is empty
     */
    @Test
    void testInitialization(){
        TicTacToeBoard testBoard = new TicTacToeBoard();
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Assertions.assertTrue(testBoard.isCellEmpty(i,j));
            } // simply runs the assertion for every cell in the board
        }
    }

    /*
    Verifying that changing a cell is reflected in getCellInfo()
     */
    @Test
    void testModifyCell(){
        TicTacToeBoard testBoard = new TicTacToeBoard();

        testBoard.setCellInfo(2,2, 'X');
        Assertions.assertEquals('X', testBoard.getCellInfo(2,2)); // changing blank cell

        testBoard.setCellInfo(2, 2, 'O');
        Assertions.assertEquals('O', testBoard.getCellInfo(2,2));
        // should never happen in a real game, but worth testing
    }

    @Test
    void testEmptyOrFullBoardMethod(){
        TicTacToeBoard testBoard = new TicTacToeBoard(); // empty board
        Assertions.assertFalse(testBoard.isTheBoardFull());

        testBoard.fromString("X,X,X,X,X,X,X,X, "); // nearly full board
        Assertions.assertFalse(testBoard.isTheBoardFull());

        testBoard.fromString("X,X,X,X,X,X,X,X,X"); // entirely full board
        Assertions.assertTrue(testBoard.isTheBoardFull());
    }

    @Test
    void verifyToStringMethod(){
        TicTacToeBoard testBoard = new TicTacToeBoard(); // empty board
        Assertions.assertEquals(" , , , , , , , , ", testBoard.toString());

        testBoard.setCellInfo(0, 0, 'X');
        testBoard.setCellInfo(1, 1, 'X'); // fill in a diagonal
        testBoard.setCellInfo(2, 2, 'X');
        Assertions.assertEquals("X, , , ,X, , , ,X", testBoard.toString());
    }
}
