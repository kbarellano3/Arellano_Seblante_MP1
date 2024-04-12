package org.quinemccluskey.algorithm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A JavaFX application that includes the full implementation of the Quine-McCluskey algorithm (QMA) for simplifying
 * boolean expressions.
 *
 * <p>
 *     This application utilizes a graphical user interface (GUI) and calls Main.fxml, which calls the
 *     MainController. The user inputs the minterms, don't cares, and the start variable into separate text field. The
 *     program then processes these via the QMA once the user clicks the 'solve' button. The simplified expression/s
 *     are then outputted onto the program.
 * </p>
 */
public class Main extends Application {


    /**
     * A void method that overrides the start method of the Application class
     *
     * <p>
     *      This method starts the JavaFX application by loading the main FXML file. It then sets the height and width
     *      as the maximum available width and height of the user's screen. It then displays the primary stage.
     * </p>
     *
     * @param primaryStage Stage window that the user will interact with
     * @throws IOException Exception in the case of an error in loading the FXML file
     */
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

    /**
     * The main method which launches the JavaFX application.
     *
     * @param args Command-line arguments which are not used in the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
