package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.shared.models.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class C4gameController {

    public Button gameOverButton;
    public Button backButton;
    public ScrollPane chatScrollPane;
    public VBox chatContainer;
    public TextField messageInput;
    public Button c00;
    public Button c01;
    public Button c02;
    public Button c03;
    public Button c04;
    public Button c05;
    public Button c06;
    public Button c10;
    public Button c11;
    public Button c12;
    public Button c13;
    public Button c14;
    public Button c15;
    public Button c16;
    public Button c20;
    public Button c21;
    public Button c22;
    public Button c23;
    public Button c24;
    public Button c25;
    public Button c26;

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
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load C4opponentSelectPage.fxml. Check file path!");
        }
    }
}
