package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.shared.models.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load C4gamePage.fxml. Check file path!");
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
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

        // loadConnect4Leaderboard(false);
    }

}
