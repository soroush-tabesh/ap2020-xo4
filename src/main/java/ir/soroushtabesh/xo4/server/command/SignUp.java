package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;

import java.net.Socket;

public class SignUp implements Command<IServer.Message> {
    private final String username, password;

    public SignUp(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public IServer.Message visit(ServerListener listener, IServer server, Socket socket) {
        return server.signUp(username, password);
    }
}
