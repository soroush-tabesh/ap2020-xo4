package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;

import java.net.Socket;

public class RetrieveRunningGames implements Command<int[]> {
    @Override
    public int[] visit(ServerListener listener, IServer server, Socket socket) {
        return server.getRunningGames();
    }
}
