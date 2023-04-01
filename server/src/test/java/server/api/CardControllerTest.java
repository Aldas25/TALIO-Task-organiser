package server.api;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TagRepository;


import java.util.ArrayList;
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
        TagRepository tagRepo = new TestTagRepository();
        TestBoardRepository boardRepo = new TestBoardRepository();
        TestCardListRepository cardListRepo = new TestCardListRepository();

        sut = new CardController(cardRepo, cardListRepo, null);
        listCtrl = new CardListController(cardListRepo, cardRepo, boardRepo, tagRepo, null);
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
        CardList l = new CardList("l1", new ArrayList<>());
        listCtrl.add(l);
        Card card = new Card("c1", new ArrayList<>());
        listCtrl.addCard(l.id,card);
        var actual = sut.getById(card.id);

        assertEquals("c1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getAllCards() {
        CardList l = new CardList("l1", new ArrayList<>());
        listCtrl.add(l);
        listCtrl.addCard(l.id,new Card("c1", new ArrayList<>()));
        var actual = cardRepo.findAll();

        assertEquals(actual, sut.getAll());
    }

    @Test
    public void databaseIsUsed() {
        CardList l = new CardList("l1", new ArrayList<>());
        listCtrl.add(l);
        listCtrl.addCard(l.id,new Card("c1", new ArrayList<>()));
        boolean actual = cardRepo.calledMethods.contains("save");
        assertTrue(actual);
    }

}
