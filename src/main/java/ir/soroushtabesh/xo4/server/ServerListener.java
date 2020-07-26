package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.command.Command;
import ir.soroushtabesh.xo4.server.command.CommandPacket;
import ir.soroushtabesh.xo4.server.models.Config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener implements Runnable {

    private boolean running = false;
    private Config config;
    private IServer server;
    private ServerSocket serverSocket;
    private Thread thread;

    public ServerListener(IServer server, Config config) {
        this.config = config;
        this.server = server;
        thread = new Thread(this);
    }

    public synchronized void startServer() {
        if (!isRunning()) {
            running = true;
            try {
                serverSocket = new ServerSocket(config.getPort());
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
            Socket clientSocket = null;
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

    public synchronized boolean isRunning() {
        return running;
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
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String s;
                while (isRunning()) {
                    s = inputStream.readUTF();
                    Command command = CommandPacket.fromJson(s);
                    if (command == null) {
                        System.err.println("unknown command from "
                                + socket.getRemoteSocketAddress() + ":" + socket.getPort());
                        continue;
                    }
                    command.visit(server, outputStream);
                }
            } catch (Exception e) {
                System.err.println("Broken pipe: " + socket.getRemoteSocketAddress() + ":" + socket.getPort());
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
