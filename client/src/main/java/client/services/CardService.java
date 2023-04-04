package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class CardService {

    private final ServerUtils server;

    private Card currentCard;

    @Inject
    public CardService(ServerUtils server) {
        this.server = server;
    }

    public Card getCurrentCard(){
        return this.currentCard;
    }

    public void setCurrentCard(Card card){
        this.currentCard = card;
    }

    public void addCardToList(CardList list, String cardTitle) {
        Card card = new Card(cardTitle);
        server.addCard(card, list);
    }

    public void updateTitleCurrentCard(String newTitle) {
        currentCard.title = newTitle;
        server.updateCardTitle(currentCard);
    }
}
