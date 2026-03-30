package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.shared.models.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mainController {

    public Button gameSelectButton;
    public Button logOutButton;
    public Button MatchMakeButton;
    public RadioButton connect4Select;
    public RadioButton TicTacToeSelect;
    public Label errorField;
    public Label gameTitleLabel;
    public Label gameDescriptionLabel;
    public Label gameIdLabel;
    public Label gameTagsLabel;
    public TextField searchField;

    private final List<Game> games = new ArrayList<>();

    private void sampleData(){

    }



    @FXML
    protected void onSelectButtonSelected(ActionEvent event) {
        errorField.setText("");
    }


    @FXML
    protected void onGameSelectButtonClick(ActionEvent event) {
        if (connect4Select.isSelected()) {
            switchScene(event, "/fxml/C4OpponentSelectPage.fxml", "Connect 4 - Select Opponent");
            errorField.setText("");
        } else if (TicTacToeSelect.isSelected()) {
            switchScene(event, "/fxml/TTTOpponentSelectPage.fxml", "Tic-Tac-Toe - Select Opponent");
            errorField.setText("");
        } else {
            errorField.setText("Please select a game first!");
        }
    }

    @FXML
    protected void onMatchMakeButtonClick(ActionEvent event) {
        if (connect4Select.isSelected()) {
            switchScene(event, "/fxml/C4OpponentSelectPage.fxml", "Connect 4 - Select Opponent");
            errorField.setText("");
        } else if (TicTacToeSelect.isSelected()) {
            switchScene(event, "/fxml/TTTOpponentSelectPage.fxml", "Tic-Tac-Toe - Select Opponent");
            errorField.setText("");
        } else {
            errorField.setText("Please select a game first!");
        }
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            errorField.setText("Error loading page: " + fxmlPath);
        }
    }



    @FXML
    protected void handleLogout(ActionEvent event) {
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
    }


    @FXML
    protected void DisplayGameList(ActionEvent event)
    {
       List<Game> GameList = GameRegistry.getInstance().ListAll();

       for (Game game : GameList)
       {
            // TODO: Create some sort of JavaFX list view (or if you have on already use it here)

           // Examples for how to pull data (see game constructor for all available info)

           String title = game.getTitle();
           String description = game.getDescription();

       }

    }
}
