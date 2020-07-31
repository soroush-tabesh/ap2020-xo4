package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;

public class PlayerController {

    private PlayerBrief playerBrief;
    private final long token;
    private transient IServer server;

    public PlayerController(PlayerBrief playerBrief, long token) {
        this.playerBrief = playerBrief;
        this.token = token;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public void updateBrief() {
        if (server == null || playerBrief == null)
            return;
        playerBrief = server.getPlayer(playerBrief.getUsername());
    }

    public PlayerBrief getPlayerBrief() {
        return playerBrief;
    }

    public long getToken() {
        return token;
    }

    public IServer getServer() {
        return server;
    }

    public void logout() {
        server.logout(token);
    }

    public IServer.Message requestGame(LazyResult<GameInstance> lazyResult) {
        return server.requestGame(token, lazyResult);
    }

    public IServer.Message cancelGameRequest() {
        return server.cancelGameRequest(token);
    }

    public IServer.Message forfeit() {
        return server.forfeit(token);
    }

    public GameInstance getMyGame() {
        return server.getGameFromToken(token);
    }

    public IServer.Message play(int i, int j) {
        return server.play(token, i, j);
    }

    public Change checkForChange() {
        return server.checkForChange(token);
    }
}
