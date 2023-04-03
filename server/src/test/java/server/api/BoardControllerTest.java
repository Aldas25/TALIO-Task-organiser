package server.api;

import commons.Board;
import commons.CardList;
import commons.CustomPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class BoardControllerTest {

    private TestBoardRepository boardRepo;
    private TestSimpMessagingTemplate msgs;
    private BoardController boardCtrl;

    @BeforeEach
    public void setup() {
        TestCardRepository cardRepo = new TestCardRepository();
        TestCardListRepository cardListRepo = new TestCardListRepository();

        boardRepo = new TestBoardRepository();
        msgs = new TestSimpMessagingTemplate(null);
        boardCtrl = new BoardController(boardRepo, cardListRepo, msgs);
    }

    @Test
    public void cannotAddNullBoardTest() {
        var actual = boardCtrl.add(new Board(null, new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddEmptyBoardTest() {
        var actual = boardCtrl.add(new Board("", new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getByIdTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        var actual = boardCtrl.getById(board.id);

        assertEquals("b1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getByBadIdTest () {
        var actual = boardCtrl.getById(1L);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getAllBoardsTest() {
        Board board = new Board("b1", new ArrayList<>());
        List<Board> expected = List.of(board);

        boardCtrl.add(new Board("b1", new ArrayList<>()));

        assertEquals(expected, boardCtrl.getAll());
        assertEquals(expected, boardRepo.findAll());
    }

    @Test
    public void databaseIsUsedTest() {
        boardCtrl.add(new Board("b1", new ArrayList<>()));

        boolean actual = boardRepo.calledMethods.contains("save");

        assertTrue(actual);
    }

    @Test
    public void cannotAddCardListWithNullTitleTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        var actual = boardCtrl.addCardList(board.id, new CardList(null, new ArrayList<>()));

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddCardListWithEmptyTitleTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        var actual = boardCtrl.addCardList(board.id, new CardList("", new ArrayList<>()));

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addOneCardListTest() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);
        boardCtrl.addCardList(board.id, new CardList("l1", new ArrayList<>()));

        var boards = boardRepo.findAll();
        var boardWithList = boards.get(0);
        var actual = boardWithList.lists.get(0);

        assertEquals("l1", actual.title);
    }

    @Test
    public void addMessageTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.addMessage(board);

        assertTrue(msgs.calledMethods.contains("convertAndSend /topic/boards/add"));
    }

    @Test
    public void addListMessageTest () {
        Board board = new Board ("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addListMessage(new CustomPair<>(board.id, list));

        assertTrue (msgs.calledMethods.contains("convertAndSend /topic/lists/add"));
    }

    @Test
    public void updateNonExistentBoardTitleTest () {
        Board board = new Board("b1", new ArrayList<>());
        var actual = boardCtrl.updateBoardTitle(1L, board);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void updateBoardTitleTest () {
        Board board1 = new Board("b1", new ArrayList<>());
        Board board2 = new Board("b2", new ArrayList<>());

        boardCtrl.add(board1);

        var actual = boardCtrl.updateBoardTitle(board1.id, board2);

        assertEquals(OK, actual.getStatusCode());
        assertEquals("b2", board1.title);
    }

    @Test
    public void addCardListToNonExistentBoardTest () {
        CardList list = new CardList("l1", new ArrayList<>());

        var actual = boardCtrl.addCardList(1L, list);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getCardListsByBadIdTest () {
        var actual = boardCtrl.getCardListsById(1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getCardListsByIdTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        CardList list = new CardList("l1", new ArrayList<>());
        boardCtrl.addCardList(board.id, list);

        var actual = boardCtrl.getCardListsById(board.id);

        assertEquals(OK, actual.getStatusCode());
        assertEquals(List.of(list), Objects.requireNonNull(actual.getBody()));
    }

    @Test
    public void deleteBoardByBadIdTest () {
        var actual = boardCtrl.deleteBoard(1L);

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void deleteBoardTest () {
        Board board1 = new Board("b1", new ArrayList<>());
        boardCtrl.add(board1);

        Board board2 = new Board("b2", new ArrayList<>());
        boardCtrl.add(board2);

        var actual = boardCtrl.deleteBoard(board1.id);

        assertEquals(OK, actual.getStatusCode());
        assertEquals(1, boardRepo.boards.size());
        assertEquals("b2", boardRepo.boards.get(0).title);
    }

    @Test
    public void getBoardByBadInviteKeyTest () {
        String inviteKey = "fdghjknm";

        var actual = boardCtrl.getBoardbyInviteKey(inviteKey);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getBoardByInviteKeyTest () {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        var actual = boardCtrl.getBoardbyInviteKey(board.inviteKey);

        assertEquals(OK, actual.getStatusCode());
        assertEquals("b1", Objects.requireNonNull(actual.getBody()).title);
    }
}
