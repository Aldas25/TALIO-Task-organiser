package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.BoardService;
import server.services.CardListService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardListServiceTest {

    private CardListService sut;
    private BoardService boardService;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;
    private TestCardRepository cardRepo;

    @BeforeEach
    public void setup() {
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        cardRepo = new TestCardRepository();

        boardService = new BoardService(boardRepo, cardListRepo);
        sut = new CardListService(cardListRepo, cardRepo, boardRepo);
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
    public void testGetByIdNegative() {
        assertThrows(Exception.class, () -> sut.getById(-1));
    }

    @Test
    public void testGetByIdNonExistent() {
        assertThrows(Exception.class, () -> sut.getById(1));
        assertCardListRepoCall("existsById");
    }

    @Test
    public void testGetById() throws Exception {
        CardList list = constructList(5); // should not throw exception
        CardList actual = sut.getById(5);

        assertEquals(list, actual);

        assertCardListRepoCall("existsById");
        assertCardListRepoCall("findById");
    }

    @Test
    public void testAddCardEmpty() throws Exception {
        CardList list = constructList(5); // should not throw exception

        Card card = new Card("");

        assertThrows(Exception.class, () -> sut.addCard(5L, card));
    }

    @Test
    public void testAddCardNull() throws Exception {
        CardList list = constructList(5); // should not throw exception

        Card card = new Card(null);

        assertThrows(Exception.class, () -> sut.addCard(5L, card));
    }

    @Test
    public void testAddCard() throws Exception {
        CardList list = constructList(5); // should not throw exception

        Card card = new Card("Card");
        card.id = 10;

        Card actual = sut.addCard(5L, card);

        assertEquals(card, actual);
        assertEquals(List.of(card), list.cards);

        // check for correct repo behaviour
        assertCardRepoCall("save");
        assertCardListRepoCall("save");
        assertEquals(List.of(card), cardRepo.cards);
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals(List.of(card), cardListRepo.cardLists.get(0).cards);
    }

    @Test
    public void testGetCardsForList() throws Exception {
        CardList list = constructList(5); // should not throw exception

        // add first card to list
        Card card1 = new Card("Card 1");
        card1.id = 10;
        sut.addCard(5L, card1);

        // add second card to list
        Card card2 = new Card("Card 2");
        card2.id = 11;
        sut.addCard(5L, card2);

        assertEquals(List.of(card1, card2), sut.getCardsForList(5L));
    }

    @Test
    public void testDeleteList() throws Exception {
        CardList list = constructList(5); // should not throw exception
        sut.deleteList(5L);

        // check that there is no list in the repo
        assertCardListRepoCall("deleteById");
        assertEquals(0, cardListRepo.cardLists.size());
        assertEquals(1, boardRepo.boards.size());
        assertEquals(0, boardRepo.boards.get(0).lists.size());
    }

    @Test
    public void testUpdateListTitle() throws Exception {
        CardList list = constructList(5, "Old title"); // should not throw exception
        CardList actual = sut.updateListTitle(5L, "New title");
        assertEquals(5L, actual.id);
        assertEquals("New title", actual.title);

        // check repo behaviour
        assertCardListRepoCall("save");
        assertEquals(1, cardListRepo.cardLists.size());
        assertEquals("New title", cardListRepo.cardLists.get(0).title);
    }

    @Test
    public void testGetBoard() throws Exception {
        Board b1 = new Board("Bad board", new ArrayList<>());
        boardService.add(b1);

        CardList list = constructList(5); // should not throw exception
        Board board = sut.getBoard(5L);

        assertEquals(1L, board.id);
        assertEquals("Board", board.title);
        assertEquals(List.of(list), board.lists);
    }

    @Test
    public void testGetBoardNotFound() {
        CardList list = new CardList("List", new ArrayList<>());
        list.id = 100L;
        cardListRepo.save(list);

        assertThrows(Exception.class, () -> sut.getBoard(100L));
    }

    private void assertCardListRepoCall(String expectedCall) {
        assertTrue(cardListRepo.calledMethods.contains(expectedCall));
    }

    private void assertCardRepoCall(String expectedCall) {
        assertTrue(cardRepo.calledMethods.contains(expectedCall));
    }

}
