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

import java.io.IOException;

public class welcomeController {

    public Button welcomeButton;
    public Button createAccountButton;
    public TextField IPAdressTextField;
    public Label errorField;

    //From Welcome Page -> Login page
    @FXML
    protected void onWelcomeButtonClick(ActionEvent event) throws Exception {
        String ipInput = IPAdressTextField.getText().trim();
        if(ipInput.isEmpty()) {
            errorField.setText("Please enter an IP Address to login!");
            return;
        }

        if(isValidIP(ipInput)) {
            Network.getInstance(ipInput, 14001);
            errorField.setText("");
            switchScene(event, "/fxml/loginPage.fxml", "Login Screen");
        }
        else {
            errorField.setText("Invalid IP.");
        }
    }

    private boolean isValidIP(String ip) {
        String[] parts = ip.split("\\.");
        if(parts.length != 4) {
            return false;
        }
        try{
            for(String part : parts) {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @FXML
    protected void onCreateAccountButtonClick(ActionEvent event) throws Exception {
        String ipInput = IPAdressTextField.getText().trim();
        if(ipInput.isEmpty()) {
            errorField.setText("Please enter an IP Address to login!");
            return;
        }

        if(isValidIP(ipInput)) {
            Network.getInstance(ipInput, 14001);
            errorField.setText("");
            switchScene(event, "/fxml/createAccountPage.fxml", "Create Account");
        }
        else {
            errorField.setText("Invalid IP.");
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
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            errorField.setText("Error: could not load " + fxmlPath);
        }
    }

    @FXML
    private void onByPassButtonClick(ActionEvent event) {
        switchScene(event, "/fxml/loginPage.fxml", "Login Page");
    }
}
