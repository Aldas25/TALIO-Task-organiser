package client.scenes;

import client.services.DeleteService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class CardDeleteConfirmationCtrl {

    private final MainCtrl mainCtrl;
    private final DeleteService deleteService;

    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param deleteService Reference to DeleteService
     */
    @Inject
    public CardDeleteConfirmationCtrl (MainCtrl mainCtrl, DeleteService deleteService) {
        this.mainCtrl = mainCtrl;
        this.deleteService = deleteService;
    }

    /**
     * Function called by button in JavaFX
     */
    public void cancelButtonOnAction () {
        mainCtrl.closeCardDeleteConfirmation();
    }

    /**
     * Function called by button in JavaFX
     */
    public void confirmButtonOnAction () {
        // delete the card itself
        deleteService.deleteSelectedObject();

        // close the pop-up
        mainCtrl.closeCardDeleteConfirmation();
    }

    /**
     * Function called by event in JavaFX
     */
    public void cancelButtonOnMouseEntered () {
        cancelButton.setStyle("-fx-background-color: #B05656");
    }

    /**
     * Function called by event in JavaFX
     */
    public void cancelButtonOnMouseExited () {
        cancelButton.setStyle("-fx-background-color: #DD6C6C");
    }

    /**
     * Function called by event in JavaFX
     */
    public void confirmButtonOnMouseEntered () {
        confirmButton.setStyle("-fx-background-color: #90A07B");
    }

    /**
     * Function called by event in JavaFX
     */
    public void confirmButtonOnMouseExited () {
        confirmButton.setStyle("-fx-background-color: #B5C99A");
    }
}
