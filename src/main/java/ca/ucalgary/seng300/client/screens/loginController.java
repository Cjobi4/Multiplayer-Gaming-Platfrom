package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.shared.models.ActivePlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class loginController {

    public Button loginButton;
    public Button createAccountButton;
    public Button backButton;
    public Label errorField;
    public TextField userNameField;
    public TextField passwordField;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = userNameField.getText() == null ? "" : userNameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (username.isEmpty()) {
            errorField.setText("Please enter your username to login!");
            return;
        }

        if (password.isEmpty()) {
            errorField.setText("Please enter your password to login!");
            return;
        }

        try {
            loginButton.setDisable(true);
            errorField.setText("Logging in...");

            Network.getInstance()
                    .queueRequest(Network.LOGIN, new String[] {username, password})
                    .orTimeout(10, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> Platform.runLater(() -> {
                        System.out.println("Test 1");
                        loginButton.setDisable(false);
                        if (throwable != null) {
                            System.out.println("Test 2");
                            errorField.setText("Error: Could not log in.");
                            return;
                        }

                        if (Boolean.TRUE.equals(result)) {
                            System.out.println("Test 3");
                            errorField.setText("Invalid username or password.");
                            return;
                        }
                        System.out.println("Test 4");
                        errorField.setText("");
                        switchSceneLargerScreen(event, "/fxml/mainPage.fxml", "Main Page");
                    }));

        } catch (Exception e) {
            loginButton.setDisable(false);
            errorField.setText("Error: Could not log in.");
        }
    }


    @FXML
    protected void onCreateAccountButtonClick(ActionEvent event) {
        switchSceneSmallerScreen(event, "/fxml/createAccountPage.fxml", "Create Account");
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        switchSceneSmallerScreen(event, "/fxml/welcomePage.fxml", "Welcome!");
    }

    private void switchSceneLargerScreen(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource(fxmlPath)));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            errorField.setText("Error: could not load " + fxmlPath);
        }
    }

    private void switchSceneSmallerScreen(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource(fxmlPath)));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            errorField.setText("Error: could not load " + fxmlPath);
        }
    }

    @FXML
    protected void onByPassButtonClick(ActionEvent event) {
        switchSceneLargerScreen(event, "/fxml/mainPage.fxml", "Main");
    }


}
