package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.connectfour.ConnectFourGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectFourGameTest {
    @Test
    void testGetCurrentPlayerBeforeMoveMade() {
        ConnectFourGame game = new ConnectFourGame();
        char expectedCurrentPlayer = 'X';
        char actualCurrentPlayer = game.getCurrentPlayer();

        assertEquals(expectedCurrentPlayer, actualCurrentPlayer, "The currently player is expected to be 'X' and the actual current player is: " + actualCurrentPlayer);
    }

    @Test
    void testConnectFourVertical0thColumnWinsCorrect(){
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

    @Test
    void testConnectFourVertical1stColWinsCorrect(){
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

    @Test
    void testConnectFourVertical2ndColWinsCorrect(){
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

    @Test
    void testConnectFourVertical3rdColWinsCorrect(){
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

    @Test
    void testConnectFourVertical4thColWinsCorrect(){
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

    @Test
    void testConnectFourVertical5thColWinsCorrect(){
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

    @Test
    void testConnectFourVertical6thColWinsCorrect(){
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

    @Test
    void testBackslashDiagonalWinsCorrect(){
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

    @Test
    void testForwardSlashDiagonalWinsCorrect(){
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

    @Test
    void testHorizontal1stRowWinsCorrect(){
        // 1. arrange

        // 2. act

        // 3. assertions
    }
}
