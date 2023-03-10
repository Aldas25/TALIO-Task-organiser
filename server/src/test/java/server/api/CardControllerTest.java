package server.api;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardControllerTest {
    public int nextInt;
    private TestCardRepository cardRepo;
    private CardController sut;
    private TestCardListRepository cardListRepo;

    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        cardListRepo = new TestCardListRepository();
        sut = new CardController(cardRepo, cardListRepo);
    }

    @Test
    public void cannotAddCardWithNullTitle() {
        var actual = sut.add(new Card(0L, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
    @Test
    public void cannotAddCardWithEmptyTitle() {
        var actual = sut.add(new Card(0L, ""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
/*
    Note that few tests below will fail in the future because we will implement a functionality that
    does not let a card to be added to the list which does not exist (this happens either by entering a non-existent
    list's id or leaving it empty)
 */
    @Test
    public void addOneCard() {
        sut.add(new Card(1L,"c1"));

        var lists = cardRepo.findAll();
        var actual = ResponseEntity.ok(lists.get(0));

        assertEquals("c1", actual.getBody().title);
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
        Card c = new Card(1L, "c1");
        sut.add(c);
        var actual = sut.getById(c.id);

        assertEquals("c1", actual.getBody().title);
    }
    @Test
    public void getAllCards() {
        sut.add(new Card(1L,"c1"));
        var actual = cardRepo.findAll();

        assertEquals(actual, sut.getAll());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(new Card(1L,"c1"));
        boolean actual = cardRepo.calledMethods.contains("save");
        assertTrue(actual);
    }
}
