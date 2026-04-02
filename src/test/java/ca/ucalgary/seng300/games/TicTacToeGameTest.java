package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicTacToeGameTest {
    // smaller personal note,

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

    /*
    Simply checking for various tie scenarios
     */
    @Test
    void testTieConditions(){
        TicTacToeGame testGame = new TicTacToeGame();

        TicTacToeBoard testBoard = new TicTacToeBoard();
        Assertions.assertFalse(testGame.checkGameTie()); // false since game just started

        testBoard.fromString("X,X, ,O,X,O,X,O,X"); //full board, but X wins
        testGame.setBoard(testBoard);
        testGame.makeMove(2,2,'X'); // need to make the move to trigger a board change
        Assertions.assertFalse(testGame.checkGameTie());

        testBoard.fromString("O,X, ,X,O,O,X,O,X"); // tie game, no winner
        testGame.setBoard(testBoard);
        testGame.makeMove(0,2,'O'); // once again, need to make the winning move
        Assertions.assertTrue(testGame.checkGameTie());
    }

    /*
    Multiple win checkers, because a lot of potential scenarios
     */

    // For all of these tests, I'll use 'X' because win logic remains the same regardless of char
    @Test
    void testWinHorizontalConditions(){
        TicTacToeGame testGame = new TicTacToeGame();

        TicTacToeBoard testBoard = new TicTacToeBoard();
        testBoard.fromString("");
    }

    @Test
    void testWinVertical(){

    }

    @Test
    void testWinDiagonal(){

    }
}
