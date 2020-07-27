package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataOutputStream;
import java.io.IOException;

public class RetrievePlayer implements Command {

    private final String username;

    public RetrievePlayer(String username) {
        this.username = username;
    }

    @Override
    public void visit(IServer server, DataOutputStream outputStream) {
        PlayerBrief player = server.getPlayer(username);
        try {
            outputStream.writeUTF(JSONUtil.getGson().toJson(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}