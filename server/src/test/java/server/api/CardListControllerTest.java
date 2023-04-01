package server.api;

import commons.Card;
import commons.CardList;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardListControllerTest {
    private TestCardListRepository cardListRepo;
    private TestTagRepository tagRepo;
    private CardListController sut;

    @BeforeEach
    public void setup() {
        TestCardRepository cardRepo = new TestCardRepository();
        TestBoardRepository boardRepo = new TestBoardRepository();
        tagRepo = new TestTagRepository();
        cardListRepo = new TestCardListRepository();
        sut = new CardListController(cardListRepo, cardRepo, boardRepo, tagRepo, null);
    }

    @Test
    public void cannotAddNullCardList() {
        var actual = sut.add(new CardList(null, new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddEmptyCardList() {
        var actual = sut.add(new CardList("", new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addOneCardList() {
        sut.add(new CardList("l1", new ArrayList<>()));

        var lists = cardListRepo.findAll();

        assertEquals(1, lists.size());
        assertEquals("l1", lists.get(0).title);
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
        sut.add(l);
        var actual = sut.getById(l.id);

        assertEquals("l1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getAllCardLists() {
        CardList cardList = new CardList("l1", new ArrayList<>());
        List<CardList> expected = List.of(cardList);

        sut.add(new CardList("l1", new ArrayList<>()));

        assertEquals(expected, sut.getAll());
        assertEquals(expected, cardListRepo.findAll());
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
        CardList l = new CardList("l1", new ArrayList<>());
        sut.add(l);
        Card c = new Card("c1", new ArrayList<>());
        sut.addCard(l.id,c);
        var cardList = sut.getCardsForList(l.id);

        assertEquals(List.of(c), cardList.getBody());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(new CardList("l1", new ArrayList<>()));
        boolean actual = cardListRepo.calledMethods.contains("save");
        assertTrue(actual);
    }

    @Test
    public void cannotAddCardWithNullTitle() {
        CardList l = new CardList("l1", new ArrayList<>());
        sut.add(l);
        var actual = sut.addCard(l.id,new Card(null, new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddCardWithEmptyTitle() {
        CardList l = new CardList("l1", new ArrayList<>());
        sut.add(l);
        var actual = sut.addCard(l.id,new Card( "", new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addOneCard() {
        CardList l = new CardList("l1", new ArrayList<>());
        sut.add(l);
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("aaa", "blue"));
        sut.addCard(l.id, new Card("c1", tags));

        var lists = cardListRepo.findAll();
        var listWithCard = lists.get(0);
        var actual = listWithCard.cards.get(0);

        assertEquals("c1", actual.title);
        assertEquals("aaa", actual.tagList.get(0).title);
    }

}
