package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.CustomPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.BoardService;
import server.services.CardListService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardListControllerTest {

    private CardListController sut;
    private CardListService cardListService;
    private BoardService boardService;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;
    private TestCardRepository cardRepo;
    private TestSimpMessagingTemplate msgs;

    @BeforeEach
    public void setUp() {
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        cardRepo = new TestCardRepository();
        boardService = new BoardService(boardRepo, cardListRepo);
        cardListService = new CardListService(cardListRepo, cardRepo, boardRepo);
        msgs = new TestSimpMessagingTemplate();

        sut = new CardListController(cardListService, msgs);
    }

    private CardList constructList(long id) throws Exception {
        Board board = new Board("Board", new ArrayList<>());
        board.id = 1;
        boardService.add(board);

        CardList list = new CardList("New list", new ArrayList<>());
        list.id = id;

        boardService.addCardList(1, list);

        return list;
    }

    private CardList constructList(long id, String title) throws Exception {
        Board board = new Board("Board", new ArrayList<>());
        board.id = 1;
        boardService.add(board);

        CardList list = new CardList(title, new ArrayList<>());
        list.id = id;

        boardService.addCardList(1, list);

        return list;
    }

    @Test
    public void testGetAll() throws Exception {
        CardList list = constructList(5); // should not throw exception
        assertEquals(List.of(list), sut.getAll());

        // check that correct repo calls were made
        assertCardListRepoCall("findAll");
    }

    @Test
    public void testGetByIdBad() {
        ResponseEntity<CardList> actual = sut.getById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetByIdOk() throws Exception {
        CardList list = constructList(5); // should not throw exception
        ResponseEntity<CardList> actual = sut.getById(5);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(list, actual.getBody());

        assertCardListRepoCall("findById");
    }

    @Test
    public void testGetCardsForListBad() {
        ResponseEntity<List<Card>> actual = sut.getCardsForList(1L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetCardsForListOk() throws Exception {
        CardList list = constructList(5); // should not throw exception

        // add first card to list
        Card card1 = new Card("Card 1");
        card1.id = 10;
        cardListService.addCard(5L, card1);

        // add second card to list
        Card card2 = new Card("Card 2");
        card2.id = 11;
        cardListService.addCard(5L, card2);

        // Check the method outcome
        ResponseEntity<List<Card>> actual = sut.getCardsForList(5L);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(List.of(card1, card2), actual.getBody());
    }

    @Test
    public void testAddCardBad() {
        ResponseEntity<Card> actual = sut.addCard(1L, new Card("Card"));
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddCardOk() throws Exception {
        CardList list = constructList(5); // should not throw exception

        Card card = new Card("Card");
        card.id = 10;

        ResponseEntity<Card> actual = sut.addCard(5L, card);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(card, actual.getBody());
        assertEquals(List.of(card), list.cards);

        // check for correct repo behaviour
        assertCardRepoCall("save");
        assertCardListRepoCall("save");
        assertEquals(List.of(card), cardRepo.cards);
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals(List.of(card), cardListRepo.cardLists.get(0).cards);
    }

    @Test
    public void testAddCardMessage() throws Exception {
        CardList list = constructList(5); // should not throw exception

        Card card = new Card("Card");
        card.id = 10;

        sut.addCardMessage(new CustomPair(5L, card));
        assertMsgsCall("/topic/lists/update");

        // check for correct repo behaviour
        assertCardRepoCall("save");
        assertCardListRepoCall("save");
        assertEquals(List.of(card), cardRepo.cards);
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals(List.of(card), cardListRepo.cardLists.get(0).cards);
    }

    @Test
    public void testDeleteListBad() {
        ResponseEntity<Boolean> actual = sut.deleteList(1L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testDeleteListOk() throws Exception {
        CardList list = constructList(5); // should not throw exception
        ResponseEntity<Boolean> actual = sut.deleteList(5L);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNull(actual.getBody());

        // check that there is no list in the repo
        assertCardListRepoCall("deleteById");
        assertEquals(0, cardListRepo.cardLists.size());
        assertEquals(1, boardRepo.boards.size());
        assertEquals(0, boardRepo.boards.get(0).lists.size());
    }

    @Test
    public void testDeleteListMessage() throws Exception {
        CardList list = constructList(5); // should not throw exception
        sut.deleteListMessage(5L);

        assertMsgsCall("/topic/lists/update");

        // check that there is no list in the repo
        assertCardListRepoCall("deleteById");
        assertEquals(0, cardListRepo.cardLists.size());
        assertEquals(1, boardRepo.boards.size());
        assertEquals(0, boardRepo.boards.get(0).lists.size());
    }

    @Test
    public void testUpdateListTitleBad() {
        ResponseEntity<CardList> actual = sut.updateListTitle(
                1L, new CardList("T", new ArrayList<>())
        );
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testUpdateListTitleOk() throws Exception {
        CardList list = constructList(5, "Old title"); // should not throw exception
        ResponseEntity<CardList> actual = sut.updateListTitle(
                5L, new CardList("New title", new ArrayList<>())
        );

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(5L, actual.getBody().id);
        assertEquals("New title", actual.getBody().title);

        // check repo behaviour
        assertCardListRepoCall("save");
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals("New title", cardListRepo.cardLists.get(0).title);
    }

    @Test
    public void testUpdateListTitleMessage() throws Exception {
        CardList list = constructList(5, "Old title"); // should not throw exception
        sut.updateListTitleMessage(
                new CustomPair(5L, new CardList("New title", new ArrayList<>()))
        );

        assertMsgsCall("/topic/lists/update");

        // check repo behaviour
        assertCardListRepoCall("save");
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals("New title", cardListRepo.cardLists.get(0).title);
    }

    @Test
    public void testGetBoardBad() {
        ResponseEntity<Board> actual = sut.getBoard(1L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetBoardOk() throws Exception {
        CardList list = constructList(5); // should not throw exception
        ResponseEntity<Board> actual = sut.getBoard(5L);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Board board = actual.getBody();
        assertEquals(1L, board.id);
        assertEquals("Board", board.title);
        assertEquals(List.of(list), board.lists);

    }

    private void assertCardListRepoCall(String expectedCall) {
        assertTrue(cardListRepo.calledMethods.contains(expectedCall));
    }

    private void assertCardRepoCall(String expectedCall) {
        assertTrue(cardRepo.calledMethods.contains(expectedCall));
    }

    private void assertMsgsCall(String destination) {
        assertTrue(msgs.calledMethods.contains("convertAndSend " + destination));
    }
}
