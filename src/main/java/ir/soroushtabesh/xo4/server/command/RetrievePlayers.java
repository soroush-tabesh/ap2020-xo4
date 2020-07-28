package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RetrievePlayers implements Command {
    @Override
    public void visit(ServerListener listener, IServer server, Socket socket) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        PlayerBrief[] allPlayers = server.getAllPlayers();
        try {
            outputStream.writeUTF(JSONUtil.getGson().toJson(allPlayers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
