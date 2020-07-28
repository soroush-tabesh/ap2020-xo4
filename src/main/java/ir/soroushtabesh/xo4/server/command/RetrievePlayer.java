package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RetrievePlayer implements Command {

    private final String username;

    public RetrievePlayer(String username) {
        this.username = username;
    }

    @Override
    public void visit(ServerListener listener, IServer server, Socket socket) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        PlayerBrief player = server.getPlayer(username);
        try {
            outputStream.writeUTF(JSONUtil.getGson().toJson(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}