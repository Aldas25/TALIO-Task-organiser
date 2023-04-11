package client.scenes;

import client.services.BoardService;
import client.services.JoinedBoardsService;
import client.utils.ImageUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardTemplateCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final BoardService boardService;
    private final JoinedBoardsService joinedBoardsService;
    private final ImageUtils imageUtils;

    private Board board;

    @FXML
    private TextField updateBoardNameField;
    @FXML
    private Button viewBoardButton;
    @FXML
    private Button leaveBoardButton;
    @FXML
    private ImageView boardDeleteImageView;
    @FXML
    private Label warningLabel;

    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param boardService Reference to BoardService
     * @param imageUtils Reference to ImageUtils
     * @param joinedBoardsService Reference to JoinedBoardsService
     */
    @Inject
    public BoardTemplateCtrl (MainCtrl mainCtrl,
                              BoardService boardService,
                              JoinedBoardsService joinedBoardsService,
                              ImageUtils imageUtils) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
        this.joinedBoardsService = joinedBoardsService;
        this.imageUtils = imageUtils;
    }

    /**
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
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

    /**
     * Function called by event in JavaFX
     */
    public void boardDeleteImageViewOnMouseEntered () {
        imageUtils.loadImage(boardDeleteImageView, "board/delete2.png");
    }

    /**
     * Function called by event in JavaFX
     */
    public void boardDeleteImageViewOnMouseExited () {
        resetDeleteImageView();
    }

    /**
     * Function called by event in JavaFX
     */
    public void viewBoardButtonOnMouseEntered () {
        viewBoardButton.setStyle("-fx-background-color: #b0bfd4; -fx-border-radius: 6");
    }

    /**
     * Function called by event in JavaFX
     */
    public void viewBoardButtonOnMouseExited () {
        viewBoardButton.setStyle("-fx-background-color: #d1dae6; -fx-border-radius: 6");
    }

    /**
     * Display the CardList of this particular Board
     */
    public void viewBoardButtonOnAction() {
        boardService.setCurrentBoard(board);
        mainCtrl.showListOverview();
    }

    /**
     * Function called by event in JavaFX
     */
    public void leaveBoardButtonOnMouseEntered () {
        leaveBoardButton.setStyle("-fx-background-color: #b0bfd4; -fx-border-radius: 6");
    }

    /**
     * Function called by event in JavaFX
     */
    public void leaveBoardButtonOnMouseExited () {
        leaveBoardButton.setStyle("-fx-background-color: #d1dae6; -fx-border-radius: 6");
    }

    /**
     * Function called by event in JavaFX
     */
    public void leaveBoardButtonOnAction() {
        joinedBoardsService.leaveBoardAndSave(board);
        mainCtrl.showBoardOverview();
    }

    /**
     * Function called by button in JavaFX
     */
    public void showBoardPopUp(){
        mainCtrl.showBoardDeleteConfirmation(board);
    }

    /**
     * Changes name for board
     * @param event Records key presses
     */
    public void updateBoardTitle(KeyEvent event) {
        warningLabel.setText("Press ENTER to confirm.");
        if (event.getCode().equals(KeyCode.ENTER)) {
            warningLabel.setText(null);
            String newTitle = updateBoardNameField.getText();
            boardService.updateBoardTitle(board, newTitle);
        }
    }
}
