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

    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param boardService Reference to BoardService
     * @param imageUtils Reference to ImageUtils
     */
    @Inject
    public AdminBoardTemplateCtrl (MainCtrl mainCtrl,
                              BoardService boardService, ImageUtils imageUtils) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
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

    /**
     * Sets a board
     * @param board The board to set
     */
    public void setBoard (Board board) {
        this.board = board;
    }

    /**
     * Resets the delete image view
     */
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
    public void viewAdminBoardButtonOnAction() {
        boardService.setCurrentBoard(board);
        mainCtrl.showAdminListOverview();
    }

    /**
     * Shows board pop up
     */
    public void showAdminBoardPopUp(){
        mainCtrl.showAdminBoardDeleteConfirmation(board);
    }

    /**
     * Updates title of board
     */
    public void updateBoardTitle() {
        String newTitle = updateBoardNameField.getText();
        boardService.updateBoardTitle(board, newTitle);
    }

}