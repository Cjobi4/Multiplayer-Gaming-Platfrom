package ca.ucalgary.seng300.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //Load FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcomePage.fxml"));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Project Welcome");
        stage.setScene(scene);
        stage.show();

//        Label label = new Label("JavaFX 25 + JDK 25 OK");
//        Scene scene = new Scene(label, 600, 400);
//        stage.setScene(scene);
//        stage.setTitle("Starter");
//        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
