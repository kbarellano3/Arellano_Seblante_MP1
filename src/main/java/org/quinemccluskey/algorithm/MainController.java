package org.quinemccluskey.algorithm;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {

    @FXML
    private TextArea dcTextArea;

    @FXML
    private TextArea mtTextArea;

    @FXML
    private TextArea startVariableTextArea;

    @FXML
    private TextArea outputTextArea;

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
//            if (isNumber(input1) && isNumber(input2)) {
//                outputTextArea.setText("INVALID INPUT: Your minterms and don't cares have one or more common terms.");
//            }
//            else outputTextArea.setText("INVALID INPUT: One of your inputs is not a number.");
            if (input1.isEmpty()) {
                outputTextArea.setText("You did not input any minterms.");
            } else if (input1.isEmpty() && input2.isEmpty()) {
                outputTextArea.setText("You did not input any minterms.");
            } else if (!checkIfNumber(input1) && !checkIfNumber(input2)) {
                outputTextArea.setText("INVALID INPUT: One of your inputs is not a number.");
            } else if (!checkIfNumber(input1) || !checkIfNumber(input2)) {
                outputTextArea.setText("INVALID INPUT: One of your inputs is not a number.");
            } else outputTextArea.setText("INVALID INPUT: Your minterms and don't cares have one or more common terms.");
        }
    }
    public static boolean isNumber(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

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