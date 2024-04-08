package org.quinemccluskey.algorithm;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {

    @FXML
    private TextArea dcTextArea;

    @FXML
    private TextArea mtTextArea;

    @FXML
    private TextArea outputTextArea;

    public void solve() {
        String input1 = mtTextArea.getText();
        String input2 = dcTextArea.getText();
        // Parse input and perform calculations
        try {
            Quine_McCluskey qm = new Quine_McCluskey(input1, input2);
            qm.solve1();
            outputTextArea.setText(qm.getResultsAsString());
        } catch (RuntimeException e) {
            if (isNumber(input1) && isNumber(input2)) {
                outputTextArea.setText("INVALID INPUT: Your minterms and don't cares have one or more common terms.");
            }
            else outputTextArea.setText("INVALID INPUT: One of your inputs is not a number.");
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

}