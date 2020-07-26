package ir.soroushtabesh.xo4.client.gui.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;

public class AudioManager {
    private static AudioManager instance;

    private MediaPlayer mediaPlayer_bg;

    private AudioManager() {
        init();
    }

    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    public void startBackgroundMusic() {
        mediaPlayer_bg.play();
    }

    public DoubleProperty bgMusicVolumeProperty() {
        return mediaPlayer_bg.volumeProperty();
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer_bg != null)
            mediaPlayer_bg.stop();
    }

    public void init() {
        Media hit_bg;
        try {
            hit_bg = new Media(getClass().getClassLoader()
                    .getResource("sound/bgmusic.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        mediaPlayer_bg = new MediaPlayer(hit_bg);
        mediaPlayer_bg.setCycleCount(-1);
    }

    public void dispose() {
        if (mediaPlayer_bg != null)
            mediaPlayer_bg.dispose();
    }

}
