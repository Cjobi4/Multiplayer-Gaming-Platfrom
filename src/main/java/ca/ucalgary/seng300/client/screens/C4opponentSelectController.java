package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.shared.models.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class C4opponentSelectController implements Initializable {
    public Button opponentSelectedButton;
    public Button backButton;
    public Button byPassButton;


    @FXML
    public TableView<LeaderboardEntry> opponentList;

    @FXML
    public TableColumn<LeaderboardEntry, String> playerColumn;

    @FXML
    public TableColumn<LeaderboardEntry, String> winsColumn;

    @FXML
    public TableColumn<LeaderboardEntry, String> matchesColumn;

    private final ObservableList<LeaderboardEntry> observableData = FXCollections.observableArrayList();

    @FXML
    protected void onOpponentSelectedButtonClick(ActionEvent event) {
        LeaderboardEntry selectedOpponent = opponentList.getSelectionModel().getSelectedItem();

        if(selectedOpponent == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No opponent selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a opponent to play");
            alert.showAndWait();
            return;
        }

        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/C4gamePage.fxml"));
            Parent gameRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene gameScene = new Scene(gameRoot, 800, 600);
            stage.setScene(gameScene);
            stage.setTitle("Play Connect 4!"); //Change stage title to reflect current scene
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load C4gamePage.fxml. Check file path!");
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
            Parent MainRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene MainScene = new Scene(MainRoot, 800, 600);
            stage.setScene(MainScene);
            stage.setTitle("Main Page"); //Change stage title to reflect current scene
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load mainPage.fxml. Check file path!");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        winsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getWins())));

        matchesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getMatches())));

        opponentList.setItems(observableData);
        opponentList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        loadConnect4Leaderboard(false);
    }

    private void loadConnect4Leaderboard(boolean onlineOnly) {
        observableData.clear();
        opponentSelectedButton.setDisable(true);

        Task<List<LeaderboardEntry>> task = new Task<>() {
            @Override
            protected List<LeaderboardEntry> call() throws Exception {
                List<LeaderboardEntry> leaderboard = LeaderBoard.getLeaderboard(GameType.CONNECT4);

                if (leaderboard == null) {
                    return new ArrayList<>();
                }

                if (!onlineOnly) {
                    return leaderboard;
                }

                Network.getInstance().queueRequest(Network.GET_ONLINE_PLAYERS, null).get();

                List<LeaderboardEntry> filteredLeaderboard = new ArrayList<>();

                for (LeaderboardEntry entry : leaderboard) {
                    if (PlayerOnline(entry.getUsername())) {
                        filteredLeaderboard.add(entry);
                    }
                }

                return filteredLeaderboard;
            }
        };

        task.setOnSucceeded(e -> {
            observableData.setAll(task.getValue());
            opponentSelectedButton.setDisable(false);
        });

        task.setOnFailed(e -> {
            opponentSelectedButton.setDisable(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Leaderboard Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load the Connect 4 leaderboard.");
            alert.showAndWait();

            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    protected void onByPassButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/C4gamePage.fxml"));
            Parent MainRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene MainScene = new Scene(MainRoot, 800, 600);
            stage.setScene(MainScene);
            stage.setTitle("Connect 4"); //Change stage title to reflect current scene
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load C4GamePage.fxml. Check file path!");
        }

    }

    private boolean PlayerOnline(String username) {
        return PlayerRegistry.getInstance().findByName(username) != null;
    }

}
