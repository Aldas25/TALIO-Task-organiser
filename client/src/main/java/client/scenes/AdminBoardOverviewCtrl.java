package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminBoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ImageView disconnectImageView;

    @Inject
    public AdminBoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDisconnectImageView();
    }

    public void resetDisconnectImageView () {
        File disconnectFile = new
                File ("client/src/main/java/client/images/admin-board-overview/disconnect1.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void disconnectFromServer(){
        mainCtrl.disconnectFromServer();
    }

    public void disconnectOnMouseEntered (MouseEvent event) {
        File disconnectFile = new
                File ("client/src/main/java/client/images/admin-board-overview/disconnect2.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void disconnectOnMouseExited (MouseEvent event) {
        resetDisconnectImageView();
    }
}
