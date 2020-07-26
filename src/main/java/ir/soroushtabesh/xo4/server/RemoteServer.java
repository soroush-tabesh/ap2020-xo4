package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.client.PlayerManager;
import ir.soroushtabesh.xo4.client.models.Config;
import ir.soroushtabesh.xo4.server.command.Command;
import ir.soroushtabesh.xo4.server.command.CommandPacket;
import ir.soroushtabesh.xo4.server.command.Login;
import ir.soroushtabesh.xo4.server.command.SignUp;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class RemoteServer implements IServer {

    private static final RemoteServer instance = new RemoteServer();
    private Config config = new Config();
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private RemoteServer() {
    }

    public static RemoteServer getInstance() {
        return instance;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    public boolean connect() {
        if (isConnected())
            return true;
        try {
            socket = new Socket(InetAddress.getByName(config.getAddress()), config.getPort());
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Message signUp(String username, String password) {
        if (!isConnected())
            return Message.ERROR;
        sendCommand(new SignUp(username, password));
        Message message = receiveResponse(Message.class);
        if (message == null)
            return Message.ERROR;
        return message;
    }

    @Override
    public PlayerController login(String username, String password) {
        if (!isConnected())
            return null;
        sendCommand(new Login(username, password));
        PlayerController playerController = receiveResponse(PlayerController.class);
        PlayerManager.getInstance().setPlayer(playerController);
        return playerController;
    }

    @Override
    public int[] getAllRunningGames() {
        if (!isConnected())
            return null;
        return new int[0];
    }

    @Override
    public GameInstance getGame(int id) {
        if (!isConnected())
            return null;
        return null;
    }

    private void sendCommand(Command command) {
        try {
            outputStream.writeUTF(CommandPacket.toJson(command));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> T receiveResponse(Class<T> clz) {
        try {
            String s = inputStream.readUTF();
            return JSONUtil.getGson().fromJson(s, clz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
