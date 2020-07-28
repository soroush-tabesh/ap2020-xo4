package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;

public interface IServer {
    enum Message {
        SUCCESS, EXISTS, ERROR, WRONG
    }

    Message signUp(String username, String password);

    PlayerController login(String username, String password);

    void logout(long token);

    int[] getAllRunningGames();

    GameInstance getGame(int id);

    PlayerBrief[] getAllPlayers();

    PlayerBrief getPlayer(String username);
}
