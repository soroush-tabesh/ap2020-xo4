package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.models.GameInstance;

import java.net.Socket;

public class RetrieveGameByID implements Command<GameInstance> {
    private final int gameID;

    public RetrieveGameByID(int gameID) {
        this.gameID = gameID;
    }

    @Override
    public GameInstance visit(ServerListener listener, IServer server, Socket socket) {
        return server.getGameByID(gameID);
    }
}
