package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.shared.models.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class C4gameController {

    public Button gameOverButton;
    public Button backButton;
    public ScrollPane chatScrollPane;
    public VBox chatContainer;
    public TextField messageInput;
    public GridPane c4grid;


    Button[][] grid = new Button[6][7];

    public void initialize() {
        for (Node node : c4grid.getChildren()) {
            if (node instanceof Button button) {
                Integer row = GridPane.getRowIndex(button);
                Integer col = GridPane.getColumnIndex(button);

                if (row == null){
                    row = 0;
                }
                if (col == null) {
                    col = 0;
                }

                grid[row][col] = button;
            }
        }
    }


    @FXML
    protected void onSendMessage() {
        String text = messageInput.getText();
        if (text != null && !text.isEmpty()) {
            Message newMessage = new Message(text, "Player 1");

            ChatRegistry.getInstance().addMessage(newMessage);
            messageInput.clear();
            refreshChatDisplay();
        }
    }

    private void refreshChatDisplay() {
        chatContainer.getChildren().clear();

        for (Message m : ChatRegistry.getInstance().ListAll()) {
            Label msgLable = new Label(m.getSender() + ": " + m.getContent());

            msgLable.setWrapText(true);
            msgLable.setMaxWidth(750);
            msgLable.setStyle("-fx-background-color:  #F9EDE1;" +
                    "-fx-padding: 8;" +
                    "-fx-background-radius: 10;" +
                    "-fx-font-family: Dubai;" +
                    "-fx-text-fill: #866b59;" +
                    "-fx-margin: 5;");

            chatContainer.getChildren().add(msgLable);
        }
        chatScrollPane.setVvalue(1.0);
    }

    @FXML
    protected void onGameOverButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameOverDisplay.fxml"));
            Parent gameOverRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene gameOverScene = new Scene(gameOverRoot, 800, 600);
            stage.setScene(gameOverScene);
            stage.setTitle("Game Over"); //Change stage title to reflect current scene
            stage.setResizable(false);
            stage.show();

            // Create Match Record (Not sure where to get player info, will be needed to make a match record)

            // MatchRecord matchRecord = new MatchRecord();

            // Update Leaderboard

            // TODO Need to communicate with someone on my team for this part, their code is hard for me to understand

            ChatRegistry.getInstance().clearChat();

        } catch (IOException e) {
            System.err.println("Error: Could not load gameOverDisplay.fxml. Check file path!");
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/C4opponentSelectPage.fxml"));
            Parent opponentRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene opponentScene = new Scene(opponentRoot, 800, 600);
            stage.setScene(opponentScene);
            stage.setTitle("Connect 4 - Opponent Select"); //Change stage title to reflect current scene
            stage.setResizable(false);
            ChatRegistry.getInstance().clearChat();
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load C4opponentSelectPage.fxml. Check file path!");
        }
    }

    @FXML
    protected void onQuestionButtonClick(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play Connect-4");
        alert.setHeaderText("Game Instructions");

        String instructions = "1. The game is played on a vertical grid with 7 columns and 6 rows.\n\n"
                + "2. Players take turns dropping one of their colored discs into a column. The disc falls to the lowest available space in that column.\n\n"
                + "3. The first player to get 4 of their discs in a row (vertically, horizontally, or diagonally) wins the game!\n\n"
                + "4. If the grid fills up completely before anyone gets 4 in a row, the game ends in a draw.\n";

        alert.setContentText(instructions);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("pane");
        dialogPane.setMinHeight(400);
        dialogPane.setMinWidth(450);

        alert.showAndWait();

    }
}
