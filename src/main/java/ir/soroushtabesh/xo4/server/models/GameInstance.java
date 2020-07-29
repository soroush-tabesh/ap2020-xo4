package ir.soroushtabesh.xo4.server.models;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class GameInstance {
    public static final int N = -1;
    public static final int X = 0;
    public static final int O = 1;

    private final int gid;
    private final int[][] cells = new int[7][7];
    private boolean active = true;
    private int turn = X;
    private final String x_username;
    private final String o_username;
    private int winner = N;
    private boolean forfeited;

    private transient final List<Change> changeList = new ArrayList<>();
    private transient int fwX, fwO;

    public GameInstance(String x_username, String o_username) {
        this.gid = new SecureRandom().nextInt();
        this.x_username = x_username;
        this.o_username = o_username;
    }

    public int getGid() {
        return gid;
    }

    public String getX_username() {
        return x_username;
    }

    public String getO_username() {
        return o_username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        Change change = new Change().setActive(active);
        changeList.add(change);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
        Change change = new Change().setTurn(turn);
        changeList.add(change);
    }

    public boolean isForfeited() {
        return forfeited;
    }

    public void setForfeited(boolean forfeited) {
        this.forfeited = forfeited;
        Change change = new Change().setForfeited(forfeited);
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
        Change change = new Change().setWinner(winner);
        changeList.add(change);
    }

    public void setWinner(String username) {
        if (username == null)
            return;
        if (username.equals(o_username))
            setWinner(O);
        else if (username.equals(x_username))
            setWinner(X);
    }

    public int getCell(int i, int j) {
        if (i >= 7 || i < 0 || j >= 7 || j < 0)
            return N;
        return cells[i][j];
    }

    public void setCell(int i, int j, int val) {
        if (i >= 7 || i < 0 || j >= 7 || j < 0)
            return;
        cells[i][j] = val;
        Change change = new Change().setCi(i).setCj(j).setCval(val);
        changeList.add(change);
    }

    public Change getNewChangeX() {
        if (fwX >= changeList.size())
            return null;
        return changeList.get(fwX++);
    }

    public Change getNewChangeO() {
        if (fwO >= changeList.size())
            return null;
        return changeList.get(fwO++);
    }

    public Change getNewChange(String username) {
        if (username == null)
            return null;
        if (username.equals(o_username))
            return getNewChangeO();
        else if (username.equals(x_username))
            return getNewChangeX();
        else
            return null;
    }

    public String ordinalToUsername(int i) {
        if (i == X)
            return x_username;
        else if (i == O)
            return o_username;
        return "";
    }

    public int usernameToOrdinal(String username) {
        if (username == null)
            return N;
        if (username.equals(o_username))
            return O;
        else if (username.equals(x_username))
            return X;
        else
            return N;
    }
}
