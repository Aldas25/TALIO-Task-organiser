package client.scenes;

import client.services.BoardService;
import client.utils.ServerUtils;
import commons.Board;
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

public class AdminBoardTemplateCtrl implements Initializable {

    private MainCtrl mainCtrl;
    private ServerUtils server;
    private BoardService boardService;

    private Board board;


    @FXML
    private TextField updateBoardNameField;
    @FXML
    private Button viewBoardButton;
    @FXML
    private ImageView boardDeleteImageView;

    @Inject
    public AdminBoardTemplateCtrl (MainCtrl mainCtrl, ServerUtils server,
                              BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardService = boardService;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDeleteImageView();
    }

    public void setBoard (Board board) {
        this.board = board;
    }

    public void resetDeleteImageView() {
        File deleteFile = new File ("client/src/main/resources/client/images/board/delete1.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        boardDeleteImageView.setImage(deleteImage);
    }

    public void boardDeleteImageViewOnMouseEntered () {
        File deleteFile = new File ("client/src/main/resources/client/images/board/delete2.png");
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
     */
    public void viewAdminBoardButtonOnAction() {
        boardService.setCurrentBoard(board);
        mainCtrl.showAdminListOverview();
    }

    public void showAdminBoardPopUp(){
        mainCtrl.showAdminBoardDeleteConfirmation(board);
    }

    public void updateBoardTitle() {
        board.title = updateBoardNameField.getText();
        server.updateBoardTitle(board);
    }

}