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
    private BoardController sut;

    @BeforeEach
    public void setup() {
        TestCardRepository cardRepo = new TestCardRepository();
        TestCardListRepository cardListRepo = new TestCardListRepository();
        boardRepo = new TestBoardRepository();
        sut = new BoardController(boardRepo, cardListRepo);
    }

    @Test
    public void cannotAddNullBoard() {
        var actual = sut.add(new Board(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddEmptyBoard() {
        var actual = sut.add(new Board(""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getById() {
        Board b = new Board("b1");
        sut.add(b);
        var actual = sut.getById(b.id);

        assertEquals("b1", Objects.requireNonNull(actual.getBody()).title);
    }

    @Test
    public void getAllBoards() {
        Board board = new Board("b1");
        List<Board> expected = List.of(board);

        sut.add(new Board("b1"));

        assertEquals(expected, sut.getAll());
        assertEquals(expected, boardRepo.findAll());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(new Board("b1"));
        boolean actual = boardRepo.calledMethods.contains("save");
        assertTrue(actual);
    }

    @Test
    public void cannotAddCardListWithNullTitle() {
        Board b = new Board("b1");
        sut.add(b);
        var actual = sut.addCardList(b.id, new CardList(null, new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddCardListWithEmptyTitle() {
        Board b = new Board("b1");
        sut.add(b);
        var actual = sut.addCardList(b.id, new CardList("", new ArrayList<>()));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addOneCardList() {
        Board b = new Board("b1");
        sut.add(b);
        sut.addCardList(b.id, new CardList("l1", new ArrayList<>()));

        var boards = boardRepo.findAll();
        var boardWithList = boards.get(0);
        var actual = boardWithList.lists.get(0);

        assertEquals("l1", actual.title);
    }

}
