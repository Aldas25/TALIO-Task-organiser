package client.scenes;

import client.Main;
import client.services.BoardService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminBoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final BoardService boardService;

    @FXML
    private ImageView disconnectImageView;

    @FXML
    private VBox boardContainer;

    @Inject
    public AdminBoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                                  BoardService boardService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDisconnectImageView();
    }

    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
        server.registerForUpdates(board ->{
            Platform.runLater(() -> refresh());
        });
    }

    public void resetDisconnectImageView () {
        File disconnectFile = new
                File ("client/src/main/resources/client" +
                "/images/admin-board-overview/disconnect1.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void disconnectFromServer(){
        mainCtrl.disconnectFromServer();
    }

    public void disconnectOnMouseEntered (MouseEvent event) {
        File disconnectFile = new
                File ("client/src/main/resources/client" +
                "/images/admin-board-overview/disconnect2.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void disconnectOnMouseExited (MouseEvent event) {
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
