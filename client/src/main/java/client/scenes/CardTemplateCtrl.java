package client.scenes;

import client.services.CardService;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class CardTemplateCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private Card card;
    private ListTemplateCtrl currentListCtrl;
    private Board board;

    private final CardService cardService;

    @FXML
    private AnchorPane cardAnchorPane;
    @FXML
    private TextField cardTitleTextField;
    @FXML
    private ImageView deleteImageView;
    @FXML
    private ImageView dotImageView;

    @Inject
    public CardTemplateCtrl(MainCtrl mainCtrl,
                            CardService cardService) {
        this.mainCtrl = mainCtrl;
        this.cardService = cardService;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDeleteImageView();
        resetDotsImageView();
    }

    public void start(Card card, ListTemplateCtrl currentListCtrl){
        setCard(card);
        setCurrentListCtrl(currentListCtrl);
    }

    public void resetDeleteImageView() {
        File deleteFile = new File ("client/src/main/java/client/images/card/delete2.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        deleteImageView.setImage(deleteImage);
    }

    public void resetDotsImageView() {
        File dotFile = new File ("client/src/main/java/client/images/card/dots1.png");
        Image dotImage = new Image (dotFile.toURI().toString());
        dotImageView.setImage(dotImage);
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

    public void setBoard (Board board) {
        this.board = board;
    }

    public void setTitle (String title) {
        cardTitleTextField.setText(title);
    }

    public void showPopUp () {
        mainCtrl.showCardDeleteConfirmation(card, board);
    }

    /**
     * Function that is called when the drag is detected
     * for this card (the drag starts on this card).
     * @param event mouse event information
     */
    public void onDragDetected(MouseEvent event) {
        // firstly change color of card (visual aid for the user)
        cardAnchorPane.setStyle("-fx-background-color: #BFC6D9; -fx-background-radius: 6");
        cardTitleTextField.setStyle("-fx-background-color: #BFC6D9; -fx-text-fill: #3c4867");

        // secondly, change the background of the icons to match the new card color
        File deleteFile = new File ("client/src/main/java/client/images/card/delete5.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        deleteImageView.setImage(deleteImage);

        File dotFile = new File ("client/src/main/java/client/images/card/dots3.png");
        Image dotImage = new Image (dotFile.toURI().toString());
        dotImageView.setImage(dotImage);

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
            cardAnchorPane.setStyle("-fx-background-color: #D1DAE6; -fx-background-radius: 6");
            cardTitleTextField.setStyle("-fx-background-color: #D1DAE6; -fx-text-fill: #3c4867");

            // reset the background color of the icons
            resetDeleteImageView();
            resetDotsImageView();
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

    public void updateCardTitle(KeyEvent event){
        if(!event.getCode().equals(KeyCode.ENTER))
            return;
        String newCardTitle = cardTitleTextField.getText();
        cardService.updateCardTitle(card, newCardTitle);
    }

    public void deleteImageViewOnMouseEntered (MouseEvent event) {
        File deleteFile = new File ("client/src/main/java/client/images/card/delete4.png");
        Image deleteImage = new Image (deleteFile.toURI().toString());
        deleteImageView.setImage(deleteImage);
    }

    public void deleteImageViewOnMouseExited (MouseEvent event) {
        resetDeleteImageView();
    }

    public void dotImageViewOnMouseEntered (MouseEvent event) {
        File dotFile = new File ("client/src/main/java/client/images/card/dots2.png");
        Image dotImage = new Image (dotFile.toURI().toString());
        dotImageView.setImage(dotImage);
    }

    public void dotImageViewOnMouseExited (MouseEvent event) {
        resetDotsImageView();
    }
}