package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class CardService {

    private final ServerUtils server;

    /**
     * Constructor of CardService
     * @param server Reference to ServerUtils
     */
    @Inject
    public CardService(ServerUtils server) {
        this.server = server;
    }

    /**
     * Adds Card to CardList
     * @param list The CardList
     * @param cardTitle The title of the card to add
     */
    public void addCardToList(CardList list, String cardTitle) {
        Card card = new Card(cardTitle);
        server.addCard(card, list);
    }

    /**
     * Updates the title of a Card
     * @param card The card to be updated
     * @param newTitle The new title
     */
    public void updateCardTitle(Card card, String newTitle) {
        card.title = newTitle;
        server.updateCardTitle(card);
    }
}
