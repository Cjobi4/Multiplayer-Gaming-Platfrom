package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.client.components.LeaderBoardRows;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.shared.models.ActivePlayer;
import javafx.concurrent.Task;
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
import java.util.List;

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

    private void  loadGameOverData(){
        if(currentGameType == null){
            rankingListView.getItems().clear();
            rankingListView.getItems().add("No Type determined");
            scoreValueLabel.setText("0 Wins");
            return;
        }

        Task<List<LeaderboardEntry>> task = new Task<>(){
            @Override
            protected List<LeaderboardEntry> call() {
                return LeaderBoard.getLeaderboard(currentGameType);
            }
        };

        task.setOnSucceeded(event -> renderGameOverData(task.getValue()));

        task.setOnFailed(event -> {
            usernameLabel.setText(ActivePlayer.getInstance().getUsername());
            scoreValueLabel.setText("0 Wins");
            rankingListView.getItems().clear();
            rankingListView.getItems().add("Failed to load rankings");


            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void renderGameOverData(List<LeaderboardEntry> leaderboard) {

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
