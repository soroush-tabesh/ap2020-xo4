package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;

import java.io.IOException;
import java.net.Socket;

public class Logout implements Command {
    private final long token;

    public Logout(long token) {
        this.token = token;
    }

    @Override
    public void visit(ServerListener listener, IServer server, Socket socket) throws IOException {
        server.logout(token);
    }
}
