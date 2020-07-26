package ir.soroushtabesh.xo4.server;


public class PlayerController {

    private final String username;
    private long token;
    private transient IServer server;

    public PlayerController(String username) {
        this.username = username;
    }

    public PlayerController(String username, long token) {
        this.username = username;
        this.token = token;
    }

    public void setServer(IServer server) {
        this.server = server;
    }
}
