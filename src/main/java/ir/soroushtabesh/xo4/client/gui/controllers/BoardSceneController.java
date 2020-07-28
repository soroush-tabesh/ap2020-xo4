package ir.soroushtabesh.xo4.client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardSceneController extends AbstractSceneController {
    @FXML
    private GridPane boardGrid;
    @FXML
    private Label youLabel;
    @FXML
    private Label oLabel;
    @FXML
    private Label xLabel;

    private Button[][] cells = new Button[7][7];

    @FXML
    private void forfeit(ActionEvent actionEvent) {

    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        EventHandler<MouseEvent> mouseEnter = event -> {
        };
        EventHandler<MouseEvent> mouseExit = event -> {
        };
        EventHandler<MouseEvent> mouseClick = event -> {
        };
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Button button = new Button();
                button.setOnMouseEntered(mouseEnter);
                button.setOnMouseExited(mouseExit);
                button.setOnMouseClicked(mouseClick);
                button.setStyle("-fx-background-color: transparent; -fx-background-size: stretch;");
                button.setMaxWidth(Double.MAX_VALUE);
                boardGrid.add(button, j, i);
                cells[i][j] = button;
            }
        }
    }
}
