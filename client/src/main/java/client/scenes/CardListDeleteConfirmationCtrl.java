package client.scenes;

import client.utils.ServerUtils;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class CardListDeleteConfirmationCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    private CardList cardListToBeDeleted;

    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @Inject
    public CardListDeleteConfirmationCtrl (MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setCardListToBeDeleted (CardList list) {
        this.cardListToBeDeleted = list;
    }

    public void cancelButtonOnAction () {
        mainCtrl.closeCardListDeleteConfirmation();
    }

    public void confirmButtonOnAction () {
        // delete the list itself
        server.send("/app/lists/delete", cardListToBeDeleted.id);
        this.cardListToBeDeleted = null;

        // refresh
        mainCtrl.showListOverview();

        // close the pop-up
        mainCtrl.closeCardListDeleteConfirmation();
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
