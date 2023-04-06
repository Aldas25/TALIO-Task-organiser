package client.services;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {

    private CardService sut;

    private TestServerUtils server;

    @BeforeEach
    public void setup() {
        server = new TestServerUtils();
        sut = new CardService(server);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    public void testAddCardToList(){
        Card c = new Card("title");
        CardList list = new CardList("list", new ArrayList<>());
        list.id = 0;

        sut.addCardToList(list,"title");

        List<Card> expected = List.of(c);
        // check that there is a new card in the server
        assertEquals(expected, server.getCardsForList(list));

        // check for server log (indirect behaviour)
        assertTrue(server.log.contains("addCard 0 0 title"));
        assertTrue(server.log.contains("getCardsForList 0"));
    }

    @Test
    public void testUpdateCardTitle() {
        Card card = new Card("Title");
        card.id = 5;
        sut.updateCardTitle(card, "New title");

        assertEquals("New title", card.title);

        // also check for called server methods (indirect behaviour)
        assertTrue(server.log.contains("updateCardTitle 5 New title"));
    }

}