package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;

public interface IServer {
    enum Message {
        SUCCESS, EXISTS, ERROR, WRONG, WAIT
    }

    Message signUp(String username, String password);

    PlayerController login(String username, String password);

    Message logout(long token);

    int[] getOldGames();

    int[] getRunningGames();

    GameInstance getGameByID(int gid);

    PlayerBrief[] getAllPlayers();

    PlayerBrief getPlayer(String username);

    Message requestGame(long token, LazyResult<GameInstance> lazyResult);

    Message cancelGameRequest(long token);

    Message forfeit(long token);

    GameInstance getGameFromToken(long token);

    Message play(long token, int i, int j);

    Change checkForChange(long token);

}
