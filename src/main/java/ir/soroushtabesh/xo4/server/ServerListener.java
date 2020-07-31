package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.command.Command;
import ir.soroushtabesh.xo4.server.command.CommandPacket;
import ir.soroushtabesh.xo4.server.models.Config;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;
import ir.soroushtabesh.xo4.server.utils.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerListener implements Runnable {

    private boolean running = false;
    private final Config config;
    private final IServer server;
    private ServerSocket serverSocket;
    private final Thread thread;
    private final Map<Socket, Long> socket2token = new HashMap<>();

    public ServerListener(IServer server, Config config) {
        this.config = config;
        this.server = server;
        thread = new Thread(this);
    }

    public synchronized void startServer() {
        if (!isRunning()) {
            Logger.log("ServerListener", "server start");
            running = true;
            try {
                serverSocket = new ServerSocket(config.getPort());
                System.out.println(serverSocket.getLocalPort());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            thread.start();
            System.out.println("Server started.");
        }
    }

    @Override
    public void run() {
        while (isRunning()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (!isRunning())
                    return;
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new ClientWorker(clientSocket).start();
        }
    }

    public synchronized void stopServer() {
        if (isRunning()) {
            running = false;
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerSocket(Socket socket, long token) {
        socket2token.put(socket, token);
    }

    public void unregisterSocket(Socket socket) {
        socket2token.remove(socket);
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public void reportBroken(Socket socket) {
        System.err.println("Broken pipe: " + socket.getRemoteSocketAddress());
        try {
            server.logout(socket2token.getOrDefault(socket, 0L));
            unregisterSocket(socket);
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private class ClientWorker extends Thread {

        private final Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        public ClientWorker(Socket socket) {
            this.socket = socket;
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.err.println("cannot connect to " + socket.getRemoteSocketAddress());
//                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String s;
                while (isRunning()) {
                    s = inputStream.readUTF();
                    Command<?> command = CommandPacket.fromJson(s);
                    if (command == null) {
                        System.err.println("unknown command from "
                                + socket.getRemoteSocketAddress() + ":" + socket.getPort());
                        continue;
                    }
                    outputStream.writeUTF(JSONUtil.getGson().toJson(
                            command.visit(ServerListener.this, server, socket)));
                }
            } catch (Exception e) {
//                e.printStackTrace();
                reportBroken(socket);
            }
        }
    }
}
