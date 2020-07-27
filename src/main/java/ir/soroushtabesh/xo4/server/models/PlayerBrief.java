package ir.soroushtabesh.xo4.server.models;

public class PlayerBrief {
    private final String username;
    private final Player.State state;
    private final int win, lose, score;

    public PlayerBrief(Player player) {
        this.username = player.getUsername();
        this.state = player.getState();
        this.win = player.getWin();
        this.lose = player.getLose();
        this.score = player.getScore();
    }

    public static PlayerBrief[] buildAll(Player[] players) {
        PlayerBrief[] briefs = new PlayerBrief[players.length];
        for (int i = 0; i < players.length; i++)
            briefs[i] = players[i].getBriefData();
        return briefs;
    }

    public String getUsername() {
        return username;
    }

    public Player.State getState() {
        return state;
    }

    public int getWin() {
        return win;
    }

    public int getLose() {
        return lose;
    }

    public int getScore() {
        return score;
    }
}
