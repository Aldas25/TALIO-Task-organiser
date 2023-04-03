package server.api;

import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BoardControllerTest {

    private TestBoardRepository boardRepo;
    private BoardController boardCtrl;

    @BeforeEach
    public void setup() {
        TestCardRepository cardRepo = new TestCardRepository();
        TestCardListRepository cardListRepo = new TestCardListRepository();

        boardRepo = new TestBoardRepository();
        boardCtrl = new BoardController(boardRepo, cardListRepo, null);
    }

    @Test
    public void cannotAddNullBoard() {
        var actual = boardCtrl.add(new Board(null, new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddEmptyBoard() {
        var actual = boardCtrl.add(new Board("", new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getById() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        var actual = boardCtrl.getById(board.id);

        assertEquals("b1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getAllBoards() {
        Board board = new Board("b1", new ArrayList<>());
        List<Board> expected = List.of(board);

        boardCtrl.add(new Board("b1", new ArrayList<>()));

        assertEquals(expected, boardCtrl.getAll());
        assertEquals(expected, boardRepo.findAll());
    }

    @Test
    public void databaseIsUsed() {
        boardCtrl.add(new Board("b1", new ArrayList<>()));

        boolean actual = boardRepo.calledMethods.contains("save");

        assertTrue(actual);
    }

    @Test
    public void cannotAddCardListWithNullTitle() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        var actual = boardCtrl.addCardList(board.id, new CardList(null, new ArrayList<>()));

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddCardListWithEmptyTitle() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);

        var actual = boardCtrl.addCardList(board.id, new CardList("", new ArrayList<>()));

        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addOneCardList() {
        Board board = new Board("b1", new ArrayList<>());
        boardCtrl.add(board);
        boardCtrl.addCardList(board.id, new CardList("l1", new ArrayList<>()));

        var boards = boardRepo.findAll();
        var boardWithList = boards.get(0);
        var actual = boardWithList.lists.get(0);

        assertEquals("l1", actual.title);
    }
}
