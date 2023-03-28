package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardTemplateCtrl implements Initializable {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private Button viewBoardButton;
    @FXML
    private TextField boardTitle;
    @FXML
    private ImageView boardDeleteImageView;

    @Inject
    public BoardTemplateCtrl (MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDeleteImageView();
    }

    public void resetDeleteImageView() {
        File deleteFile = new File ("client/src/main/java/client/images/board/delete1.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        boardDeleteImageView.setImage(deleteImage);
    }

    public void boardDeleteImageViewOnMouseEntered () {
        File deleteFile = new File ("client/src/main/java/client/images/board/delete2.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        boardDeleteImageView.setImage(deleteImage);
    }

    public void boardDeleteImageViewOnMouseExited () {
        resetDeleteImageView();
    }

    public void viewBoardButtonOnMouseEntered () {
        viewBoardButton.setStyle("-fx-background-color: #b0bfd4; -fx-border-radius: 6");
    }

    public void viewBoardButtonOnMouseExited () {
        viewBoardButton.setStyle("-fx-background-color: #d1dae6; -fx-border-radius: 6");
    }

    /**
     * Display the CardList of this particular Board
     *
     * Currently empty, needs API endpoints in order to be implementable
     * Will wait for Issue #43 to be finished
     */
    public void viewBoardButtonOnAction() {

    }
}