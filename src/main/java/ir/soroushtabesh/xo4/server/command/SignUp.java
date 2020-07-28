package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SignUp implements Command {
    private final String username, password;

    public SignUp(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void visit(ServerListener listener, IServer server, Socket socket) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        IServer.Message message = server.signUp(username, password);
        try {
            outputStream.writeUTF(JSONUtil.getGson().toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
