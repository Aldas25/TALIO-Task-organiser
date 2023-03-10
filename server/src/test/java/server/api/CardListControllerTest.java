package server.api;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardListControllerTest {
    private TestCardRepository cardRepo;
    private TestCardListRepository cardListRepo;
    private CardController cardCtrl;
    private CardListController sut;

    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        cardListRepo = new TestCardListRepository();
        cardCtrl = new CardController(cardRepo, cardListRepo);
        sut = new CardListController(cardListRepo, cardRepo);
    }
    @Test
    public void cannotAddNullCardList() {
        var actual = sut.add(new CardList(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
    @Test
    public void cannotAddEmptyCardList() {
        var actual = sut.add(new CardList(""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addOneCardList() {
        sut.add(new CardList("l1"));

        var lists = cardListRepo.findAll();
        var actual = ResponseEntity.ok(lists.get(0));

        assertEquals("l1", actual.getBody().title);
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
        sut.add(l);
        var actual = sut.getById(l.id);

        assertEquals("l1", actual.getBody().title);
    }
    @Test
    public void getAllCardLists() {
        sut.add(new CardList("l1"));
        var actual = cardListRepo.findAll();

        assertTrue(actual.equals(sut.getAll()));
    }
    @Test
    public void cannotGetCardsFromInvalidList() {
        var cardList = sut.getCardsForList(-1L);

        assertEquals(BAD_REQUEST, cardList.getStatusCode());
    }
    @Test
    public void cannotGetCardsFromNonExistentList() {
        var cardList = sut.getCardsForList(1L);

        assertEquals(BAD_REQUEST, cardList.getStatusCode());
    }
    @Test
    public void getCardsFromOneList() {
        CardList l = new CardList("l1");
        sut.add(l);
        Card c = new Card(l.id,"c1");
        cardCtrl.add(c);
        var cardList = sut.getCardsForList(l.id);

        assertEquals(Arrays.asList(c), cardList.getBody());
    }
    @Test
    public void databaseIsUsed() {
        sut.add(new CardList("l1"));
        boolean actual = cardListRepo.calledMethods.contains("save");
        assertTrue(actual);
    }
}
