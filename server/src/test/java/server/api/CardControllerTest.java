package server.api;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardControllerTest {
    public int nextInt;
    private MyRandom random;
    private TestCardRepository cardRepo;
    private CardController sut;
    private TestCardListRepository cardListRepo;

    @BeforeEach
    public void setup() {
        random = new CardControllerTest.MyRandom();
        cardRepo = new TestCardRepository();
        cardListRepo = new TestCardListRepository();
        sut = new CardController(random, cardRepo, cardListRepo);
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

        assertTrue(actual.equals(sut.getAll()));
    }

    @Test
    public void databaseIsUsed() {
        sut.add(new Card(1L,"c1"));
        boolean actual = cardRepo.calledMethods.contains("save");
        assertTrue(actual);
    }

    @SuppressWarnings("serial")
    public class MyRandom extends Random {
        public boolean wasCalled = false;
        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}
