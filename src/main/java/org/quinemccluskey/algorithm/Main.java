package org.quinemccluskey.algorithm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/quinemccluskey.algorithm/Main.fxml"));

        // Set the initial width and height to the maximum available width and height
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth();
        double maxHeight = screen.getVisualBounds().getHeight();
        primaryStage.setWidth(maxWidth);
        primaryStage.setHeight(maxHeight);

        // Set the scene and show the primary stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
