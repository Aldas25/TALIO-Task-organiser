package server.api;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardControllerTest {

    private TestCardRepository cardRepo;
    private CardController sut;
    private CardListController listCtrl;

    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        TestBoardRepository boardRepo = new TestBoardRepository();
        TestCardListRepository cardListRepo = new TestCardListRepository();
        sut = new CardController(cardRepo, cardListRepo);

        listCtrl = new CardListController(cardListRepo, cardRepo, boardRepo, null);
    }

    @Test
    public void cannotGetByInvalidId() {
        var actual = sut.getById(-1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotGetByNonExistentId() {
        var actual = sut.getById(1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getById() {
        CardList l = new CardList("l1");
        listCtrl.add(l);
        Card card = new Card("c1");
        listCtrl.addCard(l.id,card);
        var actual = sut.getById(card.id);

        assertEquals("c1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getAllCards() {
        CardList l = new CardList("l1");
        listCtrl.add(l);
        listCtrl.addCard(l.id,new Card("c1"));
        var actual = cardRepo.findAll();

        assertEquals(actual, sut.getAll());
    }

    @Test
    public void databaseIsUsed() {
        CardList l = new CardList("l1");
        listCtrl.add(l);
        listCtrl.addCard(l.id,new Card("c1"));
        boolean actual = cardRepo.calledMethods.contains("save");
        assertTrue(actual);
    }

}
