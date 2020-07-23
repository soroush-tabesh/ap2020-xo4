package ir.soroushtabesh.xo4.client.gui.controllers;

import ir.soroushtabesh.xo4.client.gui.AbstractScene;
import ir.soroushtabesh.xo4.client.gui.GameWindow;
import ir.soroushtabesh.xo4.client.utils.FXUtil;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
    private static SceneManager instance;
    private final GameWindow gameWindow;
    private final List<AbstractScene> scenes = new ArrayList<>();
    private AbstractScene currentScene;

    private SceneManager(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public static SceneManager getInstance() {
        return instance;
    }

    public static SceneManager init(GameWindow gameWindow) {
        return instance = new SceneManager(gameWindow);
    }

    public void addScene(AbstractScene scene) {
        scenes.add(scene);
        StackPane windowPane = gameWindow.getController().getWindowPane();
        windowPane.getChildren().add(scene.getPane());
    }

    public void showScene(Class<? extends AbstractScene> tClass) {
        showScene(tClass, null);
    }

    public void showScene(Class<? extends AbstractScene> tClass, Object message) {
        if (currentScene != null && currentScene.getClass().equals(tClass)) {
            return;
        }
        for (AbstractScene scene : scenes) {
            if (scene.getClass().equals(tClass)) {
                changeScene(scene, message);
                return;
            }
        }
        throw new NoSuchSceneException();
    }

    private void changeScene(AbstractScene target, Object message) {
        applyTransit(target);
        FXUtil.runLater(() -> target.onStart(message), 0);
        if (currentScene != null) {
            AbstractScene temp = currentScene;
            FXUtil.runLater(temp::onStop, 0);
        }
        currentScene = target;
    }

    private void applyTransit(AbstractScene target) {
        target.getPane().toFront();
        target.fadeIn();
        if (currentScene != null) {
            currentScene.getPane().toFront();
            currentScene.fadeOut();
        }
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
}
