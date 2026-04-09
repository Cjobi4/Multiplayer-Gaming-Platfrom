package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.connectfour.ConnectFourGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test class focuses on testing the methods and logic of the ConnectFourGame.java class
 */
public class ConnectFourGameTest {

    /**
     * Testing the getCurrentPlayer() method
     * Input: NA
     * Expected Output: player 'X'
     * Actual Output: the player that is initialized as the first player when a new game is created
     */
    @Test
    void testGetCurrentPlayerBeforeMoveMade() {
        ConnectFourGame game = new ConnectFourGame();
        char expectedCurrentPlayer = 'X';
        char actualCurrentPlayer = game.getCurrentPlayer();

        assertEquals(expectedCurrentPlayer, actualCurrentPlayer, "The currently player is expected to be 'X' and the actual current player is: " + actualCurrentPlayer);
    }

    /**
     * Testing the switchPlayer() method
     * Input: NA
     * Expected Output: player 'O'
     * Actual Output: the player that current player switches to when the method is called
     */
    @Test
    void testSwitchPlayerCorrectlySwitchesPlayerFromXtoO() {
        ConnectFourGame game = new ConnectFourGame();
        game.switchPlayer();
        char expectedCurrentPlayer = 'O';
        char actualCurrentPlayer = game.getCurrentPlayer();

        assertEquals(expectedCurrentPlayer, actualCurrentPlayer, "The expected current player after switching player was 'O' and the actual current player is: " + actualCurrentPlayer);
    }

    /**
     * Testing the method getCurrentPlayer after a move has been made
     * Input: NA
     * Expected Output: player 'O'
     * Actual Output: the player that current player is updated to after makeMove is used
     */
    @Test
    void testGetCurrentPlayerAfterMoveMade() throws Exception {
        ConnectFourGame game = new ConnectFourGame();
        game.makeMove(0);

        char expectedCurrentPlayer = 'O';
        char actualCurrentPlayer = game.getCurrentPlayer();

        assertEquals(expectedCurrentPlayer, actualCurrentPlayer, "The current player should be 'O' and the actual current player is: " + actualCurrentPlayer);
    }

