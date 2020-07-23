package ir.soroushtabesh.xo4.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameWindowController implements Initializable {
    @FXML
    private StackPane windowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public StackPane getWindowPane() {
        return windowPane;
    }
}
