package ir.soroushtabesh.xo4.client.gui;

import ir.soroushtabesh.xo4.client.gui.controllers.AbstractSceneController;
import ir.soroushtabesh.xo4.client.utils.AnimationUtil;
import ir.soroushtabesh.xo4.client.utils.FXUtil;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public abstract class AbstractScene {
    private Pane pane;
    private AbstractSceneController controller;

    public AbstractScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtil.getFXMLResource(getClass()));
        Pane root;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        setController(fxmlLoader.getController());
        getController().setCurrentScene(this);
        setPane(root);
    }

    public Pane getPane() {
        return pane;
    }

    private void setPane(Pane pane) {
        this.pane = pane;
    }

    public AbstractSceneController getController() {
        return controller;
    }

    private void setController(AbstractSceneController controller) {
        this.controller = controller;
    }

    public void fadeIn() {
        getController().setDisable(false);
        getPane().setScaleX(1);
        getPane().setScaleY(1);
        Timeline timeline = AnimationUtil.getSceneFadeIn(getPane());
        timeline.setOnFinished((evt) -> getPane().setOpacity(1));
        timeline.play();
    }

    public void fadeOut() {
        Timeline timeline = AnimationUtil.getSceneFadeOut(getPane());
        timeline.setOnFinished((evt) -> {
            getController().setDisable(true);
            getPane().setOpacity(1);
            getPane().setScaleX(1);
            getPane().setScaleY(1);
        });
        timeline.play();
    }

    public void onStart(Object message) {
        getController().onStart(message);
    }

    public void onStop() {
        getController().onStop();
    }


}
