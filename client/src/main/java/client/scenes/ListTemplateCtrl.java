package client.scenes;

import client.services.CardService;
import client.services.ListService;
import client.utils.ImageUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ListTemplateCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ListService listService;
    private final CardService cardService;
    private final ImageUtils imageUtils;

    private CardList list;
    @FXML
    private TextField updateListNameField;
    @FXML
    private AnchorPane listAnchorPane;
    @FXML
    private Button addCardButton;
    @FXML
    private ImageView deleteImageView;
    @FXML
    private Label warningLabel;

    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param listService Reference to ListService
     * @param imageUtils Reference to ImageUtils
     * @param cardService Reference to CardService
     */
    @Inject
    public ListTemplateCtrl(MainCtrl mainCtrl,  ListService listService,
                            CardService cardService, ImageUtils imageUtils) {
        this.mainCtrl = mainCtrl;
        this.listService = listService;
        this.cardService = cardService;
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

    public void resetDeleteImageView() {
        imageUtils.loadImage(deleteImageView, "list/delete1.png");
    }

    public void start(CardList list){
        setList(list);
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public CardList getList(){
        return list;
    }

    public void updateCardListTitle(KeyEvent event) {
        warningLabel.setText("Press ENTER to confirm.");
        if(event.getCode().equals(KeyCode.ENTER)){
            warningLabel.setText(null);
            String newTitle = updateListNameField.getText();
            listService.updateListTitle(list, newTitle);
        }
    }

    /**
     * Function called by button in JavaFX
     */
    public void addCard() {
        cardService.addCardToList(list, "New Card");
    }

    /**
     * Function called by button in JavaFX
     */
    public void showListPopUp() {
        mainCtrl.showCardListDeleteConfirmation(list);
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
        // if the cards is in another list now, insert card at this list
        if (mainCtrl.getDraggableCardCtrl().getCurrentListCtrl() != this) {
            insertDraggedCardAtEnd();
        }

        // check if there is a card being dragged
        if (mainCtrl.getDraggableCardCtrl() != null) {
            // change color of anchor pane
            listAnchorPane.setStyle("-fx-background-color: #3c4867");
            updateListNameField.setStyle("-fx-background-color: #3c4867; -fx-text-fill: #E6E8F0");

            // change background color of the 'delete image'
            imageUtils.loadImage(deleteImageView, "list/delete2.png");
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
        listAnchorPane.setStyle("-fx-background-color: #6D85A8");
        updateListNameField.setStyle("-fx-background-color:  #6D85A8; -fx-text-fill: #151928");
        resetDeleteImageView();

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
        boolean success = cardCtrl != null && cardCtrl.getCard() != null;
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
        //server.moveCardToList(draggedCardCtrl.getCard(),list,position);
        mainCtrl.setDragNewPosition(position);
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
        //server.moveCardToList(draggedCardCtrl.getCard(),list,position);
        mainCtrl.setDragNewPosition(position);
    }

    /**
     * Function called by event in JavaFX
     */
    public void addCardButtonOnMouseEntered () {
        addCardButton.setStyle("-fx-background-color: #b0bfd4");
    }

    /**
     * Function called by event in JavaFX
     */
    public void addCardButtonOnMouseExited () {
        addCardButton.setStyle("-fx-background-color: #d1dae6");
    }

    /**
     * Function called by event in JavaFX
     */
    public void deleteImageViewOnMouseEntered () {
        imageUtils.loadImage(deleteImageView, "list/delete3.png");
    }

    /**
     * Function called by event in JavaFX
     */
    public void deleteImageViewOnMouseExited () {
        resetDeleteImageView();
    }

}
