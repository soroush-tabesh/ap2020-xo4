package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.Player;
import ir.soroushtabesh.xo4.server.utils.DBUtil;
import ir.soroushtabesh.xo4.server.utils.HashUtil;
import ir.soroushtabesh.xo4.server.utils.Logger;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static final DataManager instance = new DataManager();

    private final Map<Long, String> token2un = new HashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    private DataManager() {
    }

    public static DataManager getInstance() {
        return instance;
    }

    public boolean checkPlayerValidity(String username, long token) {
        return username.equals(tokenToUsername(token));
    }

    public String tokenToUsername(long token) {
        return token2un.get(token);
    }

    public IServer.Message addPlayer(String username, String password) {
        password = HashUtil.hash(password);
        Player player = new Player(username, password);
        ///
        player.setWin(secureRandom.nextInt(10));
        player.setLose(secureRandom.nextInt(10));
        player.setScore(player.getWin() - player.getLose());
        ///
        return DBUtil.doInJPA(session -> {
            try {
                boolean exists = session.createQuery("from Player where username=:un")
                        .setParameter("un", username).uniqueResult() != null;
                if (exists)
                    return IServer.Message.EXISTS;
                session.save(player);
            } catch (Exception e) {
                e.printStackTrace();
                return IServer.Message.ERROR;
            }
            Logger.log("PlayerManager", "sign-up: " + player.getUsername());
            return IServer.Message.SUCCESS;
        });
    }

    public PlayerController authenticate(String username, String password) {
        password = HashUtil.hash(password);
        Player player = DBUtil.doInJPA(session ->
                (Player) session.createQuery("from Player where username=:un")
                        .setParameter("un", username).uniqueResult());
        if (player == null || !player.getPassword().equals(password))
            return null;
        long token = secureRandom.nextLong();
        token2un.put(token, username);
        return new PlayerController(player.getBriefData(), token);
    }

    public List<Player> getAllPlayers() {
        return DBUtil.doInJPA(session -> session.createQuery("from Player ", Player.class).list());
    }

    public Player getPlayer(String username) {
        return DBUtil.doInJPA(session ->
                session.createQuery("from Player where username=:un", Player.class)
                        .setParameter("un", username).uniqueResult());

    }

}
