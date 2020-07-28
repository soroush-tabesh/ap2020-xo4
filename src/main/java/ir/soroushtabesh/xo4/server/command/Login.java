package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.PlayerController;
import ir.soroushtabesh.xo4.server.ServerListener;

import java.net.Socket;

public class Login implements Command<PlayerController> {
    private final String username, password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public PlayerController visit(ServerListener listener, IServer server, Socket socket) {
        return server.login(username, password);
    }

}
