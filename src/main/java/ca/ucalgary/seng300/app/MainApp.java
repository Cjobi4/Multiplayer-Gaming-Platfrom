package ca.ucalgary.seng300.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //Load FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcomePage.fxml"));

        Scene scene = new Scene(root, 600, 400);

        Image icon = new Image(getClass().getResourceAsStream("/images/OGdino.png"));
        stage.getIcons().add(icon);

        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
