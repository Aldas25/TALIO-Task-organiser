package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ListTemplateCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private CardList list;
    @FXML
    private TextField updateListNameField;
    @FXML
    private AnchorPane listAnchorPane;
    @FXML
    private Button addCardButton;

    @Inject
    public ListTemplateCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setList(CardList list) {
        this.list = list;
    }
    @FXML
    void updateCardListTitle(KeyEvent event) {
        list.setTitle(updateListNameField.getText());
        server.updateCardListTitle(list);
    }

    public void addCard() {
        mainCtrl.showAddCard(list);
    }

    public Button getAddCardButton() {
        return addCardButton;
    }

    /**
     * The drag and drop entered the list.
     * Used for dragging the cards.
     * When drag and drop is over the list, and the card is being dragged,
     * the list is visually being shown as selected for dropping.
     * @param event mouse event containing the information
     */
    public void onDragEntered(DragEvent event) {
        mainCtrl.setCurrentDraggedOverListCtrl(this);
        // if the card is in another list now, insert card at this list
        if (mainCtrl.getDraggableCardCtrl().getCurrentListCtrl() != this) {
            insertDraggedCardAtEnd();
        }

        // check if there is a card being dragged
        if (mainCtrl.getDraggableCardCtrl() != null) {
            // change color of anchor pane
            listAnchorPane.setStyle("-fx-background-color: #F4CDB4");
        }
        event.consume();
    }

    /**
     * The drag and drop entered the list.
     * Used for dragging the cards.
     * The drag moved away from this list, so the list should
     * visually be set back to its normal state.
     * @param event mouse event containing the information
     */
    public void onDragExited(DragEvent event) {
        listAnchorPane.setStyle("-fx-background-color: #C4CDB4");
        event.consume();
    }

    /**
     * The drag and drop event has ended.
     * Used for dragging the cards.
     * This function does the "drop" of the card
     * @param event mouse event containing the information
     */
    public void onDragDropped(DragEvent event) {
        CardTemplateCtrl cardCtrl = mainCtrl.getDraggableCardCtrl();
        boolean success = false;

        if (cardCtrl != null && cardCtrl.getCard() != null) {
            server.moveCardToList(cardCtrl.getCard(), list);
            success = true;
        }

        mainCtrl.setCurrentDraggedOverListCtrl(null);

        event.setDropCompleted(success);
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
        if (mainCtrl.getDraggableCardCtrl() != null) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    /**
     * Inserts card that is being dragged in this list at the end.
     * The insertion is done as a visual aid for the user.
     */
    public void insertDraggedCardAtEnd() {
        CardTemplateCtrl draggedCardCtrl = mainCtrl.getDraggableCardCtrl();
        ListTemplateCtrl previousListCtrl = draggedCardCtrl.getCurrentListCtrl();
        draggedCardCtrl.setCurrentListCtrl(this);

        // remove card from previous list
        AnchorPane cardAnchorPane = draggedCardCtrl.getCardAnchorPane();
        AnchorPane previousListAnchorPane = previousListCtrl.listAnchorPane;
        VBox previousListVBox = (VBox) previousListAnchorPane.getChildren().get(1);
        previousListVBox.getChildren().remove(cardAnchorPane);

        // insert card to this list
        VBox currentVBox = (VBox) listAnchorPane.getChildren().get(1);
        var childrenList = currentVBox.getChildren();
        int position = childrenList.size()-1; // insert before the end
                                              // the last item is "add card" button
        childrenList.add(position, cardAnchorPane);
    }

    /**
     * Inserts card that is being dragged in this list before or after
     * some other card
     * The insertion is done as a visual aid for the user.
     * @param otherCardCtrl the controller that belongs to the card, before/after
     *                      which we are inserting the dragged card
     */
    public void insertDraggedCardBeforeOrAfter(CardTemplateCtrl otherCardCtrl) {
        CardTemplateCtrl draggedCardCtrl = mainCtrl.getDraggableCardCtrl();
        ListTemplateCtrl previousListCtrl = draggedCardCtrl.getCurrentListCtrl();
        draggedCardCtrl.setCurrentListCtrl(this);


        AnchorPane cardAnchorPane = draggedCardCtrl.getCardAnchorPane();
        AnchorPane previousListAnchorPane = previousListCtrl.listAnchorPane;
        VBox previousListVBox = (VBox) previousListAnchorPane.getChildren().get(1);
        VBox currentVBox = (VBox) listAnchorPane.getChildren().get(1);
        var childrenList = currentVBox.getChildren();

        // check if the dragged card is exactly before the 'otherCard'
        boolean wasBefore = false;
        AnchorPane lastAnchorPane = null;
        for (javafx.scene.Node node : childrenList) {
            if (node == otherCardCtrl.getCardAnchorPane()) {
                // if last pane was the card we drag, insert after this
                // else before this
                if (lastAnchorPane == cardAnchorPane)
                    wasBefore = true;
                break;
            }
            lastAnchorPane = (AnchorPane) node;
        }

        // remove card from previous list
        previousListVBox.getChildren().remove(cardAnchorPane);

        // get position for inserting
        int position = 0;
        for (int i = 0; i < childrenList.size(); i++) {
            if (childrenList.get(i) == otherCardCtrl.getCardAnchorPane()) {
                // if last pane was the card we drag, insert after this
                // else before this
                if (wasBefore)
                    position = i+1;
                else
                    position = i;
                break;
            }
        }

        // insert card to this list
        // the last item is "add card" button
        childrenList.add(position, cardAnchorPane);
    }

}
