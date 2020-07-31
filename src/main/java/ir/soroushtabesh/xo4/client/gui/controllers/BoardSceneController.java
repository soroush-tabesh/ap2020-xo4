package ir.soroushtabesh.xo4.client.gui.controllers;

import ir.soroushtabesh.xo4.client.PlayerManager;
import ir.soroushtabesh.xo4.client.utils.AnimationPool;
import ir.soroushtabesh.xo4.client.utils.AnimationUtil;
import ir.soroushtabesh.xo4.client.utils.FXUtil;
import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.PlayerController;
import ir.soroushtabesh.xo4.server.models.Change;
import ir.soroushtabesh.xo4.server.models.GameInstance;
import javafx.application.Platform;
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
    private void forfeitButton(ActionEvent actionEvent) {
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
        alert.setHeaderText("Match making");
        alert.setContentText("Finding opponent...");

        new Thread(() -> player.requestGame(gameInstance -> {
            Platform.runLater(alert::close);

            if (gameInstance == null) {
                System.err.println("Game instance is null.");
                return;
            }
            System.out.println("game instance: " + gameInstance);
            this.gameInstance = gameInstance;
            gameInstance.init();

            Platform.runLater(this::prepareGame);
        })).start();

        alert.showAndWait();
        IServer.Message request = player.cancelGameRequest(); // haha! magic!
        if (request == IServer.Message.SUCCESS)
            backPressed(null);
    }

    private void prepareGame() {
        xLabel.setText(gameInstance.getX_username());
        oLabel.setText(gameInstance.getO_username());
        playerController = PlayerManager.getInstance().getPlayer();
        int val = gameInstance.usernameToOrdinal(playerController.getPlayerBrief().getUsername());
        showTurn(gameInstance.getTurn());
        for (Button[] cell : cells)
            for (Button button : cell)
                if (val == GameInstance.X)
                    button.getStyleClass().add("button_X");
                else
                    button.getStyleClass().add("button_O");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Change change = PlayerManager.getInstance().getPlayer().checkForChange();
                if (change == null)
                    return;
                gameInstance.addChange(change);
                if (change.getActive() != null && !change.getActive())
                    Platform.runLater(() -> endRoutine());
                if (change.getForfeited() != null)
                    Platform.runLater(() -> showForfeit());
                if (change.getWinner() != null)
                    Platform.runLater(() -> showGameOver(change.getWinner()));
                if (change.getCi() != null)
                    Platform.runLater(() -> setButtonImage(change.getCi(), change.getCj(), change.getCval()));
                if (change.getTurn() != null)
                    Platform.runLater(() -> showTurn(change.getTurn()));
            }
        };
        timer.schedule(task, 0, 200);
    }

    private void endRoutine() {
        timer.cancel();
        timer.purge();
        showTurn(-1);
        forfeitButton.setText("Exit");
    }

    private void showForfeit() {
        FXUtil.showAlertInfo("Tic Tac Toe", "Forfeit"
                , "The game has been forfeited");
    }

    private void showGameOver(int winner) {
        FXUtil.showAlertInfo("Tic Tac Toe", "Game Over", "The winner is '"
                + gameInstance.ordinalToUsername(winner) + "'");
    }

    private void showTurn(int turn) {
        animationPool.stopAll();
        oLabel.setEffect(null);
        xLabel.setEffect(null);
        if (turn == GameInstance.X) {
            animationPool.startAnimation(xLabel, AnimationUtil.getPulse(xLabel));
            xLabel.setEffect(AnimationUtil.getGlowAnimated(Color.GREEN, 20, 70));
        } else if (turn == GameInstance.O) {
            animationPool.startAnimation(oLabel, AnimationUtil.getPulse(oLabel));
            oLabel.setEffect(AnimationUtil.getGlowAnimated(Color.GREEN, 20, 70));
        }
    }

    private void setButtonImage(int i, int j, int val) {
        cells[i][j].setStyle("-fx-opacity: 1;");
        cells[i][j].getStyleClass().removeAll("button_X", "button_Y");
        if (val == GameInstance.O)
            cells[i][j].setStyle("-fx-background-image: url('/ir/soroushtabesh/xo4/client/gui/image/O.png');");
        else if (val == GameInstance.X)
            cells[i][j].setStyle("-fx-background-image: url('/ir/soroushtabesh/xo4/client/gui/image/X.png');");
        else
            cells[i][j].setBackground(null);
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
        EventHandler<MouseEvent> mouseClick = event -> {
            if (gameInstance == null || !gameInstance.isActive())
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

                button.setOnMouseClicked(mouseClick);

                button.setStyle("-fx-background-color: transparent;" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-position: center");
                button.setMaxWidth(boardGrid.getPrefWidth() / 7);
                button.setMaxHeight(boardGrid.getPrefHeight() / 7);
                boardGrid.add(button, j, i);

                cells[i][j] = button;
            }
        }
    }
}
