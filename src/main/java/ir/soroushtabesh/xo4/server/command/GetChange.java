package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.models.Change;

import java.net.Socket;

public class GetChange implements Command<Change> {
    private final long token;

    public GetChange(long token) {
        this.token = token;
    }

    @Override
    public Change visit(ServerListener listener, IServer server, Socket socket) {
        return server.checkForChange(token);
    }
}
