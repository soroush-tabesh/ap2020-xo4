package ir.soroushtabesh.xo4;

import ir.soroushtabesh.xo4.server.LocalServer;
import ir.soroushtabesh.xo4.server.ServerListener;
import ir.soroushtabesh.xo4.server.models.Config;
import ir.soroushtabesh.xo4.server.utils.ConfigLoader;

import java.io.File;

public class ServerMain {
    public static void main(String[] args) {
        Config config = new Config();
        if (args.length > 0)
            config = ConfigLoader.loadConfig(new File(args[0]));
        ServerListener serverListener = new ServerListener(new LocalServer(), config);
        serverListener.startServer();
    }
}
