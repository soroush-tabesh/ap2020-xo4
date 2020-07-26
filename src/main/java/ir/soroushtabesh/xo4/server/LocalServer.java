package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.GameInstance;

public class LocalServer implements IServer {
    @Override
    public Message signUp(String username, String password) {
        System.out.println("LocalServer.signUp");
        return DataManager.getInstance().addPlayer(username, password);
    }

    @Override
    public PlayerController login(String username, String password) {
        System.out.println("LocalServer.login");
        return DataManager.getInstance().authenticate(username, password);
    }

    @Override
    public int[] getAllRunningGames() {
        return new int[0];
    }

    @Override
    public GameInstance getGame(int id) {
        return null;
    }
}
