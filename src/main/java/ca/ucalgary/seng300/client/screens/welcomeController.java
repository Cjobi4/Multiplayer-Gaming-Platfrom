package ca.ucalgary.seng300.client.screens;

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

public class welcomeController {

    public Button welcomeButton;
    public Button createAccountButton;
    public TextField IPAdressTextField;
    public Label errorField;

    @FXML
    protected void onWelcomeButtonClick(ActionEvent event) {
        //Check if the field is empty or contains only whitespace
        if(IPAdressTextField.getText() == null || IPAdressTextField.getText().trim().isEmpty()) {
            errorField.setText("Please enter a valid IP Address to login!");
        } else {
                errorField.setText("");
                switchScene(event, "/fxml/loginPage.fxml", "Login Screen");
        }
    }

    @FXML
    protected void onCreateAccountButtonClick(ActionEvent event) {
        if(IPAdressTextField.getText() == null || IPAdressTextField.getText().trim().isEmpty()) {
            errorField.setText("Please enter a valid IP Address to create an account!");
        } else {
            errorField.setText("");
            switchScene(event, "/fxml/createAccountPage.fxml", "Create Account");
        }

    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
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
