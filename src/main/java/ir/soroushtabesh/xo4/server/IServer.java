package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.GameInstance;

public interface IServer {
    enum Message {
        SUCCESS, EXISTS, ERROR, WRONG
    }

    Message signUp(String username, String password);

    PlayerController login(String username, String password);

    int[] getAllRunningGames();

    GameInstance getGame(int id);
}
