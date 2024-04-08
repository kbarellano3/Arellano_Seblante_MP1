package org.example.quine_mccluskey_gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.example.quine_mccluskey_gui.Quine_McCluskey;

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