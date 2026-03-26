package ca.ucalgary.seng300.games;

import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TicTacToeGameTest {

    @BeforeAll
    static void setUp(){
        TicTacToeBoard testBoard = new TicTacToeBoard();
    }

}
