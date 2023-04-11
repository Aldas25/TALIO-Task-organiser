package server.api;

import commons.Board;
import commons.CardList;
import commons.CustomPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.BoardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardControllerTest {

    private BoardController sut;
    private BoardService boardService;
    private TestSimpMessagingTemplate msgs;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;

    @BeforeEach
    public void setUp() {
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        msgs = new TestSimpMessagingTemplate();
        boardService = new BoardService(boardRepo, cardListRepo);
        sut = new BoardController(boardService, msgs);
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
    public void testAddOk() {
        Board b = constructBoard(1);
        ResponseEntity<Board> actual = sut.add(b);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(b, actual.getBody());

        // check if correct repo calls were called
        assertBoardRepoCall("save");
        assertEquals(List.of(b), boardRepo.boards);
    }

    @Test
    public void testAddBad() {
        Board b = constructBoard("", 1);
        ResponseEntity<Board> actual = sut.add(b);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetAll() {
        Board b1 = constructBoard("Board 1", 1);
        Board b2 = constructBoard("Board 2", 2);
        sut.add(b1);
        sut.add(b2);

        assertEquals(List.of(b1, b2), sut.getAll());
        assertBoardRepoCall("findAll");
    }

    @Test
    public void testGetByIdBad() {
        Board b = constructBoard(1);
        sut.add(b);
        ResponseEntity<Board> actual = sut.getById(2);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertBoardRepoCall("existsById");
    }

    @Test
    public void testGetByIdOk() {
        Board b = constructBoard(1);
        sut.add(b);
        ResponseEntity<Board> actual = sut.getById(1);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(b, actual.getBody());
        assertBoardRepoCall("findById");
    }

    @Test
    public void testUpdateBoardTitleOk() {
        Board b = constructBoard("Old title", 1);
        sut.add(b);
        Board b2 = constructBoard("New title", 1);
        ResponseEntity<Board> actual = sut.updateBoardTitle(1, b2);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals("New title", actual.getBody().title);
        assertEquals(1, actual.getBody().id);
        assertBoardRepoCall("save");
    }

    @Test
    public void testUpdateBoardTitleMessage() {
        Board b = constructBoard("Old title", 1);
        sut.add(b);
        Board b2 = constructBoard("New title", 1);

        sut.updateBoardTitleMessage(new CustomPair(1L, b2));
        assertMsgsCall("/topic/boards/update");

        // check the behaviour of repository
        assertEquals(1, boardRepo.boards.size());
        Board bInRepo = boardRepo.boards.get(0);
        assertEquals("New title", bInRepo.title);
        assertEquals(1, bInRepo.id);
        assertBoardRepoCall("save");
    }

    @Test
    public void testUpdateBoardTitleBad() {
        Board b = constructBoard("Old title", 1);
        sut.add(b);
        Board b2 = constructBoard("New title", 1);
        ResponseEntity<Board> actual = sut.updateBoardTitle(2, b2);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddCardListBad() {
        CardList list = new CardList("", new ArrayList<>());
        ResponseEntity<CardList> actual = sut.addCardList(1, list);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddCardListOk() {
        Board b = constructBoard(1);
        sut.add(b);

        CardList list = new CardList("List", new ArrayList<>());
        list.id = 2;

        ResponseEntity<CardList> actual = sut.addCardList(1, list);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(list, actual.getBody());
        assertEquals(List.of(list), b.lists);

        // check if correct repository methods were called
        assertCardListRepoCall("save");
        assertBoardRepoCall("save");
        assertEquals(1, boardRepo.boards.size());
        assertEquals(1, cardListRepo.cardLists.size());
    }

    @Test
    public void testAddCardListMessage() {
        Board b = constructBoard(1);
        sut.add(b);

        CardList list = new CardList("List", new ArrayList<>());
        list.id = 2;

        sut.addListMessage(new CustomPair(1L, list));
        assertEquals(List.of(list), b.lists);
        assertMsgsCall("/topic/lists/update");

        // check if correct repository methods were called
        assertCardListRepoCall("save");
        assertBoardRepoCall("save");
        assertEquals(1, boardRepo.boards.size());
        assertEquals(1, cardListRepo.cardLists.size());
    }

    @Test
    public void testGetCardListsByIdOk() {
        Board b = constructBoard(1);
        sut.add(b);

        CardList list = new CardList("List", new ArrayList<>());
        list.id = 2;
        sut.addCardList(1, list);

        ResponseEntity<List<CardList>> actual = sut.getCardListsById(1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(List.of(list), actual.getBody());
        assertBoardRepoCall("findById");
    }

    @Test
    public void testGetCardListsByIdBad() {
        ResponseEntity<List<CardList>> actual = sut.getCardListsById(1);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testDeleteBoardBad() {
        ResponseEntity<Boolean> actual = sut.deleteBoard(1);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testDeleteBoardOk() {
        Board b = constructBoard(1);
        sut.add(b);

        ResponseEntity<Boolean> actual = sut.deleteBoard(1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNull(actual.getBody());

        // check that board was deleted (correct repo calls were made)
        assertBoardRepoCall("deleteById");
        assertEquals(0, boardRepo.boards.size());
    }

    @Test
    public void testDeleteBoardMessage() {
        Board b = constructBoard(1);
        sut.add(b);

        sut.deleteBoardMessage(1L);
        assertMsgsCall("/topic/boards/update");

        // check that board was deleted (correct repo calls were made)
        assertBoardRepoCall("deleteById");
        assertEquals(0, boardRepo.boards.size());
    }

    @Test
    public void testGetBoardByInviteKeyBad() {
        ResponseEntity<Board> actual = sut.getBoardByInviteKey("AAAA");
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetBoardByInviteKeyOk() {
        Board b1 = constructBoard(1);
        b1.inviteKey = "AAAA";
        Board b2 = constructBoard(2);
        b2.inviteKey = "BBBB";
        sut.add(b1); // should not throw exception
        sut.add(b2); // should not throw exception

        ResponseEntity<Board> actual = sut.getBoardByInviteKey("BBBB");
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(b2, actual.getBody());
        assertEquals("BBBB", actual.getBody().inviteKey);
    }

    @Test
    public void testDeferredResultOk() {
        Board b = constructBoard(1);
        DeferredResult<ResponseEntity<Board>> actual = sut.getUpdates();
        sut.add(b);

        ResponseEntity<Board> result = (ResponseEntity<Board>) actual.getResult();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(b, result.getBody());
    }

    private void assertBoardRepoCall(String expectedCall) {
        assertTrue(boardRepo.calledMethods.contains(expectedCall));
    }

    private void assertCardListRepoCall(String expectedCall) {
        assertTrue(cardListRepo.calledMethods.contains(expectedCall));
    }

    private void assertMsgsCall(String destination) {
        assertTrue(msgs.calledMethods.contains("convertAndSend " + destination));
    }
    
}
