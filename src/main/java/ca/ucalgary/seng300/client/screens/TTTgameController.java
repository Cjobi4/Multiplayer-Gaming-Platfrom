package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.identity.client.Network;
import ca.ucalgary.seng300.core.identity.client.Session;
import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.games.GameState;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.shared.models.ActivePlayer;
import ca.ucalgary.seng300.shared.models.Message;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeGame;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;
import ca.ucalgary.seng300.games.GameState;
import javafx.util.Duration;

import java.io.IOException;

public class TTTgameController {
    public Button backButton;
    public Button ttt00;
    public Button ttt01;
    public Button ttt02;
    public Button ttt10;
    public Button ttt11;
    public Button ttt12;
    public Button ttt20;
    public Button ttt21;
    public Button ttt22;
    public Text turnDisplay;
    public ScrollPane chatScrollPane;
    public VBox chatContainer;
    public TextField messageInput;

    TicTacToeGame current = new TicTacToeGame();
    Button[][] grid;

    private Timeline chatRefreshTimeline;
    private int lastChatSize = -1;

    public void initialize() {
        grid = new Button[][]{{ttt00, ttt01, ttt02},{ttt10, ttt11, ttt12},{ttt20, ttt21, ttt22}};

        messageInput.setOnAction(event -> onSendMessage());
        refreshChatDisplay();
        startChatWatcher();
    }

    @FXML
    protected void onSendMessage() {
        String text = messageInput.getText();
        if (text != null && !text.trim().isEmpty()) {
            //Message newMessage = new Message(text, "Player 1");
            String sender = ActivePlayer.getInstance().getUsername();

            if(sender == null || sender.isBlank()){
                sender = "Player";
            }

            try{
                Network.getInstance().queueRequest(Network.send_chat, new String[]{text.trim()});
                System.out.println("chat message sent");
            } catch(Exception e){
                System.err.println("Failed to send chat message: " + e.getMessage());
            }


            messageInput.clear();
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


    protected void gameOver(){ //copy of the button version
        try {
            stopChatWatcher();
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameOverDisplay.fxml"));
            Parent gameOverRoot = loader.load();

            gameOverController controller = loader.getController();
            controller.setGameType(GameType.TICTACTOE);

            Stage stage = (Stage) ttt00.getScene().getWindow();
            Scene gameOverScene = new Scene(gameOverRoot, 800, 600);
            stage.setScene(gameOverScene);
            stage.setTitle("Game Over"); //Change stage title to reflect current scene
            stage.setResizable(false);
            stage.show();
            
            ChatRegistry.getInstance().clearChat();

        } catch (IOException e) {
            System.err.println("Error: Could not load gameOverDisplay.fxml. Check file path!");
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            stopChatWatcher();
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TTTopponentSelectPage.fxml"));
            Parent opponentRoot = loader.load();

            //Get current stage from the button click
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Create new scene and set it on the stage
            Scene opponentScene = new Scene(opponentRoot, 800, 600);
            stage.setScene(opponentScene);
            stage.setTitle("Tic-Tac-Toe - Opponent Select"); //Change stage title to reflect current scene
            stage.setResizable(false);
            ChatRegistry.getInstance().clearChat();
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Could not load TTTopponentSelectPage.fxml. Check file path!");
        }
    }

    @FXML
    protected void onQuestionButtonClick(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play Tic-Tac-Toe");
        alert.setHeaderText("Game Instructions");

        String instructions = "1. The game is played on a 3x3 grid.\n\n"
                + "2. If you are X, your opponent is O. If you are O, your opponent is X. Players take turns putting their marks in empty squares.\n\n"
                + "3. The first player to get 3 of their marks in a row (up, down, across, or diagonally) is the winner.\n\n"
                + "4. When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie.\n";

        alert.setContentText(instructions);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("pane");
        dialogPane.setMinHeight(400);
        dialogPane.setMinWidth(450);

        alert.showAndWait();

    }

    private void startChatWatcher(){
        chatRefreshTimeline = new Timeline(
                new KeyFrame(Duration.millis(250), event ->{
                    int currentSize = ChatRegistry.getInstance().ListAll().size();
                    if(currentSize != lastChatSize){
                        refreshChatDisplay();
                        lastChatSize = currentSize;
                    }
                })
        );

        chatRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        chatRefreshTimeline.play();
    }

    private void stopChatWatcher() {
        if (chatRefreshTimeline != null) {
            chatRefreshTimeline.stop();
        }
    }

    //Everytime this is called, the board is updated
    private void updateBoard(){
       TicTacToeBoard board = current.getBoard();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!board.isCellEmpty(row, col)){
                    grid[row][col].setText(String.valueOf(board.getCellInfo(row,col)));
                }
            }
        }
    }

    @FXML
    protected void onGridButtonClick(ActionEvent event) {
        char player = current.getCurrentPlayer(); //gets whose turn it is
        Button clicked = (Button) event.getSource(); //gets what button was clicked
        int i = 4; //four, so if not intialized, the turn shouldn't count
        int j = 4;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (clicked == grid[row][col]){
                    i = row;
                    j = col;
                }
            }
        }
        if (current.makeMove(i,j,player)) { //updates if the move was valid
            turnDisplay.setText("Yippee!");
            if (current.getGameState() == GameState.PLAYER_WIN){
                gameOver(); //ends game if someone wins
            } else if (current.getGameState() == GameState.PLAYER_DRAW) {
                gameOver(); //ends game if draw
            }
//            current.switchTurn();
        }else{
            turnDisplay.setText("Please make a valid move");
        }

        updateBoard();
    }



}
