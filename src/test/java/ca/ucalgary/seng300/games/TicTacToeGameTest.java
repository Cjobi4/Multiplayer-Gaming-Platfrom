package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicTacToeGameTest {
    /*
    Test that upon initialization all the default values are correct
     */
    @Test
    void initializationTest(){
        TicTacToeGame testGame = new TicTacToeGame();
        // create base game, no modifications to it

        Assertions.assertEquals('X', testGame.getCurrentPlayer()); // correct player symbol
        Assertions.assertEquals(' ', testGame.getWinner()); // no winner
        Assertions.assertEquals(GameState.TURN_AWAITING_MOVE, testGame.getGameState()); // waiting for a move
        Assertions.assertEquals(0, testGame.getMoveCount()); // no moves yet

        Assertions.assertNotNull(testGame.getBoard()); // make sure board exists
    }

    /*
    Test that the move validation works as intended
     */
    @Test
    void makeMoveTest(){
        TicTacToeGame testGame = new TicTacToeGame();

        TicTacToeBoard testBoard = new TicTacToeBoard();
        testBoard.fromString("X,X,X,X, ,X,X,X,X");
        //mostly full board

        testGame.setBoard(testBoard);

        // testing bounds and character checker
        Assertions.assertFalse(testGame.makeMove(5,4,'X'));
        Assertions.assertFalse(testGame.makeMove(1,1, 'E'));

        // theoretically valid move, because empty space in board
        Assertions.assertTrue(testGame.makeMove(1,1,'O'));
        // should be false because trying to place in a non-empty space
        Assertions.assertFalse(testGame.makeMove(1,1,'X'));
    }



    @Test
    void testWinConditions(){

    }
}
