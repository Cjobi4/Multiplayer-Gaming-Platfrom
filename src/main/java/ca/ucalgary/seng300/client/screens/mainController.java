package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.client.components.LeaderBoardMock;
import ca.ucalgary.seng300.client.components.LeaderBoardRows;
import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.shared.models.Game;
import ca.ucalgary.seng300.shared.models.Tag;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class mainController {

    public Button gameSelectButton;
    public Button logOutButton;
    public Button MatchMakeButton;
    public Label errorField;
    public Label gameTitleLabel;
    public Label gameDescriptionLabel;
    public Label gameIdLabel;
    public Label gameTagsLabel;
    public TextField searchField;
    public VBox leaderboardBox;
    public VBox gameOptions;

    private GameRegistry games = GameRegistry.getInstance();
    ToggleGroup group = new ToggleGroup();

    @FXML
    public void initialize() {

        if (games.ListAll().isEmpty()) {
            sampleData();
        }

        loadCombinedLeaderboard();
        DisplayGameList();
    }

    private void loadCombinedLeaderboard(){
        leaderboardBox.getChildren().clear();

        Task<List<LeaderboardEntry>> task = new Task<>() {
            @Override
            protected List<LeaderboardEntry> call() {
                return LeaderBoard.getLeaderboard(null);
            }
        };

        task.setOnSucceeded(e -> renderLeaderboard(task.getValue()));

        task.setOnFailed(e -> {
            leaderboardBox.getChildren().clear();

            Label errorLabel = new Label("Failed to load leaderboard");
            errorLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: red;");
            leaderboardBox.getChildren().add(errorLabel);

            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
        });


        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void renderLeaderboard(List<LeaderboardEntry> leaderboard){
        leaderboardBox.getChildren().clear();

        for (int i = 0; i < leaderboard.size(); i++) {
            LeaderboardEntry entry = leaderboard.get(i);
            leaderboardBox.getChildren().add(showLeaderboardRow(i+1, entry));
        }

    }

    public HBox showLeaderboardRow(int rank, LeaderboardEntry leaderboardEntry){
        Label rankLabel = new Label("#" + rank);
        Label nameLabel = new Label(row.getPlayerName());
        Label winsLabel = new Label(row.getWins() + " W");
        Label matchesLabel = new Label(row.getMatches() + " M");

        rankLabel.setMinWidth(45);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        HBox rowBox = new HBox(15);
        rowBox.setAlignment(Pos.CENTER_LEFT);
        rowBox.getChildren().addAll(rankLabel, nameLabel, winsLabel, matchesLabel);

        rowBox.setStyle("""
            -fx-background-color: #f3c1cf;
            -fx-background-radius: 10;
            -fx-padding: 10;
        """);

        rankLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #6f5a52;");
        nameLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #6f5a52;");
        winsLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #6f5a52;");
        matchesLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #6f5a52;");

        return rowBox;
    }






/// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void sampleData(){
        List<Tag> Connect4Tag = Arrays.asList(new Tag("Two player", "purple"), new Tag("Strategy", "Red"));
        List<Tag> TicTacToeTag = Arrays.asList(new Tag("Two Player", "purple"), new Tag("Classic", "Green"));
        games.register(new Game("101", "Connect 4", "A two-player game where the first to connect four discs in a row wins.", Connect4Tag, "C4")); // have leaderboard as null for now
        games.register(new Game("102", "TicTacToe", "A two-player game where the first to get three marks in a row wins.", TicTacToeTag, "TTT"));
    }

    @FXML
    protected void handleSearch(){
        String input = searchField.getText().trim();

        if(input.isEmpty()){
            clearDisplay(); // Function to be created at the end to clear display from all labels incase of past search
            gameTitleLabel.setText("Please enter a game title");
            return;
        }

        Game game = findGame(input);

        if(game != null){
            displayGame(game); // This will be the function that connects to the labels allowing to display on labels
        }
        else{
            clearDisplay();
            gameTitleLabel.setText("Game not found");
        }

    }

    private Game findGame(String query) {
        if (query == null) {
            return null;
        }

        String normalizedQuery = query.trim().toLowerCase();

        if (normalizedQuery.isEmpty()) {
            return null;
        }

        for (Game game : games.ListAll()) {
            if (game.getTitle() != null && game.getTitle().toLowerCase().contains(normalizedQuery)) {
                return game;
            }

            if (game.getId() != null && game.getId().equalsIgnoreCase(normalizedQuery)) {
                return game;
            }

            for (Tag tag : game.getTags()) {
                if (tag.getLabel() != null && tag.getLabel().toLowerCase().contains(normalizedQuery)) {
                    return game;
                }
            }
        }

        return null;
    }

    private String formatTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) return "No tags";

        StringBuilder sb = new StringBuilder();

        for (Tag tag : tags) {
            sb.append(tag.getLabel()).append(", ");
        }

        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    private void displayGame(Game game){
        gameTitleLabel.setText("Game: " + game.getTitle());
        gameDescriptionLabel.setText("Description: " + game.getDescription());
        gameIdLabel.setText("Game ID: " + game.getId());
        gameTagsLabel.setText("Game Tag: " + formatTags(game.getTags()));
    }

    private void clearDisplay(){
        gameTitleLabel.setText("");
        gameDescriptionLabel.setText("");
        gameIdLabel.setText("");
        gameTagsLabel.setText("");
    }




