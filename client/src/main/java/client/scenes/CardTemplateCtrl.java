package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class CardTemplateCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private Card card;
    private ListTemplateCtrl currentListCtrl;

    @FXML
    private AnchorPane cardAnchorPane;
    @FXML
    private Button editCardButton;
    @FXML
    private ImageView deleteImageView;

    @Inject
    public CardTemplateCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDeleteImageView();
    }

    public void resetDeleteImageView() {
        File deleteFile = new File ("client/src/main/java/client/images/card/delete2.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        deleteImageView.setImage(deleteImage);
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCurrentListCtrl(ListTemplateCtrl listCtrl) {
        this.currentListCtrl = listCtrl;
    }

    public ListTemplateCtrl getCurrentListCtrl() {
        return currentListCtrl;
    }

    public AnchorPane getCardAnchorPane() {
        return cardAnchorPane;
    }

    public void showPopUp () {
        mainCtrl.showCardDeleteConfirmation(card);
    }

    /**
     * Function that is called when the drag is detected
     * for this card (the drag starts on this card).
     * @param event mouse event information
     */
    public void onDragDetected(MouseEvent event) {
        // firstly change color of card (visual aid for the user)
        cardAnchorPane.setStyle("-fx-background-color: #BFC6D9");

        // secondly, change the background of the delete icon to match the new card color
        File deleteFile = new File ("client/src/main/java/client/images/card/delete5.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        deleteImageView.setImage(deleteImage);

        Dragboard db = cardAnchorPane.startDragAndDrop(TransferMode.ANY);
        mainCtrl.setDraggableCardCtrl(this);
        mainCtrl.setCurrentDraggedOverListCtrl(currentListCtrl);

        // ClipboardContent is only used so that DragBoard and DragEvent
        // work properly
        ClipboardContent content = new ClipboardContent();
        content.putString("Card drag");
        db.setContent(content);

        event.consume();
    }

    /**
     * Function that is called when the drag is done,
     * and the card was dropped.
     * @param event mouse event information
     */
    public void onDragDone(DragEvent event) {
        mainCtrl.setDraggableCardCtrl(null);

        // check if it was successful drag
        if (event.getTransferMode() == TransferMode.MOVE) {
            // refresh if the drag was done
            mainCtrl.showListOverview();
        } else {
            // reset card color
            cardAnchorPane.setStyle("-fx-background-color: #D1DAE6");

            // reset the background color of the delete icon
            resetDeleteImageView();
        }

        event.consume();
    }

    /**
     * The drag and drop entered the list.
     * Used for dragging the cards.
     * When drag and drop is over the list, and the card is being dragged,
     * the list is visually being shown as selected for dropping.
     * @param event mouse event containing the information
     */
    public void onDragEntered(DragEvent event) {
        CardTemplateCtrl ctrl = mainCtrl.getDraggableCardCtrl();

        // check if there is a card being dragged,
        // and it is not this card
        if (ctrl != null && ctrl != this) {
            // move dragged card
            mainCtrl.getCurrentDraggedOverListCtrl().insertDraggedCardBeforeOrAfter(this);
        }
        event.consume();
    }

    /**
     * The drag and drop event has come over this list.
     * Used for dragging the cards.
     * @param event mouse event containing the information
     */
    public void onDragOver(DragEvent event) {
        // check whether to accept transfer modes
        // check whether a card is dragged
        CardTemplateCtrl ctrl = mainCtrl.getDraggableCardCtrl();
        if (ctrl != null && ctrl != this) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }
    public void editCard(){
        mainCtrl.showUpdateCard(currentListCtrl.getList(), card);
    }

    public void editCardButtonOnMouseEntered (MouseEvent event) {
        editCardButton.setStyle("-fx-background-color: #b0bfd4; -fx-border-color: #3c4867");
    }

    public void editCardButtonOnMouseExited (MouseEvent event) {
        editCardButton.setStyle("-fx-background-color: #7D88A6; -fx-border-color: #3c4867");
    }

    public void deleteImageViewOnMouseEntered (MouseEvent event) {
        File deleteFile = new File ("client/src/main/java/client/images/card/delete4.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        deleteImageView.setImage(deleteImage);
    }

    public void deleteImageViewOnMouseExited (MouseEvent event) {
        resetDeleteImageView();
    }
}