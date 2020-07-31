package ir.soroushtabesh.xo4.client.utils;

import javafx.animation.Timeline;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class AnimationPool {
    private final Map<Node, Timeline> pool = new HashMap<>();

    public void startAnimation(Node node, Timeline timeline) {
        Timeline prevTL = pool.put(node, timeline);
        if (prevTL != null)
            prevTL.stop();
        timeline.play();
    }

    public void stopAnimation(Node node) {
        Timeline timeline = pool.remove(node);
        if (timeline != null) {
            timeline.stop();
            timeline.getOnFinished().handle(null);
        }
    }

    public void stopAll() {
        pool.forEach((node, timeline) -> {
            timeline.stop();
            timeline.getOnFinished().handle(null);
        });
    }

    public void clearAll() {
        stopAll();
        pool.clear();
    }
}