    /**
     * Testing if the checkWin method correctly identifies a vertical win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testConnectFourVertical0thColumnWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(0);   // player x 1
        game.makeMove(1);   // player o
        game.makeMove(0);   // player x 2
        game.makeMove(2);   // player o
        game.makeMove(0);   // player x 3
        game.makeMove(2);   // player o
        game.makeMove(0);   // player x

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true for the 0 column vertical.");
    }

    /**
     * Testing if the checkWin method correctly identifies a vertical win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testConnectFourVertical1stColWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(0);   // x
        game.makeMove(1);   // o 1
        game.makeMove(2);   // x
        game.makeMove(1);   // o 2
        game.makeMove(0);   // x
        game.makeMove(1);   // o 3
        game.makeMove(2);   // x
        game.makeMove(1);   // o 4

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true for the 1 column vertical.");
    }

    /**
     * Testing if the checkWin method correctly identifies a vertical win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testConnectFourVertical2ndColWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(2);   // player x 1
        game.makeMove(1);   // player o
        game.makeMove(2);   // player x 2
        game.makeMove(1);   // player o
        game.makeMove(2);   // player x 3
        game.makeMove(0);   // player o
        game.makeMove(2);   // player x

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true for the 2 column vertical.");
    }

    /**
     * Testing if the checkWin method correctly identifies a vertical win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testConnectFourVertical3rdColWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(3);   // player x 1
        game.makeMove(1);   // player o
        game.makeMove(3);   // player x 2
        game.makeMove(1);   // player o
        game.makeMove(3);   // player x 3
        game.makeMove(0);   // player o
        game.makeMove(3);   // player x

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true for the 3 column vertical.");
    }

    /**
     * Testing if the checkWin method correctly identifies a vertical win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testConnectFourVertical4thColWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(4);   // player x 1
        game.makeMove(1);   // player o
        game.makeMove(4);   // player x 2
        game.makeMove(1);   // player o
        game.makeMove(4);   // player x 3
        game.makeMove(0);   // player o
        game.makeMove(4);   // player x

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true for the 4 column vertical.");
    }

    /**
     * Testing if the checkWin method correctly identifies a vertical win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testConnectFourVertical5thColWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(5);   // player x 1
        game.makeMove(1);   // player o
        game.makeMove(5);   // player x 2
        game.makeMove(1);   // player o
        game.makeMove(5);   // player x 3
        game.makeMove(0);   // player o
        game.makeMove(5);   // player x

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true for the 5 column vertical.");
    }

    /**
     * Testing if the checkWin method correctly identifies a vertical win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testConnectFourVertical6thColWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(6);   // player x 1
        game.makeMove(1);   // player o
        game.makeMove(6);   // player x 2
        game.makeMove(1);   // player o
        game.makeMove(6);   // player x 3
        game.makeMove(0);   // player o
        game.makeMove(6);   // player x

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true for the 6 column vertical.");
    }

    /**
     * Testing if the checkWin method correctly identifies a diagonal win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testBackslashDiagonalWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        // need to create a backslash of 'O'
        game.makeMove(0); //x
        game.makeMove(1); // o
        game.makeMove(2); // x
        game.makeMove(3); // o
        game.makeMove(4); // x
        game.makeMove(2); // o
        game.makeMove(1);  // x
        game.makeMove(1);  // o
        game.makeMove(0); // x
        game.makeMove(2); // o
        game.makeMove(0); // x
        game.makeMove(0); // 4th o to create the diagonal

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true and it was: " + actualWinCondition);
    }

    /**
     * Testing if the checkWin method correctly identifies a diagonal win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testForwardSlashDiagonalWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        // manually add each piece
        game.makeMove(2); //x
        game.makeMove(3); //o
        game.makeMove(3); // x
        game.makeMove(4); // o
        game.makeMove(5); //x
        game.makeMove(4); // o
        game.makeMove(4); //x
        game.makeMove(5); // o
        game.makeMove(6); // x
        game.makeMove(5); // o
        game.makeMove(5); // x

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true and it was: " + actualWinCondition);
    }

    /**
     * Testing if the checkWin method correctly identifies a horizontal win
     * Input: added pieces to the board
     * Expected Output: true win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testHorizontal1stRowWinsCorrect() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        game.makeMove(0); //x
        game.makeMove(1); //o
        game.makeMove(1); //x
        game.makeMove(2); //o
        game.makeMove(2); //x
        game.makeMove(3); //o
        game.makeMove(3); // x
        game.makeMove(4); // o --> last piece to create 4 in a row horizontally

        boolean expectedWinCondition = true;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be true and it was: " + actualWinCondition);
    }

    /**
     * Testing if the checkWin method correctly identifies no wins when the board is empty
     * Input: added pieces to the board
     * Expected Output: false win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testCheckWinWhenBoardIsEmptyReturnsFalse() {
        ConnectFourGame game = new ConnectFourGame();
        boolean expectedWinCondition = false;
        boolean actualWinCondition = game.checkWin();

        assertEquals(expectedWinCondition, actualWinCondition, "It is an empty board, thus the win condition should be false.");
    }

    /**
     * Testing if the checkWin method correctly identifies no wins when there shouldn't be a win
     * Input: added pieces to the board
     * Expected Output: false win condition
     * Actual Output: the win condition based on the checkWin logic
     */
    @Test
    void testCheckWinWhenBoardHasNoWinsReturnsFalse() throws Exception {
        ConnectFourGame game = new ConnectFourGame();

        // adding pieces to the board
        game.makeMove(0); //x
        game.makeMove(1); //o
        game.makeMove(1); //x
        game.makeMove(2); //o
        game.makeMove(2); //x

        boolean expectedWinCondition = false;
        boolean actualWinCondition = game.checkWin();
        assertEquals(expectedWinCondition, actualWinCondition, "The win condition should be false.");
    }
}
