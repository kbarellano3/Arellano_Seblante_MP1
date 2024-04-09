package org.quinemccluskey.algorithm;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

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
                outputTextArea.setText("INVALID INPUT: Use letters only for your starting variable.");
            }
        } catch (RuntimeException e) {
            if (input1.isEmpty()) {
                outputTextArea.setText("You did not input any minterms.");
            } else if (!checkIfNumber(input1) && !checkIfNumber(input2)) {
                outputTextArea.setText("INVALID INPUT: One of your inputs is not a number.");
            } else if (!checkIfNumber(input1) || !checkIfNumber(input2)) {
                outputTextArea.setText("INVALID INPUT: One of your inputs is not a number.");
            } else outputTextArea.setText("INVALID INPUT: Your minterms and don't cares have one or more common terms.");
        }
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

}