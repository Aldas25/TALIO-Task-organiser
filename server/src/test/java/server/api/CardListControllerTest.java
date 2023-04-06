package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class CardListControllerTest {
    private TestCardListRepository cardListRepo;
    private TestBoardRepository boardRepo;
    private CardListController listCtrl;
    private BoardController boardCtrl;
    private TestSimpMessagingTemplate msgs;

    @BeforeEach
    public void setup() {
        TestCardRepository cardRepo = new TestCardRepository();

        cardListRepo = new TestCardListRepository();
        boardRepo = new TestBoardRepository();
        msgs = new TestSimpMessagingTemplate(null);

        listCtrl = new CardListController(cardListRepo, cardRepo, boardRepo, msgs);
        boardCtrl = new BoardController(boardRepo, cardListRepo, msgs);
    }

    @Test
    public void cannotGetByInvalidIdTest() {
        var actual = listCtrl.getById(-1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotGetByNonExistentIdTest() {
        var actual = listCtrl.getById(1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getByIdTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList l = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, l);
        var actual = listCtrl.getById(l.id);

        assertEquals(OK, actual.getStatusCode());
        assertEquals("l1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getAllCardListsTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList cardList = new CardList("l1", new ArrayList<>());
        List<CardList> expected = List.of(cardList);

        boardCtrl.addCardList(board.id, new CardList("l1", new ArrayList<>()));

        assertEquals(expected, listCtrl.getAll());
        assertEquals(expected, cardListRepo.findAll());
    }

    @Test
    public void cannotGetCardsFromInvalidListTest() {
        var cardList = listCtrl.getCardsForList(-1L);

        assertEquals(BAD_REQUEST, cardList.getStatusCode());
    }

    @Test
    public void cannotGetCardsFromNonExistentListTest() {
        var cardList = listCtrl.getCardsForList(1L);

        assertEquals(BAD_REQUEST, cardList.getStatusCode());
    }

    @Test
    public void getCardsFromOneListTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList l = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, l);

        Card c = new Card("c1");
        listCtrl.addCard(l.id, c);

        var cardList = listCtrl.getCardsForList(l.id);

        assertEquals(OK, cardList.getStatusCode());
        assertEquals(List.of(c), cardList.getBody());
    }

    @Test
    public void databaseIsUsedTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        boardCtrl.addCardList(board.id, new CardList("l1", new ArrayList<>()));
        boolean actual = cardListRepo.calledMethods.contains("save");
        assertTrue(actual);
    }

    @Test
    public void cannotAddCardWithNullTitleTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList l = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, l);

        var actual = listCtrl.addCard(l.id,new Card(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddCardWithEmptyTitleTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList l = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, l);

        var actual = listCtrl.addCard(l.id,new Card( ""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addOneCardTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList l = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, l);

        listCtrl.addCard(l.id, new Card("c1"));

        var lists = cardListRepo.findAll();
        var listWithCard = lists.get(0);
        var actual = listWithCard.cards.get(0);

        assertEquals("c1", actual.title);
    }

    @Test
    public void addCardNonExistentListTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);
        Card card = new Card ("c1");

        var actual = listCtrl.addCard(1L, card);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getCardsForNonExistentListTest () {
        var actual = listCtrl.getCardsForList(1L);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getCardsForListTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card1 = new Card ("c1");
        listCtrl.addCard(list.id, card1);

        var actual = listCtrl.getCardsForList(list.id);

        assertEquals(OK, actual.getStatusCode());
        assertEquals(List.of(card1), actual.getBody());
    }

    @Test
    public void deleteNonExistentListTest () {
        var actual = listCtrl.deleteList(1L);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void deleteListTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        var deleteResponse = listCtrl.deleteList(list.id);

        assertEquals(OK, deleteResponse.getStatusCode());
        assertEquals(0, board.lists.size());
    }

    @Test
    public void updateNonExistentListTitleTest () {
        var actual = listCtrl.updateListTitle(1L, new CardList("c1", null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void updateListTitleTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        CardList list2 = new CardList("l2", new ArrayList<>());
        var actual = listCtrl.updateListTitle(list.id, list2);

        assertEquals(OK, actual.getStatusCode());
        assertEquals("l2", list.title);
    }

    @Test
    public void getBoardByBadIdTest () {
        var actual = listCtrl.getBoard(1L);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void moveBadIdListTest () {
        var actual = listCtrl.moveList(1L, 1L, 0);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void moveBadBoardIdListTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        var actual = listCtrl.moveList(list.id, 7L, 0);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void moveListTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        CardList list2 = new CardList("l2", new ArrayList<>());
        boardCtrl.addCardList(board.id, list2);

        var actual = listCtrl.moveList(list.id, board.id, 1);

        assertEquals(OK, actual.getStatusCode());
        assertEquals("l2", board.lists.get(0).title);
    }

    @Test
    public void addCardMessageTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card ("c1");

        listCtrl.addCardMessage(new CustomPair<>(list.id, card));

        assertTrue(msgs.calledMethods.contains("convertAndSend /topic/lists/update"));
    }

    @Test
    public void deleteListMessageTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        listCtrl.deleteListMessage(list.id);

        assertTrue(msgs.calledMethods.contains("convertAndSend /topic/lists/update"));
    }

    @Test
    public void deleteCardMessageTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        Card card = new Card ("c1");

        listCtrl.deleteCardMessage(new CustomPair<>(list.id, list));

        assertTrue(msgs.calledMethods.contains("convertAndSend /topic/lists/update"));
    }
}
