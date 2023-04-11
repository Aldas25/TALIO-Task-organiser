package server.api;

import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.BoardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardServiceTest {

    private BoardService sut;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;

    @BeforeEach
    public void setUp() {
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        sut = new BoardService(boardRepo, cardListRepo);
    }

    // helper method for tests
    private Board constructBoard() {
        Board b = new Board("Board", new ArrayList<>());
        b.id = 1;
        return b;
    }

    // helper method for tests
    private Board constructBoard(String title, int id) {
        Board b = new Board(title, new ArrayList<>());
        b.id = id;
        return b;
    }

    // helper method for tests
    private Board constructBoard(int id) {
        Board b = new Board("Board", new ArrayList<>());
        b.id = id;
        return b;
    }

    @Test
    public void testAddNullTitle() {
        Board b = new Board(null, new ArrayList<>());
        assertThrows(Exception.class, () -> sut.add(b));
    }

    @Test
    public void testAddEmptyTitle() {
        Board b = new Board("", new ArrayList<>());
        assertThrows(Exception.class, () -> sut.add(b));
    }

    @Test
    public void testAdd() throws Exception {
        Board b = constructBoard();
        Board saved = sut.add(b); // should now throw Exception
                                  // this is why the method is left with unhandled exception

        assertEquals(constructBoard(), saved);

        // check if repo was called correctly
        assertBoardRepoCall("save");
        assertEquals(List.of(b), boardRepo.boards);
    }

    @Test
    public void testGetAllEmpty() {
        List<Board> actual = sut.getAll();
        assertTrue(actual.isEmpty());
        assertBoardRepoCall("findAll");
    }

    @Test
    public void testGetAll() throws Exception {
        Board b1 = constructBoard(1);
        Board b2 = constructBoard(2);
        sut.add(b1); // should not throw exception
        sut.add(b2); // should not throw exception

        assertEquals(List.of(b1, b2), sut.getAll());

        // check if repo behaviour was correct
        assertEquals(List.of(b1, b2), boardRepo.boards);
        assertBoardRepoCall("findAll");
    }

    @Test
    public void testGetByIdNegative() {
        assertThrows(Exception.class, () -> sut.getById(-1));
    }

    @Test
    public void testGetByIdNonExistent() {
        assertThrows(Exception.class, () -> sut.getById(0));
        assertBoardRepoCall("existsById");
    }

    @Test
    public void testGetById() throws Exception {
        Board b = constructBoard(1);
        sut.add(b); // should not throw exception
        Board actual = sut.getById(1);
        assertEquals(b, actual);
        assertBoardRepoCall("findById");
    }

    @Test
    public void testUpdateBoardTitleNonExistent() {
        assertThrows(Exception.class, () -> sut.updateBoardTitle(1, "New title"));
    }

    @Test
    public void testUpdateBoardTitle() throws Exception {
        Board b = constructBoard("Old title", 1);
        sut.add(b); // should not throw exception
        Board actual = sut.updateBoardTitle(1, "New title");

        assertEquals(1, actual.id);
        assertEquals("New title", actual.title);

        // check if board was updated in the repository
        assertBoardRepoCall("findById");
        assertBoardRepoCall("save");
        assertEquals(1, boardRepo.boards.size());
        assertEquals("New title", boardRepo.boards.get(0).title);
    }

    @Test
    public void testAddCardListEmptyTitle() throws Exception {
        Board b = constructBoard(1);
        sut.add(b); // should not throw exception

        CardList list = new CardList("", new ArrayList<>());
        assertThrows(Exception.class, () -> sut.addCardList(1, list));
    }

    @Test
    public void testAddCardList() throws Exception {
        Board b = constructBoard(1);
        sut.add(b); // should not throw exception

        CardList list = new CardList("List", new ArrayList<>());
        list.id = 2;

        CardList actual = sut.addCardList(1, list);
        assertEquals(list, actual);
        assertEquals(List.of(list), b.lists);

        // check if correct repository methods were called
        assertCardListRepoCall("save");
        assertBoardRepoCall("save");
        assertEquals(1, boardRepo.boards.size());
        assertEquals(1, cardListRepo.cardLists.size());
    }

    @Test
    public void testGetCardListsById() throws Exception {
        Board b = constructBoard(1);
        sut.add(b); // should not throw exception

        CardList list = new CardList("List", new ArrayList<>());
        list.id = 2;

        sut.addCardList(1, list); // should not throw exception
        assertEquals(List.of(list), sut.getCardListsById(1));
        assertBoardRepoCall("findById");
    }

    @Test
    public void testDeleteBoard() throws Exception {
        Board b = constructBoard(1);
        sut.add(b); // should not throw exception

        sut.deleteBoard(1);

        // check that board was deleted (correct repo calls were made)
        assertBoardRepoCall("deleteById");
        assertEquals(0, boardRepo.boards.size());
    }

    @Test
    public void testGetBoardByInviteKeyWrong() {
        assertThrows(Exception.class, () -> sut.getBoardByInviteKey("A"));
    }

    @Test
    public void testGetBoardByInviteKeyNonExistent() {
        assertThrows(Exception.class, () -> sut.getBoardByInviteKey("AAAA"));
    }

    @Test
    public void testGetBoardByInviteKey() throws Exception {
        Board b1 = constructBoard(1);
        b1.inviteKey = "AAAA";
        Board b2 = constructBoard(2);
        b2.inviteKey = "BBBB";
        sut.add(b1); // should not throw exception
        sut.add(b2); // should not throw exception

        Board actual = sut.getBoardByInviteKey("BBBB");
        assertEquals(b2, actual);
        assertEquals("BBBB", actual.inviteKey);
    }

    private void assertBoardRepoCall(String expectedCall) {
        assertTrue(boardRepo.calledMethods.contains(expectedCall));
    }

    private void assertCardListRepoCall(String expectedCall) {
        assertTrue(cardListRepo.calledMethods.contains(expectedCall));
    }

}
