package client.scenes;

import client.Main;
import client.utils.ImageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminBoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ImageUtils imageUtils;

    @FXML
    private ImageView disconnectImageView;

    @FXML
    private VBox boardContainer;

    @Inject
    public AdminBoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                                  ImageUtils imageUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.imageUtils = imageUtils;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDisconnectImageView();
    }

    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
        server.registerForUpdates(board -> Platform.runLater(this::refresh));
    }

    public void resetDisconnectImageView () {
        imageUtils.loadImage(disconnectImageView, "admin-board-overview/disconnect1.png");
    }

    public void disconnectFromServer(){
        mainCtrl.disconnectFromServer();
    }

    public void disconnectOnMouseEntered () {
        imageUtils.loadImage(disconnectImageView, "admin-board-overview/disconnect2.png");
    }

    public void disconnectOnMouseExited () {
        resetDisconnectImageView();
    }

    public void refresh() {
        boardContainer.getChildren().clear();

        List<Board> allBoards = server.getBoards();
        for(Board board: allBoards) {
            var adminBoardTemplate = Main.load (
                    AdminBoardTemplateCtrl.class, "client", "scenes",
                    "AdminBoardTemplate.fxml"
            );

            AdminBoardTemplateCtrl adminBoardTemplateCtrl = adminBoardTemplate.getKey();
            AnchorPane boardNode = (AnchorPane) adminBoardTemplate.getValue();
            adminBoardTemplateCtrl.setBoard(board);

            // retrieving text from a copy of the file BoardTemplate
            TextField textField = (TextField) boardNode.getChildren().get(0);
            textField.setText(board.title);                  // setting title to new node

            // add board to the board container
            boardContainer.getChildren().add(boardNode);
        }
    }

    public void stop(){
        server.stop();
    }

}
