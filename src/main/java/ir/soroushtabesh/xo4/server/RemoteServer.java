package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.client.PlayerManager;
import ir.soroushtabesh.xo4.client.models.Config;
import ir.soroushtabesh.xo4.server.command.*;
import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        Message message = communicate(new SignUp(username, password), Message.class);
        if (message == null)
            return Message.ERROR;
        return message;
    }

    @Override
    public PlayerController login(String username, String password) {
        PlayerController controller = communicate(new Login(username, password), PlayerController.class);
        if (controller == null)
            return null;
        controller.setServer(this);
        PlayerManager.getInstance().setPlayer(controller);
        return controller;
    }

    @Override
    public Message logout(long token) {
        return communicate(new Logout(token), Message.class);
    }

    @Override
    public int[] getOldGames() {
        return communicate(new RetrieveOldGames(), int[].class);
    }

    @Override
    public int[] getRunningGames() {
        return communicate(new RetrieveRunningGames(), int[].class);
    }

    @Override
    public GameInstance getGameByID(int gid) {
        return communicate(new RetrieveGameByID(gid), GameInstance.class);
    }

    @Override
    public PlayerBrief[] getAllPlayers() {
        return communicate(new RetrievePlayers(), PlayerBrief[].class);
    }

    @Override
    public PlayerBrief getPlayer(String username) {
        return communicate(new RetrievePlayer(username), PlayerBrief.class);
    }

    @Override
    public Message requestGame(long token) {
        return communicate(new GameRequest(token), Message.class);
    }

    @Override
    public Message cancelGameRequest(long token) {
        return communicate(new CancelRequest(token), Message.class);
    }

    @Override
    public Message forfeit(long token) {
        return communicate(new Forfeit(token), Message.class);
    }

    @Override
    public GameInstance getGameFromToken(long token) {
        return communicate(new RetrieveGame(token), GameInstance.class);
    }

    @Override
    public Message play(long token, int i, int j) {
        return communicate(new Play(token, i, j), Message.class);
    }

    @Override
    public Change checkForChange(long token) {
        return communicate(new GetChange(token), Change.class);
    }

    private final Lock comLock = new ReentrantLock(true);

    private <T, E extends T> T communicate(Command<E> command, Class<T> clz) {
        comLock.lock();
        try {
            if (!isConnected())
                return null;
            outputStream.writeUTF(CommandPacket.toJson(command));
            String s = inputStream.readUTF();
            return JSONUtil.getGson().fromJson(s, clz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            comLock.unlock();
        }
    }

}
