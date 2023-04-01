package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class AdminBoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button disconnectButton;

    @Inject
    public AdminBoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void disconnectFromServer(){
        mainCtrl.disconnectFromServer();
    }

    public void disconnectButtonOnMouseEntered (MouseEvent event) {
        disconnectButton.setStyle("-fx-background-color: #b0bfd4; -fx-border-color: #6D85A8");
    }

    public void disconnectButtonOnMouseExited (MouseEvent event) {
        disconnectButton.setStyle("-fx-background-color: #d1dae6; -fx-border-color: #6D85A8");
    }
}
