package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataOutputStream;
import java.io.IOException;

public class SignUp implements Command {
    private final String username, password;

    public SignUp(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void visit(IServer server, DataOutputStream outputStream) {
        IServer.Message message = server.signUp(username, password);
        try {
            outputStream.writeUTF(JSONUtil.getGson().toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
