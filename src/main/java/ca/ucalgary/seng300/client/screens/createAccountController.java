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

public class createAccountController {
    public Button backButton;
    public Button continueButton;

    @FXML
    protected void onContinueButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
            Parent MainRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene MainScene = new Scene(MainRoot, 800, 600);
            stage.setScene(MainScene);
            stage.setTitle("Main Page"); //Change stage title to reflect current scene
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load mainPage.fxml. Check file path!");
        }
    }



    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/welcomePage.fxml"));
            Parent welcomeRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene welcomeScene = new Scene(welcomeRoot, 600, 400);
            stage.setScene(welcomeScene);
            stage.setTitle("Welcome!"); //Change stage title to reflect current scene
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load welcomePage.fxml. Check file path!");

        }

    }
}
