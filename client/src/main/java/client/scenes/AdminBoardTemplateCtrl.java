package client.scenes;

import client.services.BoardService;
import client.utils.ImageUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminBoardTemplateCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final BoardService boardService;
    private final ImageUtils imageUtils;

    private Board board;


    @FXML
    private TextField updateBoardNameField;
    @FXML
    private Button viewBoardButton;
    @FXML
    private ImageView boardDeleteImageView;

    @Inject
    public AdminBoardTemplateCtrl (MainCtrl mainCtrl,
                              BoardService boardService, ImageUtils imageUtils) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
        this.imageUtils = imageUtils;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDeleteImageView();
    }

    public void setBoard (Board board) {
        this.board = board;
    }

    public void resetDeleteImageView() {
        imageUtils.loadImage(boardDeleteImageView, "board/delete1.png");
    }

    public void boardDeleteImageViewOnMouseEntered () {
        imageUtils.loadImage(boardDeleteImageView, "board/delete2.png");
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
        String newTitle = updateBoardNameField.getText();
        boardService.updateBoardTitle(board, newTitle);
    }

}