package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class CardService {

    private final ServerUtils server;

    @Inject
    public CardService(ServerUtils server) {
        this.server = server;
    }

    public void addCardToList(CardList list, String cardTitle) {
        Card card = new Card(cardTitle);
        server.addCard(card, list);
    }

    public void updateCardTitle(Card card, String newTitle) {
        card.title = newTitle;
        server.updateCardTitle(card);
    }
}
