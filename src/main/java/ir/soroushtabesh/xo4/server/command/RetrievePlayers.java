package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataOutputStream;
import java.io.IOException;

public class RetrievePlayers implements Command {
    @Override
    public void visit(IServer server, DataOutputStream outputStream) {
        PlayerBrief[] allPlayers = server.getAllPlayers();
        try {
            outputStream.writeUTF(JSONUtil.getGson().toJson(allPlayers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
