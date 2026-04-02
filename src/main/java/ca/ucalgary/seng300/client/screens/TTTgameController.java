package ca.ucalgary.seng300.client.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeGame;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;


import java.io.IOException;

public class TTTgameController {
    public Button gameOverButton;
    public Button backButton;
    public Button ttt00;
    public Button ttt01;
    public Button ttt02;
    public Button ttt10;
    public Button ttt11;
    public Button ttt12;
    public Button ttt20;
    public Button ttt21;
    public Button ttt22;

    TicTacToeGame current = new TicTacToeGame();
    Button grid[][] = {{ttt00, ttt01, ttt02},{ttt10, ttt11, ttt12},{ttt20, ttt21, ttt22}};

    @FXML
    protected void onGameOverButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameOverDisplay.fxml"));
            Parent gameOverRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene gameOverScene = new Scene(gameOverRoot, 800, 600);
            stage.setScene(gameOverScene);
            stage.setTitle("Game Over"); //Change stage title to reflect current scene
            stage.show();

            // Create Match Record (Not sure where to get player info, will be needed to make a match record)

            // MatchRecord matchRecord = new MatchRecord();

            // Update Leaderboard

            // TODO Need to communicate with someone on my team for this part, their code is hard for me to understand

        } catch (IOException e) {
            System.err.println("Error: Could not load gameOverDisplay.fxml. Check file path!");
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TTTopponentSelectPage.fxml"));
            Parent opponentRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene opponentScene = new Scene(opponentRoot, 800, 600);
            stage.setScene(opponentScene);
            stage.setTitle("Tic-Tac-Toe - Opponent Select"); //Change stage title to reflect current scene
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load TTTopponentSelectPage.fxml. Check file path!");
        }
    }

    private void updateBoard(){
       TicTacToeBoard board = current.getBoard();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!board.isCellEmpty(row, col)){
                    //TODO ill come back to this
                }
            }
            }
    }



}