/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    protected void onSelectButtonSelected(ActionEvent event) {
        errorField.setText("");
    }


    @FXML
    protected void onGameSelectButtonClick(ActionEvent event) {

        RadioButton selected = (RadioButton) group.getSelectedToggle();

        if (selected != null)
        {
            Game selectedGame = findGame(selected.getText());

            if (selectedGame != null) {
                switchScene(event, "/fxml/" + selectedGame.getFxmlPath() + "opponentSelectPage.fxml", selected.getText() + " - Select Opponent");
                errorField.setText("");
            } else {
                errorField.setText("Could not find the selected game.");
            }
        }
        else
        {
            errorField.setText("Please select a game first!");
        }
    }

    @FXML
    protected void onMatchMakeButtonClick(ActionEvent event) {

        RadioButton selected = (RadioButton) group.getSelectedToggle();

        if (selected != null)
        {
            Game selectedGame = findGame(selected.getText());

            if (selectedGame != null) {
                switchScene(event, "/fxml/" + selectedGame.getFxmlPath() + "opponentSelectPage.fxml", selected.getText() + " - Select Opponent");
                errorField.setText("");
            } else {
                errorField.setText("Could not find the selected game.");
            }
        }
        else
        {
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
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            errorField.setText("Error loading page: " + fxmlPath);
        }
    }



    @FXML
    protected void handleLogout(ActionEvent event) {
        logOutButton.setDisable(true);
        errorField.setText("Logging out...");

        try {
            Network.getInstance()
                    .queueRequest(Network.LOGOUT, null)
                    .orTimeout(5, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> Platform.runLater(() -> {
                        if (throwable != null || !Boolean.TRUE.equals(result)) {
                            System.err.println("Warning: server logout was not confirmed.");
                        }

                        switchToLoginScene(event);
                    }));
        } catch (Exception e) {
            switchToLoginScene(event);
        }
    }

    private void switchToLoginScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginPage.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene loginScene = new Scene(loginRoot, 600, 400);
            stage.setScene(loginScene);
            stage.setTitle("Login Screen");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            errorField.setText("Error loading page: /fxml/loginPage.fxml");
            logOutButton.setDisable(false);
        }
    }


    @FXML
    protected void DisplayGameList()
    {
       List<Game> GameList = GameRegistry.getInstance().ListAll();
       gameOptions.getChildren().clear();

       for (Game game : GameList)
       {
           RadioButton rb = new RadioButton(game.getTitle());
           rb.setToggleGroup(group);
           gameOptions.getChildren().add(rb);

       }

    }
}
