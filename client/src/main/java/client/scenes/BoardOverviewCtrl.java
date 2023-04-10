package client.scenes;

import client.Main;
import client.services.JoinedBoardsService;
import client.services.BoardService;
import client.utils.ImageUtils;
import client.utils.ServerUtils;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final BoardService boardService;
    private final JoinedBoardsService joinedBoardsService;
    private final ImageUtils imageUtils;

    @FXML
    private ImageView addImageView;
    @FXML
    private ImageView joinImageView;
    @FXML
    private ImageView disconnectImageView;
    @FXML
    private VBox boardContainer;

    @Inject
    public BoardOverviewCtrl (MainCtrl mainCtrl, ServerUtils server,
                              BoardService boardService,
                              JoinedBoardsService joinedBoardsService,
                              ImageUtils imageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardService = boardService;
        this.joinedBoardsService = joinedBoardsService;
        this.imageUtils = imageUtils;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetAddImageView();
        resetJoinImageView();
        resetDisconnectImageView();
    }

    public void resetAddImageView () {
        imageUtils.loadImage(addImageView, "board-overview/add1.png");
    }

    public void resetJoinImageView () {
        imageUtils.loadImage(joinImageView, "board-overview/join1.png");
    }

    public void resetDisconnectImageView () {
        imageUtils.loadImage(disconnectImageView, "board-overview/disconnect1.png");
    }

    public void start() {
        server.registerForMessages("/topic/boards/update", HttpStatus.class, httpStatus -> {
            if (httpStatus.equals(HttpStatus.OK)){
                Platform.runLater(this::refresh);
            }
        });
    }

    public void refresh() {
        boardContainer.getChildren().clear();

        List<Board> allBoards = joinedBoardsService.getJoinedBoards();
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
        boardService.addBoard("New Board");
        refresh();
    }

    /**
     * Allows the user to join boards based on ID.
     * Currently non-functional. When the user clicks on the image,
     * a text field is supposed to appear where the user may type in
     * the ID of the desired Board and then show the Board in the List.
     */
    public void joinOnMouseClicked () {
        mainCtrl.showJoinBoard();
    }

    public void disconnectOnMouseClicked () {
        mainCtrl.disconnectFromServer();
    }

    public void addOnMouseEntered () {
        imageUtils.loadImage(addImageView, "board-overview/add2.png");
    }

    public void addOnMouseExited () {
        resetAddImageView();
    }

    public void joinOnMouseEntered () {
        imageUtils.loadImage(joinImageView, "board-overview/join2.png");
    }

    public void joinOnMouseExited () {
        resetJoinImageView();
    }

    public void disconnectOnMouseEntered () {
        imageUtils.loadImage(disconnectImageView, "board-overview/disconnect2.png");
    }

    public void disconnectOnMouseExited () {
        resetDisconnectImageView();
    }
}
