package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class BoardDeleteConfirmationCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    private Board boardToBeDeleted;

    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @Inject
    public BoardDeleteConfirmationCtrl (MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setBoardToBeDeleted (Board board) {
        this.boardToBeDeleted = board;
    }
    public void cancelButtonOnAction () {
        mainCtrl.closeBoardDeleteConfirmation();
    }

    public void confirmButtonOnAction () {
        // delete the board itself
        // add the code here


        this.boardToBeDeleted = null;

        // refresh
        mainCtrl.showListOverview();

        // close the pop-up
        mainCtrl.closeBoardDeleteConfirmation();
    }

    public void cancelButtonOnMouseEntered (MouseEvent event) {
        cancelButton.setStyle("-fx-background-color: #B05656");
    }

    public void cancelButtonOnMouseExited (MouseEvent event) {
        cancelButton.setStyle("-fx-background-color: #DD6C6C");
    }

    public void confirmButtonOnMouseEntered (MouseEvent event) {
        confirmButton.setStyle("-fx-background-color: #90A07B");
    }

    public void confirmButtonOnMouseExited (MouseEvent event) {
        confirmButton.setStyle("-fx-background-color: #B5C99A");
    }
}
