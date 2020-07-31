package ir.soroushtabesh.xo4.client.utils;

import animatefx.animation.AnimateFXInterpolator;
import animatefx.animation.Tada;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AnimationUtil {
    private AnimationUtil() {
    }

    public static Timeline getSceneFadeOut(Node node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0)
                        , new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleXProperty(), 1, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleYProperty(), 1, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500)
                        , new KeyValue(node.opacityProperty(), 0, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleXProperty(), 2, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleYProperty(), 2, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.opacityProperty(), 0, AnimateFXInterpolator.EASE)
                )
        );
    }

    public static Timeline getSceneFadeIn(Node node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE)
                )

        );
    }

    public static Effect getGlowAnimated(Color color, double minDepth, double maxDepth) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(color);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(borderGlow.heightProperty(), minDepth, AnimateFXInterpolator.EASE),
                        new KeyValue(borderGlow.widthProperty(), minDepth, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(borderGlow.heightProperty(), maxDepth, AnimateFXInterpolator.EASE),
                        new KeyValue(borderGlow.widthProperty(), maxDepth, AnimateFXInterpolator.EASE)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return borderGlow;
    }

    public static Timeline getPulse(Node node) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.scaleXProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 1, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.scaleXProperty(), 2, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 2, AnimateFXInterpolator.EASE)
                )
        );
        timeline.setOnFinished(event -> {
            node.setScaleX(1);
            node.setScaleY(1);
        });
        timeline.setCycleCount(-1);
        timeline.setAutoReverse(true);
        return timeline;
    }

    public static Timeline getTada(Node node) {
        node.setRotationAxis(Rotate.Z_AXIS);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.rotateProperty(), 0, AnimateFXInterpolator.EASE)

                ),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(700),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(900),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.rotateProperty(), 0, AnimateFXInterpolator.EASE)
                )
        );
        timeline.setOnFinished(event -> node.setRotate(0));
        return timeline;
    }

    public static Timeline getDragHover(Node node) {
        return getDragHover(node, 1.3);
    }

    public static Timeline getDragHover(Node node, double scale) {
        double scaleX = node.getScaleX();
        double scaleY = node.getScaleY();
        double opacity = node.getOpacity();
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.scaleXProperty(), scaleX, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), scaleY, AnimateFXInterpolator.EASE),
                        new KeyValue(node.opacityProperty(), opacity, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.scaleXProperty(), scaleX * scale, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), scaleY * scale, AnimateFXInterpolator.EASE),
                        new KeyValue(node.opacityProperty(), opacity * 0.7, AnimateFXInterpolator.EASE)
                )
        );
        pulse.setCycleCount(-1);
        pulse.setAutoReverse(true);
        pulse.setOnFinished(event -> {
            node.setScaleX(scaleX);
            node.setScaleY(scaleY);
            node.setOpacity(opacity);
        });
        return pulse;
    }

    public static Timeline getPassiveBounce(Node node) {
        Timeline anim = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.translateYProperty(), 0, AnimateFXInterpolator.EASE),
                        new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.translateYProperty(), 5, AnimateFXInterpolator.EASE),
                        new KeyValue(node.opacityProperty(), 0.7, AnimateFXInterpolator.EASE)
                )
        );
        anim.setCycleCount(-1);
        anim.setAutoReverse(true);
        anim.setOnFinished(event -> {
            node.setTranslateX(0);
            node.setOpacity(1);
        });
        return anim;
    }

    public static Timeline getTextGlowRed(Labeled node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.textFillProperty(), Color.GAINSBORO, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.textFillProperty(), Color.RED, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.textFillProperty(), Color.GAINSBORO, AnimateFXInterpolator.EASE)
                )
        );
    }

    public static Timeline getTextGlowGreen(Labeled node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.textFillProperty(), Color.GAINSBORO, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.textFillProperty(), Color.GREEN, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.textFillProperty(), Color.GAINSBORO, AnimateFXInterpolator.EASE)
                )
        );
    }

    public static Timeline getAttackAnimation(Node node) {
        return new Tada(node).getTimeline();
    }

    public static Timeline getMinionAppearance(Node node) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0.2, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleXProperty(), 2, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 2, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleXProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 1, AnimateFXInterpolator.EASE)
                )
        );
        timeline.setOnFinished(event -> {
            node.setOpacity(1);
            node.setScaleX(1);
            node.setScaleY(1);
        });
        return timeline;
    }

    public static Timeline getWeaponAppearance(Node node) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0.8, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleXProperty(), 0, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 0, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleXProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 1, AnimateFXInterpolator.EASE)
                )
        );
        timeline.setOnFinished(event -> {
            node.setOpacity(1);
            node.setScaleX(1);
            node.setScaleY(1);
        });
        return timeline;
    }


    public static Timeline getSpellAppearance(Node node) {
        double startX = -node.localToScene(0, 0).getX() - node.getBoundsInParent().getWidth();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(node.translateXProperty(), startX, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.opacityProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(node.translateXProperty(), 25, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(750),
                        new KeyValue(node.translateXProperty(), -10, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(900),
                        new KeyValue(node.translateXProperty(), 5, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.translateXProperty(), 0, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(node.translateXProperty(), 0, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(2200),
                        new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.translateXProperty(), -20, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(node.opacityProperty(), 0, AnimateFXInterpolator.EASE),
                        new KeyValue(node.translateXProperty(), 2000, AnimateFXInterpolator.EASE)
                )

        );
        timeline.setDelay(Duration.millis(500));
        return timeline;
    }


}
