package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.shared.models.Game;
import ca.ucalgary.seng300.shared.models.ActivePlayer;
import ca.ucalgary.seng300.shared.models.Tag;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.control.MenuItem;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    public ImageView thumbnail;
    public MenuItem aboutTheApp;
    public MenuItem viewDeveloperCredits;
    public MenuItem viewPlayerStats;

    private GameRegistry games = GameRegistry.getInstance();
    ToggleGroup group = new ToggleGroup();

    @FXML
    public void initialize() {

        if (games.ListAll().isEmpty()) {
            sampleData();
        }

        loadCombinedLeaderboard();
        DisplayGameList();

        // Allow Challenges to be received on this page
        try {
            // Register this UI screen to listen for challenges
            Network.getInstance().setChallengeListener((challengerName, gameType) -> {

                // Force the UI drawing onto the JavaFX thread:
                Platform.runLater(() -> {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Incoming Challenge!");
                    alert.setHeaderText(challengerName + " has challenged you to " + gameType.toUpperCase() + "!");
                    alert.setContentText("Would you like to accept this challenge?");

                    ButtonType buttonAccept = new ButtonType("Accept");
                    ButtonType buttonDecline = new ButtonType("Decline", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(buttonAccept, buttonDecline);

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                    dialogPane.getStyleClass().add("pane");

                    Button accBtn = (Button) dialogPane.lookupButton(buttonAccept);
                    Button decBtn = (Button) dialogPane.lookupButton(buttonDecline);

                    if (accBtn != null) {
                        accBtn.getStyleClass().add("basic-button");
                    }

                    if(decBtn != null) {
                        decBtn.getStyleClass().add("basic-button");
                    }

                    Optional<ButtonType> result = alert.showAndWait();

                    try {
                        if (result.isPresent() && result.get() == buttonAccept) {

                            // ACCEPT: Queue the request and WAIT for the server's final confirmation
                            Network.getInstance().queueRequest(Network.RECEIVE_CHALLENGE, new String[]{"1"})
                                    .thenAccept(res -> {
                                        int finalResult = (Integer) res;

                                        // Move BACK to the JavaFX thread to load the new scene
                                        Platform.runLater(() -> {
                                            if (finalResult == Network.MATCH_ACCEPTED) { // 14

                                                // Determine which FXML to load based on the gameType string
                                                String fxmlPrefix = gameType.equalsIgnoreCase("ttt") ? "TTT" : "C4";
                                                String fxmlFile = "/fxml/" + fxmlPrefix + "gamePage.fxml";

                                                try {
                                                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                                                    Parent root = loader.load();

                                                    // Get the current stage using an existing UI node (since we have no ActionEvent)
                                                    Stage stage = (Stage) gameSelectButton.getScene().getWindow();

                                                    Scene scene = new Scene(root, 800, 600);
                                                    stage.setScene(scene);
                                                    stage.setTitle("Playing " + gameType.toUpperCase());
                                                    stage.setResizable(false);
                                                    stage.show();

                                                } catch (IOException e) {
                                                    errorField.setText("Error loading game page.");
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                errorField.setText("Opponent disconnected or match failed.");
                                            }
                                        });
                                    });
                        } else {
                            // DECLINE
                            Network.getInstance().queueRequest(Network.RECEIVE_CHALLENGE, new String[]{"0"});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        } catch (Exception e) {
            System.err.println("Failed to connect Network listener: " + e.getMessage());
        }
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

    public HBox showLeaderboardRow(int rank, LeaderboardEntry entry){
        Label rankLabel = new Label("#" + rank);
        Label nameLabel = new Label(entry.getUsername());
        Label winsLabel = new Label(entry.getWins() + " W");
        Label matchesLabel = new Label(entry.getMatches() + " M");

        rankLabel.setMinWidth(30);
        winsLabel.setMinWidth(40);
        matchesLabel.setMinWidth(40);

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

        rankLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #6f5a52;");
        nameLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #6f5a52;");
        winsLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #6f5a52;");
        matchesLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #6f5a52;");

        return rowBox;
    }

    @FXML
    private void onAboutTheAppButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Application");
        alert.setHeaderText("Trainwreck!");

        String information = "This is trainwreck weeeewoooooo";

        alert.setContentText(information);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("pane");
        dialogPane.setMinHeight(400);
        dialogPane.setMinWidth(450);

        alert.showAndWait();

    }

    @FXML
    private void onViewPlayerStatsClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Application");
        alert.setHeaderText("Trainwreck!");

        String information = "This is trainwreck weeeewoooooo";

        alert.setContentText(information);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("pane");
        dialogPane.setMinHeight(400);
        dialogPane.setMinWidth(450);

        alert.showAndWait();


    }

    @FXML
    private void onViewDeveloperCreditsClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developers");
        alert.setHeaderText("Trainwreck Developers!");

        String information = "This app was developed by weewoo";

        alert.setContentText(information);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("pane");
        dialogPane.setMinHeight(400);
        dialogPane.setMinWidth(450);

        alert.showAndWait();

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
        gameTitleLabel.setFont(new Font("Dubai Medium", 20));
        gameTitleLabel.setTextFill(javafx.scene.paint.Color.web("#866B59"));

        gameDescriptionLabel.setText("Description: " + game.getDescription());
        gameDescriptionLabel.setFont(new Font("Dubai Medium", 13));
        gameDescriptionLabel.setTextFill(javafx.scene.paint.Color.web("#866B59"));

        gameIdLabel.setText("Game ID: " + game.getId());
        gameIdLabel.setFont(new Font("Dubai Medium", 15));
        gameIdLabel.setTextFill(javafx.scene.paint.Color.web("#866B59"));

        gameTagsLabel.setText("Game Tag: " + formatTags(game.getTags()));
        gameTagsLabel.setFont(new Font("Dubai Medium", 15));
        gameTagsLabel.setTextFill(javafx.scene.paint.Color.web("#866B59"));

        if (game.getId() == "102"){
            setThumbnailTTT();
        } else if (game.getId() == "101"){
            setThumbnailC4();
        }
    }

    private void clearDisplay(){
        gameTitleLabel.setText("");
        gameDescriptionLabel.setText("");
        gameIdLabel.setText("");
        gameTagsLabel.setText("");
    }


    private void setThumbnailTTT() {
        Image image = new Image(getClass().getResource("/images/TTTthumbnail.jpg").toExternalForm());
        thumbnail.setImage(image);
    }

    private void setThumbnailC4() {
        Image image = new Image(getClass().getResource("/images/C4thumbnail.jpg").toExternalForm());
        thumbnail.setImage(image);
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
    protected void onMatchMakeButtonClick(ActionEvent event) throws Exception {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        if (selected != null)
        {
            Game selectedGame = findGame(selected.getText());
            if (selectedGame != null) {

                if (selectedGame.getTitle().equals("TicTacToe"))
                {
                    errorField.setText("Finding a match...");
                    String joinedTTT = Network.getInstance().queueRequest(Network.JOIN_TTT_QUEUE, null).get().toString();
                    showMatchFoundPopup(joinedTTT, selectedGame, event);
                }
                else
                {
                    errorField.setText("Finding a match...");
                    String joinedC4  = Network.getInstance().queueRequest(Network.JOIN_C4_QUEUE, null).get().toString();
                    showMatchFoundPopup(joinedC4, selectedGame, event);
                }

            } else {
                errorField.setText("Could not find the selected game.");
            }
        }
        else
        {
            errorField.setText("Please select a game first!");
        }
    }

    //Popoup that appears when the user is matched with another opponenet.
    private void showMatchFoundPopup(String opponentName, Game game, ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Match Found!");
        alert.setHeaderText("Opponent found: " + opponentName);
        alert.setContentText("Do you want to accept this match for " + game.getTitle() + "?");


        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType declineButton = new ButtonType("Decline", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(acceptButton, declineButton);


        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("pane");


        Button accBtn = (Button) dialogPane.lookupButton(acceptButton);
        Button decBtn = (Button) dialogPane.lookupButton(declineButton);


        if(accBtn != null) {
            accBtn.getStyleClass().add("basic-button");
        }
        if (decBtn != null) {
            decBtn.getStyleClass().add("basic-button");
        }


        alert.showAndWait().ifPresent(type -> {
            if (type == acceptButton) {
                try {
                    System.out.println("Accept button hit");
                    int result = (Integer) Network.getInstance().queueRequest(Network.RESPOND_QUEUE, new String[]{"1"}).get();
                    System.out.println(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String fxmlFile = "/fxml/" + game.getFxmlPath() + "GamePage.fxml";
                switchScene(event, fxmlFile, "Playing " + game.getTitle());
            } else {
                try {
                    int result = (Integer) Network.getInstance().queueRequest(Network.RESPOND_QUEUE, new String[]{"0"}).get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                errorField.setText("Match declined.");
            }
        });
    }


    @FXML
    public void updateInfo(ActionEvent event) { //updates the Radio Buttons
        RadioButton selected = (RadioButton) group.getSelectedToggle();

        if (selected != null) {
            Game selectedGame = findGame(selected.getText());
            displayGame(selectedGame);
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
           rb.setOnAction(this::updateInfo); //updates the info

       }

    }
}
