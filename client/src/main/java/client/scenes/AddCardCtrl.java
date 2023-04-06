package client.scenes;

import client.services.CardService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
//import commons.CustomPair;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddCardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    private CardList list;
    private CardService cardService;

    @FXML
    private TextField cardTitleTextField;
    @FXML
    private Button addCardButton;

    @FXML
    private TextField titleTagTextField;

    @FXML
    private ColorPicker colorPicker;



    @Inject
    public AddCardCtrl(MainCtrl mainCtrl, ServerUtils server, CardService cardService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.cardService = cardService;
    }

    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
    }

    public void setList(CardList list) {
        this.list = list;
    }
    public void setCard(Card card){
        cardService.setCurrentCard(card);
    }
    /**
     * When adding a new card, the text field should be empty.
     * When updating an existing card, the text field should contain the old data.
     */
    public void refresh() {
        if(cardService.getCurrentCard() == null){
            cardTitleTextField.setText("");
        }
        else{
            cardTitleTextField.setText(cardService.getCurrentCard().title);
        }
    }
    /**
     * If the current card is not yet initialized, adds a new card.
     *
     * Otherwise, updates the old card.
     * When everything's done, we set the current card of this controller to null.
     */
    public void addOrUpdateCard() {
        if(cardService.getCurrentCard() == null){
            addCard();
//            mainCtrl.showListOverview();
            return;
        }

        updateCard();
        mainCtrl.showListOverview();
    }

    /**
     * Creates a new card and adds it to the server
     */
    public void addCard() {
        cardService.addCardToList(list,cardTitleTextField.getText());
    }

    /**
     * Updates the title of a card after an edit
     */
    public void updateCard() {
        cardService.updateTitleCurrentCard(cardTitleTextField.getText());
        cardService.setCurrentCard(null);
    }

    public void addCardButtonOnMouseEntered (MouseEvent event) {
        addCardButton.setStyle("-fx-background-color: #b0bfd4");
    }

    public void addCardButtonOnMouseExited (MouseEvent event) {
        addCardButton.setStyle("-fx-background-color: #d1dae6");
    }
}