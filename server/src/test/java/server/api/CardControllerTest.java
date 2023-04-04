package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class CardControllerTest {

    private TestCardRepository cardRepo;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;

    private CardController cardCtrl;
    private CardListController listCtrl;
    private BoardController boardCtrl;
    private TestSimpMessagingTemplate msgs;

    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        msgs = new TestSimpMessagingTemplate(null);

        cardCtrl = new CardController(cardRepo, cardListRepo, msgs);
        listCtrl = new CardListController(cardListRepo, cardRepo, boardRepo, msgs);
        boardCtrl = new BoardController(boardRepo, cardListRepo, msgs);
    }

    @Test
    public void cannotGetByInvalidIdTest() {
        var actual = cardCtrl.getById(-1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotGetByNonExistentIdTest() {
        var actual = cardCtrl.getById(1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getByIdTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card("c1");
        listCtrl.addCard(list.id, card);

        var actual = cardCtrl.getById(card.id);

        assertEquals(OK, actual.getStatusCode());
        assertEquals("c1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getAllCardsTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        listCtrl.addCard(list.id, new Card("c1"));
        var actual = cardRepo.findAll();

        assertEquals(actual, cardCtrl.getAll());
    }

    @Test
    public void databaseIsUsedTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);
        listCtrl.addCard(list.id, new Card("c1"));

        boolean actual = cardRepo.calledMethods.contains("save");
        assertTrue(actual);
    }

    @Test
    public void getListTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card("c1");
        listCtrl.addCard(list.id, card);

        var actual = cardCtrl.getList(card.id);

        assertEquals(OK, actual.getStatusCode());
        assertEquals(list, Objects.requireNonNull(actual.getBody()));
    }

    @Test
    public void getNonExistentListTest () {
        var actual = cardCtrl.getList(1L);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void updateCardTitleTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card("c1");
        listCtrl.addCard(list.id, card);

        Card card2 = new Card("c2");
        var actual = cardCtrl.updateCardTitle(card.id, card2);

        assertEquals(OK, actual.getStatusCode());
        assertEquals("c2", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void updateNonExistentCardTitleTest () {
        Card card = new Card ("c3");
        var actual = cardCtrl.updateCardTitle(1L, card);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void deleteCardTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card deletedCard = new Card("c1");
        listCtrl.addCard(list.id, deletedCard);

        var actual = cardCtrl.deleteCard(deletedCard.id);

        assertEquals(OK, actual.getStatusCode());
        assertEquals(0, board.lists.get(0).cards.size());
    }

    @Test
    public void deleteNonExistentCardTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        var actual = cardCtrl.deleteCard(1L);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void deleteCardNonExistentListTest () {
        Card card = new Card("c1");

        var actual = cardCtrl.deleteCard(card.id);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void moveCardTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card1 = new Card("c1");
        Card card2 = new Card("c2");
        listCtrl.addCard(list.id, card1);
        listCtrl.addCard(list.id, card2);

        var actual = cardCtrl.moveCard(card1.id, list.id, 1);
        var cards = listCtrl.getCardsForList(list.id);

        assertEquals(card1, Objects.requireNonNull(cards.getBody()).get(1));
    }

    @Test
    public void moveNonExistentCardTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card("c1");
        listCtrl.addCard(list.id, card);

        var actual = cardCtrl.moveCard(1L, list.id, 0);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void moveCardBadListTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card("c1");
        listCtrl.addCard(list.id, card);

        var actual = cardCtrl.moveCard(card.id, -1L, 0);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void deleteCardMessageTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card("c1");
        listCtrl.addCard(list.id, card);

        Card card2 = new Card("c2");
        listCtrl.addCard(list.id, card2);

        cardCtrl.deleteCardMessage(card2.id);

        assertTrue(msgs.calledMethods.contains("convertAndSend /topic/cards/delete"));
        assertEquals(1, list.cards.size());
    }

    @Test
    public void updateCardMessageTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card("c1");
        listCtrl.addCard(list.id, card);

        Card card2 = new Card("c2");
        listCtrl.addCard(list.id, card2);

        cardCtrl.updateCardMessage(new CustomPair<>(card.id, card2));

        assertTrue(msgs.calledMethods.contains("convertAndSend /topic/cards/update"));
        assertEquals("c2", card.title);
    }

    @Test
    public void isNullOrEmptyTest () {
        String s = "";

        assertTrue(cardCtrl.isNullOrEmpty(s));
    }
}
