package ir.soroushtabesh.xo4.client.gui.controllers;

import ir.soroushtabesh.xo4.client.gui.LoginScene;
import ir.soroushtabesh.xo4.client.gui.MenuScene;
import ir.soroushtabesh.xo4.client.utils.ConfigLoader;
import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.PlayerController;
import ir.soroushtabesh.xo4.server.RemoteServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class LoginSceneController extends AbstractSceneController {
    @FXML
    private Label configLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void signUp(ActionEvent event) {
        RemoteServer server = RemoteServer.getInstance();
        LoginScene currentScene = (LoginScene) getCurrentScene();
        if (!server.connect()) {
            currentScene.showCantConnect();
            return;
        }
        if (!checkInputValid()) {
            currentScene.showCheckInput();
            return;
        }
        IServer.Message message = server.signUp(getInputUsername(), getInputPassword());
        switch (message) {
            case ERROR:
                currentScene.showError();
                break;
            case EXISTS:
                currentScene.showExists();
                break;
            case SUCCESS:
                currentScene.showSignUpSuccess();
        }
    }

    @FXML
    private void login(ActionEvent event) {
        RemoteServer server = RemoteServer.getInstance();
        LoginScene currentScene = (LoginScene) getCurrentScene();
        if (!server.connect()) {
            currentScene.showCantConnect();
            return;
        }
        if (!checkInputValid()) {
            currentScene.showCheckInput();
            return;
        }
        PlayerController playerController = server.login(getInputUsername(), getInputPassword());
        if (playerController != null) {
            SceneManager.getInstance().showScene(MenuScene.class);
        } else {
            currentScene.showWrong();
        }
    }

    private boolean checkInputValid() {
        return getInputPassword().length() >= 3 && !getInputUsername().isEmpty();
    }

    private String getInputUsername() {
        return usernameField.getText().trim();
    }

    private String getInputPassword() {
        return passwordField.getText();
    }

    @FXML
    private void chooseConfig(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(getPane().getScene().getWindow());
        if (file == null)
            return;
        configLabel.setText(file.getAbsolutePath());
        RemoteServer remoteServer = RemoteServer.getInstance();
        remoteServer.disconnect();
        remoteServer.setConfig(ConfigLoader.loadConfig(file));
    }
}
