package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.identity.client.Network;
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
import org.w3c.dom.Text;

import java.io.IOException;

public class loginController {

    public Button loginButton;
    public Button createAccountButton;
    public Button backButton;
    public Label errorField;
    public TextField userNameField;
    public TextField passwordField;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws Exception {
        //Check if the field is empty or contains only whitespace
        if(userNameField.getText() == null || userNameField.getText().trim().isEmpty()) {
            errorField.setText("Please enter your username Address to login!");
        } else if (passwordField.getText() == null || passwordField.getText().trim().isEmpty()) {
            errorField.setText("Please enter your password Address to login!");
        } else {
            Network.getInstance().queueRequest(2, new String[] {userNameField.getText(), passwordField.getText()});
            errorField.setText("");
            switchSceneLargerScreen(event, "/fxml/mainPage.fxml", "Login Screen");
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
            stage.show();
        } catch (IOException e) {
            errorField.setText("Error: could not load " + fxmlPath);
        }
    }

}
