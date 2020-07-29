package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;

import java.net.Socket;

public class Logout implements Command<IServer.Message> {
    private final long token;

    public Logout(long token) {
        this.token = token;
    }

    @Override
    public IServer.Message visit(ServerListener listener, IServer server, Socket socket) {
        listener.unregisterSocket(socket);
        return server.logout(token);
    }
}
