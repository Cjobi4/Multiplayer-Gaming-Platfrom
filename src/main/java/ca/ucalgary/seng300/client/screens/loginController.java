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

public class loginController {

    public Button loginButton;
    public Button createAccountButton;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
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
    protected void onCreateAccountButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createAccountPage.fxml"));
            Parent createAccountRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene createAccountScene = new Scene(createAccountRoot, 600, 400);
            stage.setScene(createAccountScene);
            stage.setTitle("Create Account"); //Change stage title to reflect current scene
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load createAccountPage.fxml. Check file path!");

        }

    }

}
