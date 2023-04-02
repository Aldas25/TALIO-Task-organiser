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
    public void testGetCurrentCardNull(){
        assertNull(sut.getCurrentCard());
    }

    @Test
    public void testSetCurrentCard(){
        Card c = new Card("title", new ArrayList<>());
        sut.setCurrentCard(c);
        assertEquals(c,sut.getCurrentCard());
    }

    @Test
    public void testAddCardToList(){
        Card c = new Card("title", new ArrayList<>());
        CardList list = new CardList("list", new ArrayList<>());
        list.id = 0;

        sut.addCardToList(list,"title");


        List<Card> expected = List.of(c);
        assertEquals(expected, server.getCardsForList(list));
        assertTrue(server.log.contains("addCard 0 0 title"));
        assertTrue(server.log.contains("getCardsForList 0"));
    }

    @Test
    public void testUpdateTitleCurrentCard(){
        Card c = new Card("title", new ArrayList<>());
        sut.setCurrentCard(c);
        sut.updateTitleCurrentCard("newTitle");
        assertEquals("newTitle",sut.getCurrentCard().title);
        assertTrue(server.log.contains("updateCardTitle 0 newTitle"));
    }


}