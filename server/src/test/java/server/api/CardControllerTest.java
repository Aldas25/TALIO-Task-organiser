package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.BoardService;
import server.services.CardListService;
import server.services.CardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CardControllerTest {

    private CardController sut;
    private CardService cardService;
    private CardListService cardListService;
    private BoardService boardService;
    private TestCardRepository cardRepo;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;

    private TestSimpMessagingTemplate msgs;

    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        boardService = new BoardService(boardRepo, cardListRepo);
        cardListService = new CardListService(cardListRepo, cardRepo, boardRepo);
        cardService = new CardService(cardRepo, cardListRepo);
        msgs = new TestSimpMessagingTemplate();

        sut = new CardController(cardService, msgs);
    }

    // helper method for tests
    private CardList constructCardList(long id) throws Exception {
        Board board = new Board("b1", new ArrayList<>());
        boardService.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardService.addCardList(board.id, list);
        list.id = id;

        return list;
    }

    private Card constructCard(long id, long cardListId) throws Exception {
        Card card = new Card("c1");
        cardListService.addCard(cardListId, card);
        card.id = id;

        return card;
    }

    @Test
    public void testGetByInvalidId() {
        ResponseEntity<Card> actual = sut.getById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetById() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        ResponseEntity<Card> actual = sut.getById(card.id);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(card, actual.getBody());

        assertCardRepoCall("findById");
    }

    @Test
    public void testGetAllCards() throws Exception {
        constructCardList(1L);
        Card c1 = constructCard(1L, 1L);
        Card c2 = constructCard(2L, 1L);

        assertEquals(List.of(c1, c2), sut.getAll());

        assertEquals(List.of(c1, c2), cardRepo.cards);
        assertCardRepoCall("findAll");
    }

    @Test
    public void testGetCardListByIdOk() throws Exception {
        CardList list = constructCardList(1L);
        Card card = constructCard(1L, 1L);
        ResponseEntity<CardList> actual = sut.getCardListById(card.id);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(list, actual.getBody());
        assertCardRepoCall("findById");
        assertCardListRepoCall("findAll");
    }

    @Test
    public void testGetCardListByIdBad() {
        ResponseEntity<CardList> actual = sut.getCardListById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testUpdateCardTitleBad() throws Exception {
        ResponseEntity<Card> actual = sut.updateCardTitle(1L, new Card("New title"));

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testUpdateCardTitleOk() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        ResponseEntity<Card> actual = sut.updateCardTitle(card.id, new Card("New title"));

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(card.id, actual.getBody().id);
        assertEquals("New title", actual.getBody().title);

        // check if board was updated in the repository
        assertCardRepoCall("findById");
        assertCardRepoCall("save");
        assertEquals(1, cardRepo.cards.size());
        assertEquals("New title", cardRepo.cards.get(0).title);
    }

    @Test
    public void testUpdateCardTitleMessage() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        sut.updateCardMessage(new CustomPair<>(card.id, new Card("New title")));

        assertMsgsCall("/topic/lists/update");

        // check if board was updated in the repository
        assertCardRepoCall("findById");
        assertCardRepoCall("save");
        assertEquals(1, cardRepo.cards.size());
        assertEquals("New title", cardRepo.cards.get(0).title);
    }

    @Test
    public void testDeleteCardBad(){
        ResponseEntity<Boolean> actual = sut.deleteCard(1L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testDeleteCardOk() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        ResponseEntity<Boolean> actual = sut.deleteCard(card.id);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNull(actual.getBody());

        assertCardRepoCall("deleteById");
        assertCardListRepoCall("findAll");
        assertCardListRepoCall("save");
        assertEquals(0, cardRepo.cards.size());
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals(0, cardListRepo.cardLists.get(0).cards.size());
    }

    @Test
    public void testDeleteCardMessage() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        sut.deleteCardMessage(card.id);

        assertMsgsCall("/topic/lists/update");

        assertCardRepoCall("deleteById");
        assertCardListRepoCall("findAll");
        assertCardListRepoCall("save");
        assertEquals(0, cardRepo.cards.size());
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals(0, cardListRepo.cardLists.get(0).cards.size());
    }
    @Test
    public void testMoveCardTestBad () throws Exception {
        CardList list2 = constructCardList(2L);

        ResponseEntity<Card> actual = sut.moveCard(1L, list2.id, 0);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testMoveCardTestOk () throws Exception {
        CardList list1 = constructCardList(1L);
        CardList list2 = constructCardList(2L);
        Card card = constructCard(1L, 1L);

        ResponseEntity<Card> actual = sut.moveCard(card.id, list2.id, 0);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(card, actual.getBody());
        assertEquals(List.of(actual.getBody()), list2.cards);
        assertEquals(List.of(), list1.cards);

        assertCardListRepoCall("findById");
        assertCardRepoCall("findById");
        assertCardListRepoCall("save");
        assertCardRepoCall("save");
        assertEquals(1, cardRepo.cards.size());
    }

    @Test
    public void testMoveCardTestMessage () throws Exception {
        CardList list1 = constructCardList(1L);
        CardList list2 = constructCardList(2L);
        Card card = constructCard(1L, 1L);

        sut.moveCardMessage(new CustomPair<>(card, new CustomPair<>(list2, 0)));

        assertMsgsCall("/topic/lists/update");

        assertCardListRepoCall("findById");
        assertCardRepoCall("findById");
        assertCardListRepoCall("save");
        assertCardRepoCall("save");
        assertEquals(1, cardRepo.cards.size());
    }

    private void assertCardRepoCall(String expectedCall) {
        assertTrue(cardRepo.calledMethods.contains(expectedCall));
    }

    private void assertCardListRepoCall(String expectedCall) {
        assertTrue(cardListRepo.calledMethods.contains(expectedCall));
    }

    private void assertMsgsCall(String destination) {
        assertTrue(msgs.calledMethods.contains("convertAndSend " + destination));
    }

}
