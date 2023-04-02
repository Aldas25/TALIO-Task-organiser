package client.scenes;

import client.services.CardService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
//import commons.CustomPair;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

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
        if(cardService.getCurrentCard()== null){
            if(titleTagTextField.getText().equals("")){
                addCard();
            }
            else{
                addCardTag();
            }
        }
        else{
            if(titleTagTextField.getText().equals("")){
                updateCard();
            }
            else{
                updateTagCard();
            }
        }
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

    /**
     * Adds a card with a tag in it. First, it creates a tag,
     * then it adds it to a list and creates a card with it
     */
    private void addCardTag() {
        Tag tag = new Tag(titleTagTextField.getText(), toHexString(colorPicker.getValue()));
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        Card card = new Card(cardTitleTextField.getText(), tagList);
        server.addCard(card, list);
    }

    /**
     * Updates a card title and a tag
     */
    private void updateTagCard() {
        return;
//        currentCard.title = cardTitleTextField.getText();
//        Tag tag = new Tag(titleTagTextField.getText(), toHexString(colorPicker.getValue()));
//        currentCard.tagList.add(tag);
////        server.updateCardTitle(currentCard);
//        server.send("/app/cards/update", new CustomPair<Long, Card>(currentCard.id, currentCard));
//        currentCard = null;
    }

    public void addCardButtonOnMouseEntered (MouseEvent event) {
        addCardButton.setStyle("-fx-background-color: #b0bfd4");
    }

    public void addCardButtonOnMouseExited (MouseEvent event) {
        addCardButton.setStyle("-fx-background-color: #d1dae6");
    }

    /**
     * This method translates a Color to Hexadecimal value
     * @param color the color chosen by the picker
     * @return the hexadecimal value of the color
     */
    private String toHexString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("#%08X", (r + g + b + a));
    }
}