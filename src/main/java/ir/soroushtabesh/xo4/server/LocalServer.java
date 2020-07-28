package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.Player;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;

import java.util.List;

public class LocalServer implements IServer {
    @Override
    public Message signUp(String username, String password) {
        System.out.println("LocalServer.signUp");
        return DataManager.getInstance().addPlayer(username, password);
    }

    @Override
    public PlayerController login(String username, String password) {
        System.out.println("LocalServer.login");
        DataManager dataManager = DataManager.getInstance();
        PlayerController controller = dataManager.authenticate(username, password);
        dataManager.setPlayerState(username, Player.State.ONLINE);
        if (controller == null)
            return null;
        controller.setServer(this);
        return controller;
    }

    @Override
    public Message logout(long token) {
        DataManager dataManager = DataManager.getInstance();
        Player player = dataManager.getPlayer(token);
        dataManager.expire(token);
        dataManager.setPlayerState(player, Player.State.OFFLINE);
        return Message.SUCCESS;
    }

    @Override
    public int[] getAllGames() {
        return null;
    }

    @Override
    public GameInstance getGameByID(int gid) {
        return null;
    }

    @Override
    public PlayerBrief[] getAllPlayers() {
        List<Player> allPlayers = DataManager.getInstance().getAllPlayers();
        PlayerBrief[] playerBriefs = new PlayerBrief[allPlayers.size()];
        int i = 0;
        for (Player player : allPlayers)
            playerBriefs[i++] = player.getBriefData();
        return playerBriefs;
    }

    @Override
    public PlayerBrief getPlayer(String username) {
        return DataManager.getInstance().getPlayer(username).getBriefData();
    }

    @Override
    public GameInstance requestGame(long token) {
        return null;
    }

    @Override
    public Message cancelGameRequest(long token) {
        return null;
    }

    @Override
    public Message forfeit(long token) {
        return null;
    }

    @Override
    public GameInstance getGameFromToken(long token) {
        return null;
    }

    @Override
    public Change play(long token, int i, int j) {
        return null;
    }
}
