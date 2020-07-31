package ir.soroushtabesh.xo4.client.gui.controllers;

import ir.soroushtabesh.xo4.client.PlayerManager;
import ir.soroushtabesh.xo4.client.gui.BoardScene;
import ir.soroushtabesh.xo4.client.gui.LoginScene;
import ir.soroushtabesh.xo4.client.utils.FXUtil;
import ir.soroushtabesh.xo4.server.PlayerController;
import ir.soroushtabesh.xo4.server.RemoteServer;
import ir.soroushtabesh.xo4.server.models.PlayerBrief;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MenuSceneController extends AbstractSceneController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label wlLabel;
    @FXML
    private TableView<PlayerBrief> tableView;

    private ObservableList<PlayerBrief> briefs;
    private SortedList<PlayerBrief> sortedBriefs;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        briefs = FXCollections.observableArrayList();
        sortedBriefs = new SortedList<>(briefs
                , (o1, o2) -> new CompareToBuilder()
                .append(o2.getScore(), o1.getScore())
                .append(o2.getState().ordinal(), o1.getState().ordinal())
                .build());
        tableView.getColumns().clear();
        TableColumn<PlayerBrief, String>
                usernameColumn = new TableColumn<>("Username"),
                scoreColumn = new TableColumn<>("Score"),
                winColumn = new TableColumn<>("Win"),
                loseColumn = new TableColumn<>("Lose"),
                stateColumn = new TableColumn<>("State");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        winColumn.setCellValueFactory(new PropertyValueFactory<>("win"));
        loseColumn.setCellValueFactory(new PropertyValueFactory<>("lose"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        tableView.getColumns().addAll(Arrays.asList(usernameColumn, scoreColumn, stateColumn, winColumn, loseColumn));
        tableView.getColumns().forEach(column -> column.setMinWidth(100));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Bindings.bindContent(tableView.getItems(), sortedBriefs);
    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                refreshScreen();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
        timer.purge();
    }

    @FXML
    private void refreshScreen() {
        RemoteServer server = RemoteServer.getInstance();
        PlayerController player = PlayerManager.getInstance().getPlayer();
        player.updateBrief();
        PlayerBrief[] allPlayers = server.getAllPlayers();
        PlayerBrief myBrief = player.getPlayerBrief();
        FXUtil.runLater(() -> {
            usernameLabel.setText(myBrief.getUsername());
            wlLabel.setText(String.format("%d/%d", myBrief.getWin(), myBrief.getLose()));
            scoreLabel.setText(myBrief.getScore() + "");
            briefs.clear();
            briefs.addAll(Arrays.asList(allPlayers));
        }, 0);
    }

    @FXML
    private void newMatch(ActionEvent actionEvent) {
        SceneManager.getInstance().showScene(BoardScene.class);
    }

    @FXML
    private void exitBtn(ActionEvent actionEvent) {
        PlayerManager instance = PlayerManager.getInstance();
        instance.getPlayer().logout();
        instance.setPlayer(null);
        SceneManager.getInstance().showScene(LoginScene.class);
    }
}
