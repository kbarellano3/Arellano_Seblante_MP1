package org.quinemccluskey.algorithm;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.time.LocalTime;

/**
 * Controller class of the Main FXML which enables the application to function.
 *
 * <p>
 *     This class is responsible for providing the functionalities ond flow of processes for each element used in the
 *     GUI. It is responsible for handling the user-input on minterms, don't cares, and the start variable. It is also
 *     responsible for implementing the Quine_McCluskey Java class, invoking the algorithm for proper solving.
 * </p>
 */
public class MainController {

    @FXML
    private TextArea dcTextArea;

    @FXML
    private TextArea mtTextArea;

    @FXML
    private TextArea startVariableTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private ImageView finnImage;

    @FXML
    private ImageView jakeImage;

    @FXML
    private ImageView bmoImage;

    @FXML
    private ImageView iceKingImage;

    @FXML
    private Label numSuccessfulRunsLabel;

    @FXML
    private Label timeLabel;
    private int numSuccessfulRuns = 0;

    /**
     * Method called when the user clicks the 'solve' button.
     *
     * <p>
     *     This method retrieves user input and calls the Quine_McCluskey class to perform calculations on them. It
     *     then outputs the proper boolean expression/error message onto another text field.
     * </p>
     */
    public void solve() {
        String input1 = mtTextArea.getText();
        String input2 = dcTextArea.getText();
        String input3 = startVariableTextArea.getText();
        // Parse input and perform calculations
        try {
            Quine_McCluskey qm = new Quine_McCluskey(input1, input2, input3);
            qm.solve1();
            outputTextArea.setText(qm.getResultsAsString());
            if (isNumber(input3)) {
                outputTextArea.setText("INVALID INPUT: Use letters only for your starting variable");
            } else {
                numSuccessfulRuns++;
                updateNumSuccessfulRunsLabel();
            }
        } catch (RuntimeException e) {
            if (input1.isEmpty()) {
                outputTextArea.setText("INVALID INPUT: You did not input any minterms");
            } else if (!checkIfNumber(input1) && !checkIfNumber(input2)) {
                outputTextArea.setText("INVALID INPUT: One of your inputs is not a number");
            } else if (!checkIfNumber(input1) || !checkIfNumber(input2)) {
                outputTextArea.setText("INVALID INPUT: One of your inputs is not a number");
            } else outputTextArea.setText("INVALID INPUT: Your minterms and don't cares have one or more common terms");
        }
    }

    /**
     * Updates the text of the numSuccessfulRunsLabel.
     *
     * <p>
     *     This method sets the text of the numSuccessfulRunsLabel to display the current number of successful runs.
     * </p>
     */
    private void updateNumSuccessfulRunsLabel() {
        numSuccessfulRunsLabel.setText("No. of Runs: " + numSuccessfulRuns);
    }

  
    /**
     * Helper method which determines if a string is a number or not.
     *
     * <p>
     *     This is a boolean method which determines that a string is an integer by parsing it and returning true. If it
     *     yields in a NumberFormatException, it determines that the string is not a number by returning false.
     * </p>
     * @param x The string to be determined.
     * @return Returns the truth value of param x as a number.
     */
    public static boolean isNumber(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Helper method that determines if a string, removed of its delimiters, is a number or not.
     *
     * <p>
     *     This method is used in catching runtime exceptions, which can be caused by incorrectly inputted values.
     * </p>
     * <p>
     *     This method first removes the delimiters—either commas or spaces—from a given string of minterms/don't
     *     cares. It then calls the isNumber() method to determine the boolean value of the string.
     * </p>
     * @param str The string separated by commas or spaces.
     * @return Returns the truth value of param str as a number.
     */
    public static boolean checkIfNumber(String str) {
        String[] numbers = str.split("[,\\s]+");
        for (String s : numbers) {
            if (!isNumber(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that intiailizes the controller's view.
     *
     * <p>
     *     This method sets up the animations for character images, the label for the number of runs, and the timeline
     *     animation to update the time label.
     * </p>
     */
    public void initialize() {
        // Create a translate transition for the Finn image
        TranslateTransition finnTransition = new TranslateTransition(Duration.seconds(1), finnImage);
        finnTransition.setByY(-20); // Move the image up by 20 pixels
        finnTransition.setAutoReverse(true); // Move the image back down after reaching the top
        finnTransition.setCycleCount(TranslateTransition.INDEFINITE); // Repeat the transition indefinitely
        finnTransition.play();

        // Create a translate transition for the Jake image
        TranslateTransition jakeTransition = new TranslateTransition(Duration.seconds(1), jakeImage);
        jakeTransition.setByY(-20);
        jakeTransition.setAutoReverse(true);
        jakeTransition.setCycleCount(TranslateTransition.INDEFINITE);
        jakeTransition.play();

        // Create a translate transition for the BMO image
        TranslateTransition bmoTransition = new TranslateTransition(Duration.seconds(1), bmoImage);
        bmoTransition.setByY(-20);
        bmoTransition.setAutoReverse(true);
        bmoTransition.setCycleCount(TranslateTransition.INDEFINITE);
        bmoTransition.play();

        // Create a translate transition for the Ice King image
        TranslateTransition iceKingTransition = new TranslateTransition(Duration.seconds(1), iceKingImage);
        iceKingTransition.setByY(-20);
        iceKingTransition.setAutoReverse(true);
        iceKingTransition.setCycleCount(TranslateTransition.INDEFINITE);
        iceKingTransition.play();

        numSuccessfulRunsLabel.setText("No. of Runs: 0");

        updateNumSuccessfulRunsLabel();

        // Set the initial time label text
        timeLabel.setText(getCurrentTime());

        // Create a Timeline animation that updates the time label every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLabel.setText(getCurrentTime());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Method that return the current time.
     *
     * <p>
     *     This method uses the Java.time library to get the local time and displays it in [Hours]:[Minutes]:[Seconds].
     * </p>
     */
    private String getCurrentTime() {
        LocalTime time = LocalTime.now();
        return String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
    }
}