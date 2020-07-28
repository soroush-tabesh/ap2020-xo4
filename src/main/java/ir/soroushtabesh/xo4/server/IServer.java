package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;

public interface IServer {
    enum Message {
        SUCCESS, EXISTS, ERROR, WRONG
    }

    Message signUp(String username, String password);

    PlayerController login(String username, String password);

    Message logout(long token);

    int[] getAllGames();

    GameInstance getGameByID(int gid);

    PlayerBrief[] getAllPlayers();

    PlayerBrief getPlayer(String username);

    GameInstance requestGame(long token);

    Message cancelGameRequest(long token);

    Message forfeit(long token);

    GameInstance getGameFromToken(long token);

    Change play(long token, int i, int j);

}
