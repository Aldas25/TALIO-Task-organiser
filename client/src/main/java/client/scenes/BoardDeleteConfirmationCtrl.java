package client.scenes;

import client.services.DeleteService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class BoardDeleteConfirmationCtrl {

    private final MainCtrl mainCtrl;
    private final DeleteService deleteService;

    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @Inject
    public BoardDeleteConfirmationCtrl (MainCtrl mainCtrl, DeleteService deleteService) {
        this.mainCtrl = mainCtrl;
        this.deleteService = deleteService;
    }

    public void cancelButtonOnAction () {
        mainCtrl.closeBoardDeleteConfirmation();
    }

    public void confirmButtonOnAction () {
        // delete the board itself
        deleteService.deleteSelectedObject();

        // refresh
        mainCtrl.showBoardOverview();

        // close the pop-up
        mainCtrl.closeBoardDeleteConfirmation();
    }

    public void cancelButtonOnMouseEntered () {
        cancelButton.setStyle("-fx-background-color: #B05656");
    }

    public void cancelButtonOnMouseExited () {
        cancelButton.setStyle("-fx-background-color: #DD6C6C");
    }

    public void confirmButtonOnMouseEntered () {
        confirmButton.setStyle("-fx-background-color: #90A07B");
    }

    public void confirmButtonOnMouseExited () {
        confirmButton.setStyle("-fx-background-color: #B5C99A");
    }
}
