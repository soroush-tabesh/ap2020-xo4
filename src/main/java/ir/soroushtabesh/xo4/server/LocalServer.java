package ir.soroushtabesh.xo4.server;

import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import ir.soroushtabesh.xo4.server.models.Player;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalServer implements IServer {

    private final Map<Long, GameInstance> token2game = new HashMap<>();
    private final Map<Integer, GameInstance> gid2game = new HashMap<>();
    private Long waitingToken;
    private LazyResult<GameInstance> waitingLazyResult;

    @Override
    public Message signUp(String username, String password) {
        return DataManager.getInstance().addPlayer(username, password);
    }

    @Override
    public PlayerController login(String username, String password) {
        DataManager dataManager = DataManager.getInstance();
        PlayerController controller = dataManager.authenticate(username, password);
        dataManager.setPlayerState(username, Player.State.ONLINE);
        if (controller == null)
            return null;
        controller.setServer(this);
        return controller;
    }

    @Override
    public Message logout(long token) {
        DataManager dataManager = DataManager.getInstance();
        Player player = dataManager.getPlayer(token);
        dataManager.expire(token);
        dataManager.setPlayerState(player, Player.State.OFFLINE);
        return Message.SUCCESS;
    }

    @Override
    public PlayerBrief[] getAllPlayers() {
        List<Player> allPlayers = DataManager.getInstance().getAllPlayers();
        PlayerBrief[] playerBriefs = new PlayerBrief[allPlayers.size()];
        int i = 0;
        for (Player player : allPlayers)
            playerBriefs[i++] = player.getBriefData();
        return playerBriefs;
    }

    @Override
    public PlayerBrief getPlayer(String username) {
        return DataManager.getInstance().getPlayer(username).getBriefData();
    }

    private final Lock lock = new ReentrantLock(true);

    @Override
    public Message requestGame(long token, LazyResult<GameInstance> lazyResult) {
        lock.lock();
        try {
            if (waitingToken == null
                    || DataManager.getInstance().getPlayer(waitingToken) == null
                    || DataManager.getInstance().getPlayer(waitingToken).getState() != Player.State.ONLINE) {
                waitingToken = token;
                waitingLazyResult = lazyResult;
                return Message.WAIT;
            } else {
                GameInstance gameInstance = new GameInstance(
                        DataManager.getInstance().tokenToUsername(waitingToken),
                        DataManager.getInstance().tokenToUsername(token));
                DataManager dataManager = DataManager.getInstance();

                token2game.put(waitingToken, gameInstance);
                token2game.put(token, gameInstance);
                gid2game.put(gameInstance.getGid(), gameInstance);

                new Thread(() -> {
                    LazyResult<GameInstance> l1 = waitingLazyResult;
                    waitingToken = null;
                    waitingLazyResult = null;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    l1.call(gameInstance);
                    lazyResult.call(gameInstance);
                }).start();

                dataManager.setPlayerState(gameInstance.getO_username(), Player.State.PLAYING);
                dataManager.setPlayerState(gameInstance.getX_username(), Player.State.PLAYING);

                return Message.SUCCESS;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message cancelGameRequest(long token) {
        lock.lock();
        try {
            System.out.println("LocalServer.cancelGameRequest");
            System.out.println(waitingToken);
            if (waitingToken == null || waitingToken != token)
                return Message.WRONG;
            waitingToken = null;
            waitingLazyResult.call(null);
            return Message.SUCCESS;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message forfeit(long token) {
        GameInstance gameInstance = token2game.get(token);
        DataManager dataManager = DataManager.getInstance();
        if (gameInstance == null || !gameInstance.isActive())
            return Message.WRONG;
        String username = dataManager.tokenToUsername(token);
        gameInstance.setForfeited(true);
        gameInstance.setWinner(1 - gameInstance.usernameToOrdinal(username));
        endRoutine(gameInstance);
        return Message.SUCCESS;
    }

    @Override
    public GameInstance getGameFromToken(long token) {
        return token2game.get(token);
    }

    @Override
    public int[] getOldGames() {
        return DataManager.getInstance().getOldGames();
    }

    @Override
    public int[] getRunningGames() {
        Set<Integer> integers = gid2game.keySet();
        int[] res = new int[integers.size()];
        int i = 0;
        for (Integer integer : integers) {
            res[i++] = integer;
        }
        return res;
    }

    @Override
    public GameInstance getGameByID(int gid) {
        GameInstance gameInstance = gid2game.get(gid);
        if (gameInstance == null)
            gameInstance = DataManager.getInstance().getGame(gid);
        return gameInstance;
    }

    @Override
    public Message play(long token, int i, int j) {
        DataManager dataManager = DataManager.getInstance();
        Player player = dataManager.getPlayer(token);
        if (player == null)
            return Message.ERROR;
        GameInstance gameInstance = token2game.get(token);
        if (gameInstance == null)
            return Message.WAIT;

        //check turn
        if (!gameInstance.ordinalToUsername(gameInstance.getTurn()).equals(player.getUsername()))
            return Message.WRONG;

        //check cell validity
        if (gameInstance.getCell(i, j) != GameInstance.N)
            return Message.WRONG;

        //occupy cell
        if (i >= 7 || i < 0 || j >= 7 || j < 0)
            return Message.WRONG;
        gameInstance.setCell(i, j, gameInstance.usernameToOrdinal(player.getUsername()));

        //check for game end
        boolean win = checkWinner(gameInstance, i, j, 0, 1);
        win |= checkWinner(gameInstance, i, j, 1, 0);
        win |= checkWinner(gameInstance, i, j, 1, 1);
        win |= checkWinner(gameInstance, i, j, 1, -1);

        if (win) {
            gameInstance.setWinner(gameInstance.usernameToOrdinal(player.getUsername()));
            endRoutine(gameInstance);
        }

        gameInstance.setTurn(1 - gameInstance.getTurn());
        return Message.SUCCESS;
    }

    private void endRoutine(GameInstance gameInstance) {
        DataManager dataManager = DataManager.getInstance();
        gameInstance.setActive(false);
        dataManager.saveGame(gameInstance);

        Player playerO = dataManager.getPlayer(gameInstance.getO_username());
        Player playerX = dataManager.getPlayer(gameInstance.getX_username());
        playerO.setState(Player.State.ONLINE);
        playerX.setState(Player.State.ONLINE);
        if (gameInstance.getWinner() == GameInstance.X) {
            playerX.setWin(playerX.getWin() + 1);
            playerO.setLose(playerO.getLose() + 1);
        } else if (gameInstance.getWinner() == GameInstance.O) {
            playerO.setWin(playerO.getWin() + 1);
            playerX.setLose(playerX.getLose() + 1);
        }
        dataManager.updatePlayer(playerO);
        dataManager.updatePlayer(playerX);
    }

    private boolean checkWinner(GameInstance gameInstance, int i, int j, int vi, int vj) {
        if (gameInstance.getCell(i, j) == GameInstance.N)
            return false;
        int cnt = 0, mx = 0;
        for (int t = -3; t <= 3; t++) {
            if (gameInstance.getCell(i, j) == gameInstance.getCell(i + t * vi, j + t * vj)) {
                ++cnt;
            } else {
                mx = Math.max(mx, cnt);
                cnt = 0;
            }
        }
        mx = Math.max(mx, cnt);
        return mx >= 4;
    }

    @Override
    public Change checkForChange(long token) {
        GameInstance gameInstance = token2game.get(token);
        if (gameInstance == null)
            return null;
        return gameInstance.getNewChange(DataManager.getInstance().tokenToUsername(token));
    }
}
