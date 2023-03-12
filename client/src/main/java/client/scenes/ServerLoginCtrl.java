package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ServerLoginCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField serverURLTextField;
    @FXML
    private Label warningLabel;

    @Inject
    public ServerLoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void refresh() {
        warningLabel.setVisible(false);
    }

    public void connectToServer() {
        String serverURL = serverURLTextField.getText();
        server.setServer(serverURL);
        if (server.isServerOk())
            mainCtrl.showListOverview();
        else
            showWarning("Wrong Server URL");
    }

    public void showWarning(String warningMessage) {
        warningLabel.setText(warningMessage);
        warningLabel.setVisible(true);
    }

}
