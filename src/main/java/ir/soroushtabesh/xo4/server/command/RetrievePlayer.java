package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;

import java.net.Socket;

public class RetrievePlayer implements Command<PlayerBrief> {

    private final String username;

    public RetrievePlayer(String username) {
        this.username = username;
    }

    @Override
    public PlayerBrief visit(ServerListener listener, IServer server, Socket socket) {
        return server.getPlayer(username);
    }
}