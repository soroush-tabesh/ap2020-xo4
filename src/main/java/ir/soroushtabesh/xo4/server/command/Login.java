package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.PlayerController;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataOutputStream;
import java.io.IOException;

public class Login implements Command {
    private final String username, password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void visit(IServer server, DataOutputStream outputStream) {
        PlayerController playerController = server.login(username, password);
        try {
            outputStream.writeUTF(JSONUtil.getGson().toJson(playerController));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
