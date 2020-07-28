package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.models.GameInstance;

import java.net.Socket;

public class RetrieveGame implements Command<GameInstance> {
    private final long token;

    public RetrieveGame(long token) {
        this.token = token;
    }

    @Override
    public GameInstance visit(ServerListener listener, IServer server, Socket socket) {
        return server.getGameFromToken(token);
    }
}
