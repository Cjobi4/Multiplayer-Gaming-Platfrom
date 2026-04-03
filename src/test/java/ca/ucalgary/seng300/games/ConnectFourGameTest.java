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
        char player1 =  'X';
        char player2 = 'O';

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
    void testForwardSlashDiagonalWinsCorrect(){
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
