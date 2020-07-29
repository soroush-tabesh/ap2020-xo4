package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;

import java.net.Socket;

public class Play implements Command<IServer.Message> {
    private final long token;
    private final int i, j;

    public Play(long token, int i, int j) {
        this.token = token;
        this.i = i;
        this.j = j;
    }

    @Override
    public IServer.Message visit(ServerListener listener, IServer server, Socket socket) {
        return server.play(token, i, j);
    }
}
