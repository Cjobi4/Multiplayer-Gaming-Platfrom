package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicTacToeGameTest {
    // smaller somewhat unimportant note, but very stringent class
    // relies extremely heavily on GameState, with no explicit setter though this isn't inherently an issue

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
    Test that the move validation works as intended on bad moves

    @Test
    void makeMoveTest() throws Exception {
        TicTacToeGame testGame = new TicTacToeGame();

        TicTacToeBoard testBoard = new TicTacToeBoard();
        testBoard.fromString("X,X,X,X, ,X,X,X,X");
        //mostly full board

        testGame.setBoard(testBoard);

        // testing bounds and character checker
        Assertions.assertFalse(testGame.makeMove(5,4,'X'));
        Assertions.assertFalse(testGame.makeMove(-1,-1,'O'));
        Assertions.assertFalse(testGame.makeMove(1,1, 'E'));

        // valid move, because empty space in board
        Assertions.assertTrue(testGame.makeMove(1,1,'O'));
        Assertions.assertEquals('O', testGame.getBoard().getCellInfo(1,1)); // check the proper character was placed
        // should be false because trying to place in a occupied space
        Assertions.assertFalse(testGame.makeMove(1,1,'X'));
    }
    */


    /*
    makeMove() depends on game state to allow/reject moves, regardless of validity
     */
    @Test
    void testGameStateBlockers() throws Exception {
        TicTacToeGame testGame = new TicTacToeGame();

        testGame.setGameState(GameState.PLAYER_WIN); // simulate a player winning
        Assertions.assertFalse(testGame.makeMove(0,0,'X'));
        // even though theoretically valid move, because it believes a player has won it rejects the move

        testGame.setGameState(GameState.PLAYER_DRAW); // draw
        Assertions.assertFalse(testGame.makeMove(0,0,'X'));
        // same as before
    }

    /*
    Simply checking for various tie scenarios
     */
    @Test
    void testTieConditions() throws Exception {
        TicTacToeGame testGame = new TicTacToeGame();

        TicTacToeBoard testBoard = new TicTacToeBoard();
        Assertions.assertFalse(testGame.checkGameTie()); // false since game just started

        testBoard.fromString("X,X,X,O,X,O,X,O,X"); //full board, but X wins
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.checkGameTie());

        testBoard.fromString("O,X,O,X,O,O,X,O,X"); // tie game, no winner
        testGame.setBoard(testBoard);
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

        testBoard.fromString("X,X,X, , , , , , ");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O')); // worth testing that 'O' doesn't win
        // horizontal win in row 1

        testBoard.fromString(" , , ,X,X,X, , , ");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O'));
        // horizontal win in row 2

        testBoard.fromString(" , , , , , ,X,X,X");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O'));
        // horizontal win in row 3
    }

    @Test
    void testWinVertical(){
        TicTacToeGame testGame = new TicTacToeGame();
        TicTacToeBoard testBoard = new TicTacToeBoard();

        testBoard.fromString("X, , ,X, , ,X, , ");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O')); // worth testing that 'O' doesn't win
        // vertical win in column 1

        testBoard.fromString(" ,X, , ,X, , ,X, ");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O'));
        // vertical win in column 2

        testBoard.fromString(" , ,X, , ,X, , ,X");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O'));
        // vertical win in column 3
    }

    @Test
    void testWinDiagonal(){
        TicTacToeGame testGame = new TicTacToeGame();
        TicTacToeBoard testBoard = new TicTacToeBoard();

        testBoard.fromString("X, , , ,X, , , ,X");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O')); // worth testing that 'O' doesn't win
        // top left to bottom right win

        testBoard.fromString(" , ,X, ,X, ,X, , ");
        testGame.setBoard(testBoard);
        Assertions.assertTrue(testGame.validateWin('X'));
        Assertions.assertFalse(testGame.validateWin('O'));
        // bottom left to top right win
    }

    /*
    Testing for a win
     */
    @Test
    void testWinningGame() throws Exception {
        TicTacToeGame testGame = new TicTacToeGame();
        TicTacToeBoard testBoard = new TicTacToeBoard();

        // X has won
        testBoard.fromString("X,X,X, , , , , , ");
        testGame.setBoard(testBoard);

        Assertions.assertTrue(testGame.validateWin('X')); // winner is X
    }
}
