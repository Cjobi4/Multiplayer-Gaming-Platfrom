package ca.ucalgary.seng300.client.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class gameController {
    public Button gameOverButton;

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

        } catch (IOException e) {
            System.err.println("Error: Could not load gameOverDisplay.fxml. Check file path!");
        }

    }
}
