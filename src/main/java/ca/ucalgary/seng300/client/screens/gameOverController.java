package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.shared.models.ActivePlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class gameOverController {

    public Button returnToMainButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label scoreValueLabel;

    @FXML
    private ListView<String> rankingListView;

    private GameType currentGameType;

    @FXML
    public void initialize() {
        usernameLabel.setText(ActivePlayer.getInstance().getUsername());
        scoreValueLabel.setText("");
        rankingListView.getItems().clear();
    }

    public void setGameType(GameType gameType){
        this.currentGameType = gameType;
        //loadGameOverData();
    }

    @FXML
    protected void onReturnToMainButtonClick(ActionEvent event) {
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
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load mainPage.fxml. Check file path!");
        }
    }


}
