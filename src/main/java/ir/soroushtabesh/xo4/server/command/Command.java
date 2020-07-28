package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;

import java.io.IOException;
import java.net.Socket;

public interface Command {
    void visit(ServerListener listener, IServer server, Socket socket) throws IOException;
}
