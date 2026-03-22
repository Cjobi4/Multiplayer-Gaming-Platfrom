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

public class mainController {

    public Button gameSelectButton;
    public Button backButton;
    public Button logOutButton;

    @FXML
    protected void onGameSelectButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/opponentSelectPage.fxml"));
            Parent opponentRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene opponentScene = new Scene(opponentRoot, 800, 600);
            stage.setScene(opponentScene);
            stage.setTitle("Opponent Select"); //Change stage title to reflect current scene
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load opponentSelectPage.fxml. Check file path!");
        }

    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginPage.fxml"));
            Parent loginRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene loginScene = new Scene(loginRoot, 600, 400);
            stage.setScene(loginScene);
            stage.setTitle("Login Screen"); //Change stage title to reflect current scene
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load loginPage.fxml. Check file path!");

        }

    }}
