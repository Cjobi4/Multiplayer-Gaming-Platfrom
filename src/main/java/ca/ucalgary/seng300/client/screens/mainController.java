package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.shared.models.Game;
import ca.ucalgary.seng300.shared.models.Tag;
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
import java.util.Arrays;
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
        List<Tag> Connect4Tag = Arrays.asList(new Tag("Two player", "purple"), new Tag("Strategy", "Red"));
        List<Tag> TicTacToeTag = Arrays.asList(new Tag("Two Player", "purple"), new Tag("Classic", "Green"));
        games.add(new Game("101", "Connect 4", " Connect 4 is a two-player strategy game where players drop colored discs into a seven-column, six-row vertical grid.", Connect4Tag, null )); // have leaderboard as null for now
        games.add(new Game("102", "TicTacToe", " Tic-Tac-Toe (or Noughts and Crosses) is a classic, two-player paper-and-pencil game played on a \n" +
                "\n" +
                " grid. Players alternate placing their mark—\"X\" or \"O\"—in empty spaces, with the primary objective of being the first to align three of their marks in a horizontal, vertical, or diagonal row.", TicTacToeTag, null )); // have leaderboard as null for now
    }

    public Game findById(String id)
    {
        for (Game g : games)
        {
            if(g.getId().equalsIgnoreCase(id) || g.getTitle().toLowerCase().contains(id.toLowerCase())){
                return g;
            }

            for (Tag tag : g.getTags()){
                if(tag.matches(id)){
                    return g;
                }
            }
        }

        return null;
    }

    @FXML
    protected void handleSearch(){
        String input = searchField.getText().trim();

        if(input.isEmpty()){
            //clearDisplay(); // Function to be created at the end to clear display from all labels incase of past search
            gameTitleLabel.setText("Please enter a game title");
            return;
        }

        Game game = findById(input);

        if(game != null){
            //displayGame(game); // This will be the function that connects to the labels allowing to display on labels
        }
        else{
            //clearDisplay();
            gameTitleLabel.setText("Game not found");
        }

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
