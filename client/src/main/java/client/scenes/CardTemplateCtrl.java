package client.scenes;

import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

public class CardTemplateCtrl {

    private final MainCtrl mainCtrl;

    private Card card;
    private ListTemplateCtrl currentListCtrl;

    @FXML
    private AnchorPane cardAnchorPane;
    @FXML
    private Button editCardButton;

    @Inject
    public CardTemplateCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
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

    /**
     * Function that is called when the drag is detected
     * for this card (the drag starts on this card).
     * @param event mouse event information
     */
    public void onDragDetected(MouseEvent event) {
        // firstly change color of card (visual aid for the user)
        cardAnchorPane.setStyle("-fx-background-color: #F4FDB4");

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
            cardAnchorPane.setStyle("-fx-background-color: #AAB793");
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
    @FXML
    public void editCard(){
        mainCtrl.showUpdateCard(currentListCtrl.getList(), card);
    }
}
