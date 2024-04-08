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
        Quine_McCluskey qm = new Quine_McCluskey(input1, input2);
        qm.solve1();
        outputTextArea.setText(qm.getResultsAsString());
    }
}