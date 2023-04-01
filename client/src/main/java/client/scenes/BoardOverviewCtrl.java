package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private ImageView addImageView;
    @FXML
    private ImageView joinImageView;
    @FXML
    private ImageView disconnectImageView;
    @FXML
    private VBox boardContainer;

    @Inject
    public BoardOverviewCtrl (MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetAddImageView();
        resetJoinImageView();
        resetDisconnectImageView();
    }

    public void resetAddImageView () {
        File addFile = new File ("client/src/main/java/client/images/board-overview/add1.png");
        Image addImage = new Image (addFile.toURI().toString());
        addImageView.setImage(addImage);
    }

    public void resetJoinImageView () {
        File joinFile = new File ("client/src/main/java/client/images/board-overview/join1.png");
        Image joinImage = new Image (joinFile.toURI().toString());
        joinImageView.setImage(joinImage);
    }

    public void resetDisconnectImageView () {
        File disconnectFile = new
                File ("client/src/main/java/client/images/board-overview/disconnect1.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void start() {
        server.registerForMessages("/topic/boards/add", Board.class, board -> {
            Platform.runLater(() -> refresh());
        });
    }

    public void refresh() {
        boardContainer.getChildren().clear();

        List<Board> allBoards = server.getBoards();
        for(Board board: allBoards) {
            var boardTemplate = Main.load (
                    BoardTemplateCtrl.class, "client", "scenes", "BoardTemplate.fxml"
            );

            BoardTemplateCtrl boardTemplateCtrl = boardTemplate.getKey();
            AnchorPane boardNode = (AnchorPane) boardTemplate.getValue();
            boardTemplateCtrl.setBoard(board);

            // retrieving text from a copy of the file BoardTemplate
            TextField textField = (TextField) boardNode.getChildren().get(0);
            textField.setText(board.title);                  // setting title to new node

            // add board to the board container
            boardContainer.getChildren().add(boardNode);
        }
    }

    public void addOnMouseClicked () {
        Board newBoard = new Board("New Board", new ArrayList<>());
        server.send("/app/boards/add", newBoard);
    }

    /**
     * Allows the user to join boards based on ID.
     *
     * Currently non-functional. When the user clicks on the image,
     * a text field is supposed to appear where the user may type in
     * the ID of the desired Board and then show the Board in the List.
     */
    public void joinOnMouseClicked () {

    }

    public void disconnectOnMouseClicked () {
        mainCtrl.disconnectFromServer();
    }

    public void addOnMouseEntered () {
        File addFile = new File ("client/src/main/java/client/images/board-overview/add2.png");
        Image addImage = new Image (addFile.toURI().toString());
        addImageView.setImage(addImage);
    }

    public void addOnMouseExited () {
        resetAddImageView();
    }

    public void joinOnMouseEntered () {
        File joinFile = new File ("client/src/main/java/client/images/board-overview/join2.png");
        Image joinImage = new Image (joinFile.toURI().toString());
        joinImageView.setImage(joinImage);
    }

    public void joinOnMouseExited () {
        resetJoinImageView();
    }

    public void disconnectOnMouseEntered () {
        File disconnectFile = new
                File ("client/src/main/java/client/images/board-overview/disconnect2.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void disconnectOnMouseExited () {
        resetDisconnectImageView();
    }
}
