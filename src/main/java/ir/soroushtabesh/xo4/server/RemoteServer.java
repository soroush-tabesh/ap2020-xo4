package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.client.PlayerManager;
import ir.soroushtabesh.xo4.client.models.Config;
import ir.soroushtabesh.xo4.server.command.*;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;
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
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
            inputStream = null;
            outputStream = null;
        }
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
        PlayerController controller = receiveResponse(PlayerController.class);
        if (controller == null)
            return null;
        controller.setServer(this);
        PlayerManager.getInstance().setPlayer(controller);
        return controller;
    }

    @Override
    public int[] getAllRunningGames() {
        if (!isConnected())
            return null;
        return null;
    }

    @Override
    public GameInstance getGame(int id) {
        if (!isConnected())
            return null;
        return null;
    }

    @Override
    public PlayerBrief[] getAllPlayers() {
        sendCommand(new RetrievePlayers());
        return receiveResponse(PlayerBrief[].class);
    }

    @Override
    public PlayerBrief getPlayer(String username) {
        sendCommand(new RetrievePlayer(username));
        return receiveResponse(PlayerBrief.class);
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
