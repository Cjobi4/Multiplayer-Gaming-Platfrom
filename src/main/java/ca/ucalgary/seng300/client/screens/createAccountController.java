package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.identity.client.Network;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class createAccountController {
    public Button backButton;
    public Button continueButton;
    public Label errorField;
    public TextField userNameField;
    public PasswordField passwordField;

    @FXML
    protected void onContinueButtonClick(ActionEvent event) {
        String username = userNameField.getText() == null ? "" : userNameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (username.isEmpty()) {
            errorField.setText("Please enter a username.");
            return;
        }

        if (password.isEmpty()) {
            errorField.setText("Please enter a password.");
            return;
        }

        if (password.length() < 8 || password.length() > 18) {
            errorField.setText("Password must be 8-18 characters.");
            return;
        }

        if (username.contains("`") || username.contains("^")) {
            errorField.setText("Username cannot contain ` or ^.");
            return;
        }

        try {
            continueButton.setDisable(true);
            errorField.setText("Creating account...");

            Network.getInstance()
                    .queueRequest(Network.CREATE_ACCOUNT, new String[] {username, password})
                    .orTimeout(10, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> Platform.runLater(() -> {
                        continueButton.setDisable(false);

                        if (throwable != null) {
                            errorField.setText("Error: Could not create account.");
                            return;
                        }

                        if (Boolean.TRUE.equals(result)) {
                            errorField.setText("Failed to create account.");
                            return;
                        }

                        errorField.setText("");
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
                            Parent mainRoot = loader.load();

                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Scene mainScene = new Scene(mainRoot, 800, 600);
                            stage.setScene(mainScene);
                            stage.setTitle("Main Page");
                            stage.setResizable(false);
                            stage.show();
                        } catch (IOException e) {
                            errorField.setText("Error: Could not load main page.");
                        }
                    }));
        } catch (Exception e) {
            continueButton.setDisable(false);
            errorField.setText("Error: Could not create account.");
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
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load welcomePage.fxml. Check file path!");

        }

    }
}
