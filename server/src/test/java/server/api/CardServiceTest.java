package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.BoardService;
import server.services.CardListService;
import server.services.CardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardServiceTest {

    private CardService sut;
    private CardListService cardListService;
    private BoardService boardService;
    private TestCardRepository cardRepo;
    private TestCardListRepository cardListRepo;
    private TestBoardRepository boardRepo;

    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();

        sut = new CardService(cardRepo, cardListRepo);
        cardListService = new CardListService(cardListRepo, cardRepo, boardRepo);
        boardService = new BoardService(boardRepo, cardListRepo);
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
    public void testGetAllEmpty() {
        List<Card> actual = sut.getAll();
        assertTrue(actual.isEmpty());
        assertCardRepoCall("findAll");
    }

    @Test
    public void testGetAll() throws Exception {
        constructCardList(1L);
        Card c1 = constructCard(1L, 1L);
        Card c2 = constructCard(2L, 1L);

        assertEquals(List.of(c1, c2), sut.getAll());

        // check if repo behaviour was correct
        assertEquals(List.of(c1, c2), cardRepo.cards);
        assertCardRepoCall("findAll");
    }

    @Test
    public void testGetByIdNegative() {
        assertThrows(Exception.class, () -> sut.getById(-1));
    }

    @Test
    public void testGetByIdNonExistent() {
        assertThrows(Exception.class, () -> sut.getById(0));
        assertCardRepoCall("existsById");
    }

    @Test
    public void testGetById() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        Card actual = sut.getById(card.id);
        assertEquals(card, actual);
        assertCardRepoCall("findById");
    }

    @Test
    public void testUpdateCardTitleNonExistent() {
        assertThrows(Exception.class, () -> sut.updateCardTitle(1, new Card("c1")));
    }

    @Test
    public void testUpdateCardTitle() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        Card actual = sut.updateCardTitle(card.id, new Card("New title"));

        assertEquals(card.id, actual.id);
        assertEquals("New title", actual.title);

        // check if board was updated in the repository
        assertCardRepoCall("findById");
        assertCardRepoCall("save");
        assertEquals(1, cardRepo.cards.size());
        assertEquals("New title", cardRepo.cards.get(0).title);
    }

    @Test
    public void testGetCardListById() throws Exception {
        constructCardList(1L);
        CardList list = constructCardList(2L);
        Card card = constructCard(1L, 2L);

        assertEquals(list, sut.getCardListById(card.id));
        assertCardRepoCall("findById");
        assertCardListRepoCall("findAll");
    }


    @Test
    public void testGetCardListByIdNotFound() {
        Card card = new Card("Card");
        card.id = 1L;
        cardRepo.save(card);

        assertThrows(Exception.class, () -> sut.getCardListById(1L));
    }

    @Test
    public void testMoveCardTest () throws Exception {
        CardList list1 = constructCardList(1L);
        CardList list2 = constructCardList(2L);
        Card card = constructCard(1L, 1L);

        Card actual = sut.moveCard(card.id, list2.id, 0);

        assertEquals(card, actual);
        assertEquals(List.of(actual), list2.cards);
        assertEquals(List.of(), list1.cards);

        assertCardListRepoCall("findById");
        assertCardRepoCall("findById");
        assertCardListRepoCall("save");
        assertCardRepoCall("save");
        assertEquals(1, cardRepo.cards.size());
    }

    @Test
    public void testDeleteCard() throws Exception {
        constructCardList(1L);
        Card card = constructCard(1L, 1L);
        sut.deleteCard(card.id);

        assertCardRepoCall("deleteById");
        assertCardListRepoCall("findAll");
        assertCardListRepoCall("save");
        assertEquals(0, cardRepo.cards.size());
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals(0, cardListRepo.cardLists.get(0).cards.size());
    }

    private void assertCardRepoCall(String expectedCall) {
        assertTrue(cardRepo.calledMethods.contains(expectedCall));
    }

    private void assertCardListRepoCall(String expectedCall) {
        assertTrue(cardListRepo.calledMethods.contains(expectedCall));
    }

}
