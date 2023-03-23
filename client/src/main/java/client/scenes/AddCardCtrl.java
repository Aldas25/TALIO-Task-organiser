package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddCardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private CardList list;
    private Card currentCard;

    @FXML
    private TextField cardTitleTextField;
    @FXML
    private Button addCardButton;

    @Inject
    public AddCardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setList(CardList list) {
        this.list = list;
    }
    public void setCard(Card card){
        this.currentCard = card;
    }
    /**
     * When adding a new card, the text field should be empty.
     * When updating an existing card, the text field should contain the old data.
     */
    public void refresh() {
        if(currentCard == null){
            cardTitleTextField.setText("");
        }
        else{
            cardTitleTextField.setText(currentCard.title);
        }
    }
    /**
     * If the current card is not yet initialized, adds a new card.
     *
     * Otherwise, updates the old card.
     * When everything's done, we set the current card of this controller to null.
     */
    public void addOrUpdateCard() {
        if(currentCard == null){
            addCard();
        }
        else{
            updateCard();
        }
        mainCtrl.showListOverview();
    }

    public void addCard() {
        Card card = new Card(cardTitleTextField.getText());
        server.addCard(card,list);
    }

    public void updateCard() {
        currentCard.title = cardTitleTextField.getText();
        server.updateCardTitle(currentCard);
        currentCard = null;
    }

    public void addCardButtonOnMouseEntered (MouseEvent event) {
        addCardButton.setStyle("-fx-background-color: #b0bfd4");
    }

    public void addCardButtonOnMouseExited (MouseEvent event) {
        addCardButton.setStyle("-fx-background-color: #d1dae6");
    }
}