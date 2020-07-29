package ir.soroushtabesh.xo4.client.gui.controllers;

import ir.soroushtabesh.xo4.client.PlayerManager;
import ir.soroushtabesh.xo4.client.utils.AnimationPool;
import ir.soroushtabesh.xo4.client.utils.AnimationUtil;
import ir.soroushtabesh.xo4.client.utils.FXUtil;
import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.PlayerController;
import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BoardSceneController extends AbstractSceneController {
    @FXML
    private Button forfeitButton;
    @FXML
    private GridPane boardGrid;
    @FXML
    private Label youLabel;
    @FXML
    private Label oLabel;
    @FXML
    private Label xLabel;

    private final Button[][] cells = new Button[7][7];
    private GameInstance gameInstance;
    private PlayerController playerController;

    private final Timer timer = new Timer();
    private final AnimationPool animationPool = new AnimationPool();

    @FXML
    private void forfeit(ActionEvent actionEvent) {
        if (gameInstance.isActive())
            playerController.forfeit();
        else
            backPressed(null);
    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);

        PlayerController player = PlayerManager.getInstance().getPlayer();
        youLabel.setText(player.getPlayerBrief().getUsername());
        forfeitButton.setText("Forfeit");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Node button = alert.getDialogPane().lookupButton(ButtonType.OK);
        button.setDisable(true);
        alert.setTitle("Tic tac toe");
        alert.setContentText("Finding opponent...");

        new Thread(() -> player.requestGame(gameInstance -> {
            alert.close();
            if (gameInstance == null)
                return;
            this.gameInstance = gameInstance;
            prepareGame();
        })).start();

        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.CANCEL)) {
            IServer.Message request = player.cancelGameRequest();
            if (request == IServer.Message.SUCCESS)
                backPressed(null);
        }
    }

    private void prepareGame() {
        xLabel.setText(gameInstance.getX_username());
        oLabel.setText(gameInstance.getO_username());
        playerController = PlayerManager.getInstance().getPlayer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Change change = PlayerManager.getInstance().getPlayer().checkForChange();
                if (change.getActive() != null) {
                    gameInstance.setActive(change.getActive());
                    if (!change.getActive()) {
                        timer.cancel();
                        timer.purge();
                        showTurn(-1);
                        forfeitButton.setText("Exit");
                    }
                }
                if (change.getForfeited() != null) {
                    gameInstance.setForfeited(change.getForfeited());
                    FXUtil.showAlertInfo("Tic Tac Toe", "Forfeit", "The game has been " +
                            "forfeited by one of the players");
                }
                if (change.getWinner() != null) {
                    gameInstance.setWinner(change.getWinner());
                    FXUtil.showAlertInfo("Tic Tac Toe", "Game Over", "The winner is '"
                            + gameInstance.ordinalToUsername(change.getWinner()) + "'");
                }
                if (change.getCi() != null) {
                    gameInstance.setCell(change.getCi(), change.getCj(), change.getCval());
                    setButtonImage(change.getCi(), change.getCj(), change.getCval());
                }
                if (change.getTurn() != null) {
                    gameInstance.setTurn(change.getTurn());
                    showTurn(change.getTurn());
                }
            }
        };
        timer.schedule(task, 0, 200);
    }

    private void showTurn(int turn) {
        animationPool.stopAll();
        oLabel.setEffect(null);
        xLabel.setEffect(null);
        if (turn == GameInstance.X) {
            animationPool.startAnimation(xLabel, AnimationUtil.getPassiveBounce(xLabel));
            xLabel.setEffect(AnimationUtil.getGlowAnimated(Color.GREEN, 20, 70));
        } else if (turn == GameInstance.O) {
            animationPool.startAnimation(oLabel, AnimationUtil.getPassiveBounce(oLabel));
            oLabel.setEffect(AnimationUtil.getGlowAnimated(Color.GREEN, 20, 70));
        }
    }

    private void setButtonImage(int i, int j, int val) {
        cells[i][j].setStyle("-fx-opacity: 1;");
        if (val == GameInstance.O)
            cells[i][j].setStyle("-fx-background-image: url('/ir/soroushtabesh/xo4/client/gui/image/O.png');");
        else if (val == GameInstance.X)
            cells[i][j].setStyle("-fx-background-image: url('/ir/soroushtabesh/xo4/client/gui/image/X.png');");
        else
            cells[i][j].setStyle("-fx-background-image: none;");
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
        timer.purge();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        EventHandler<MouseEvent> mouseEnter = event -> {
            if (!gameInstance.isActive())
                return;
            Button source = (Button) event.getSource();
            int i = (int) source.getProperties().get("i");
            int j = (int) source.getProperties().get("j");
            if (gameInstance.getCell(i, j) == GameInstance.N) {
                setButtonImage(i, j, gameInstance.usernameToOrdinal(playerController.getPlayerBrief().getUsername()));
                cells[i][j].setStyle("-fx-opacity: 0.5;");
            }
        };
        EventHandler<MouseEvent> mouseExit = event -> {
            if (!gameInstance.isActive())
                return;
            Button source = (Button) event.getSource();
            int i = (int) source.getProperties().get("i");
            int j = (int) source.getProperties().get("j");
            setButtonImage(i, j, gameInstance.getCell(i, j));
        };
        EventHandler<MouseEvent> mouseClick = event -> {
            if (!gameInstance.isActive())
                return;
            Button source = (Button) event.getSource();
            int i = (int) source.getProperties().get("i");
            int j = (int) source.getProperties().get("j");
            if (gameInstance.getCell(i, j) != GameInstance.N ||
                    gameInstance.getTurn() !=
                            gameInstance.usernameToOrdinal(playerController.getPlayerBrief().getUsername()))
                return;
            playerController.play(i, j);
        };
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Button button = new Button();
                button.getProperties().put("i", i);
                button.getProperties().put("j", j);

                button.setOnMouseEntered(mouseEnter);
                button.setOnMouseExited(mouseExit);
                button.setOnMouseClicked(mouseClick);

                button.setStyle("-fx-background-color: transparent; -fx-background-size: stretch;");
                button.setMaxWidth(Double.MAX_VALUE);
                boardGrid.add(button, j, i);

                cells[i][j] = button;
            }
        }
    }
}
