package ir.soroushtabesh.xo4.client.gui;

import ir.soroushtabesh.xo4.client.PlayerManager;
import ir.soroushtabesh.xo4.client.gui.controllers.AudioManager;
import ir.soroushtabesh.xo4.client.gui.controllers.GameWindowController;
import ir.soroushtabesh.xo4.client.gui.controllers.SceneManager;
import ir.soroushtabesh.xo4.client.utils.FXUtil;
import ir.soroushtabesh.xo4.server.RemoteServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.SecureRandom;

public class GameWindow extends Application {
    private Stage stage;
    private GameWindowController controller;
    private Parent root = null;
    private Scene scene = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        setStage(stage);
        if (!setUpStage(stage)) throw new RuntimeException("Could not load fxml");
        gameInit();
        FXUtil.runLater(() -> {
//            SceneManager.getInstance().showScene(BoardScene.class);
            SceneManager.getInstance().showScene(LoginScene.class);
            AudioManager.getInstance().startBackgroundMusic();
            controller.muteButton(null);
        }, 500);
    }

    private boolean setUpStage(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/GameWindow.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        setController(fxmlLoader.getController());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("XO4 - Soroush Tabesh " + new SecureRandom().nextInt(20));
        System.out.println(stage.getTitle());
        stage.setResizable(false);
        stage.show();
        return true;
    }

    private void gameInit() {
        initSceneManager();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("GameWindow.stop");
        AudioManager.getInstance().dispose();
        if (PlayerManager.getInstance().getPlayer() != null)
            PlayerManager.getInstance().getPlayer().logout();
        RemoteServer.getInstance().disconnect();
        SceneManager.getInstance().shutdown();
    }

    private void initSceneManager() {
        SceneManager sceneManager = SceneManager.init(this);
        sceneManager.addScene(new BoardScene());
        sceneManager.addScene(new MenuScene());
        sceneManager.addScene(new LoginScene());
    }

    public Stage getStage() {
        return stage;
    }

    private void setStage(Stage stage) {
        this.stage = stage;
    }

    public GameWindowController getController() {
        return controller;
    }

    private void setController(GameWindowController controller) {
        this.controller = controller;
    }
}
