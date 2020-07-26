package ir.soroushtabesh.xo4.client;

import ir.soroushtabesh.xo4.server.PlayerController;

public class PlayerManager {
    private final static PlayerManager instance = new PlayerManager();

    private PlayerController player;

    private PlayerManager() {

    }

    public static PlayerManager getInstance() {
        return instance;
    }

    public PlayerController getPlayer() {
        return player;
    }

    public void setPlayer(PlayerController player) {
        this.player = player;
    }
}
