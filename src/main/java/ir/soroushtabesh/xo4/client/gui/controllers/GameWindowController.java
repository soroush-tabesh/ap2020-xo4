package ir.soroushtabesh.xo4.client.gui.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameWindowController implements Initializable {

    @FXML
    private Button spButton;
    @FXML
    private StackPane windowPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public StackPane getWindowPane() {
        return windowPane;
    }

    @FXML
    public void muteButton(ActionEvent actionEvent) {
        DoubleProperty volumeProperty = AudioManager.getInstance().bgMusicVolumeProperty();
        if (volumeProperty.get() < 0.1) {
            volumeProperty.setValue(1);
            spButton.setStyle("-fx-background-image: url('/ir/soroushtabesh/xo4/client/gui/image/sp_on.png');");
        } else {
            volumeProperty.setValue(0);
            spButton.setStyle("-fx-background-image: url('/ir/soroushtabesh/xo4/client/gui/image/sp_off.png');");
        }
    }
}
