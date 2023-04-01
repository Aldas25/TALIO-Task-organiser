package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class CardDeleteConfirmationCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    private Card cardToBeDeleted;

    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @Inject
    public CardDeleteConfirmationCtrl (MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setCardToBeDeleted (Card card) {
        this.cardToBeDeleted = card;
    }
    public void cancelButtonOnAction () {
        mainCtrl.closeCardDeleteConfirmation();
    }

    public void confirmButtonOnAction () {
        // delete the card itself
        server.send("/app/cards/delete", cardToBeDeleted.id);
        this.cardToBeDeleted = null;

        // close the pop-up
        mainCtrl.closeCardDeleteConfirmation();
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
