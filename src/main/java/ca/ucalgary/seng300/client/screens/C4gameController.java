package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.games.GameState;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeBoard;
import ca.ucalgary.seng300.games.tictactoe.TicTacToeGame;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.shared.models.Message;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ca.ucalgary.seng300.games.connectfour.ConnectFourGame;
import ca.ucalgary.seng300.games.connectfour.ConnectFourBoard;

import java.io.IOException;

public class C4gameController {

    public Button gameOverButton;
    public Button backButton;
    public ScrollPane chatScrollPane;
    public VBox chatContainer;
    public TextField messageInput;
    public GridPane c4grid;
    public Text turnDisplayc4;


    ConnectFourGame current = new ConnectFourGame();
    Button[][] grid = new Button[6][7];

    private Timeline chatRefreshTimeline;
    private int lastChatSize = -1;

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

        messageInput.setOnAction(event -> onSendMessage());
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

    private void updateBoard(){
        ConnectFourBoard board = current.getBoard(); //loops through the board
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (board.getCell(i, j) == 'X'){ //if its not empty
                    grid[i][j].setStyle( "-fx-background-color: #f0e8a1;"); //yellow
                } else if (board.getCell(i, j) == 'O') {
                    grid[i][j].setStyle( "-fx-background-color: #f0a1a1;"); //red
                }
            }
        }
    }

    protected void gameOver(){ //copy of the button version
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameOverDisplay.fxml"));
            Parent gameOverRoot = loader.load();

            gameOverController controller = loader.getController();
            controller.setGameType(GameType.CONNECT4);


            Stage stage = (Stage) grid[0][0].getScene().getWindow();
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
    protected void onGameOverButtonClick(ActionEvent event) {
        try {
            //Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameOverDisplay.fxml"));
            Parent gameOverRoot = loader.load();


            gameOverController controller = loader.getController();
            controller.setGameType(GameType.CONNECT4);

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

    @FXML
    protected void onGridButtonClick(ActionEvent event) {
        char player = current.getCurrentPlayer(); //gets whose turn it is
        Button clicked = (Button) event.getSource(); //gets what button was clicked
        int i = 8; //four, so if not intialized, the turn shouldn't count
        int j = 8;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (clicked == grid[row][col]){
                    i = col;
                }
            }
        }
        if (current.makeMove(i,player)) { //updates if the move was valid
            turnDisplayc4.setText("Yippee!");
            if (current.getGameState() == GameState.PLAYER_WIN){
                gameOver(); //ends game if someone wins
            } else if (current.getGameState() == GameState.PLAYER_DRAW) {
                gameOver(); //ends game if draw
            }
//            current.switchTurn();
        }else{
            turnDisplayc4.setText("Please make a valid move");
        }

        updateBoard();
    }
}
