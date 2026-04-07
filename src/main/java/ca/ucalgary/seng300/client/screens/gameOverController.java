package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.client.components.LeaderBoardRows;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.shared.models.ActivePlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        String activeUsername = getLocalUsername();

        if (activeUsername == null || activeUsername.isEmpty()) {
            activeUsername = "Unknown User";
        }

        usernameLabel.setText(activeUsername);
        scoreValueLabel.setText("Loading...");
        rankingListView.getItems().clear();
        rankingListView.getItems().add("Loading rankings...");
//        usernameLabel.setText(ActivePlayer.getInstance().getUsername());
//        scoreValueLabel.setText("");
//        rankingListView.getItems().clear();
    }

    public void setGameType(GameType gameType){
        this.currentGameType = gameType;
        loadGameOverData();
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
            String activeUsername = getLocalUsername();

            if (activeUsername == null || activeUsername.isEmpty()) {
                activeUsername = "Unknown User";
            }

            usernameLabel.setText(activeUsername);
            scoreValueLabel.setText("0 Wins");
            rankingListView.getItems().clear();
            rankingListView.getItems().add("Failed to load rankings");

            if (task.getException() != null) {
                task.getException().printStackTrace();

            }
//            usernameLabel.setText(ActivePlayer.getInstance().getUsername());
//            scoreValueLabel.setText("0 Wins");
//            rankingListView.getItems().clear();
//            rankingListView.getItems().add("Failed to load rankings");
//
//
//            if (task.getException() != null) {
//                task.getException().printStackTrace();
//            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void renderGameOverData(List<LeaderboardEntry> leaderboard) {
        rankingListView.getItems().clear();

        String localUsername = getLocalUsername();

        if(localUsername == null || localUsername.isEmpty()) {
            localUsername = "Unknown User";
        }

        usernameLabel.setText(localUsername);

        if(leaderboard == null || leaderboard.isEmpty()){
            scoreValueLabel.setText("0 Wins");
            rankingListView.getItems().add("No leaderboard data");
            return;
        }

        ObservableList<String> listRows = FXCollections.observableArrayList();
        int userWins = 0;
        boolean found = false;
        for(int i = 0; i < leaderboard.size(); i++){
            LeaderboardEntry entry = leaderboard.get(i);

            if(entry.getUsername().equals(localUsername)){
                listRows.add("#" + (i + 1) + " " + entry.getUsername()
                        + " - " + entry.getWins() + " wins"
                        + " - " + entry.getMatches() + " matches");
                userWins = entry.getWins();
                found = true;
                break;
            }
        }

        if (!found) {
            listRows.add(localUsername + " - no ranking found");
        }

        scoreValueLabel.setText(userWins + " Wins");
        rankingListView.setItems(listRows);
    }

    private String getLocalUsername() {
        return ActivePlayer.getInstance().getUsername();
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
