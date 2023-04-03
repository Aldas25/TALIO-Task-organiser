package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class AdminControllerTest {

    private AdminController sut;
    private TestBoardRepository boardRepository;
    private TestRandom random;
    private final String password = "AAAAAAAAAA";

    @BeforeEach
    public void setup() {
        boardRepository = new TestBoardRepository();
        random = new TestRandom();

        sut = new AdminController(boardRepository, random);
    }

    @Test
    public void testGeneratePassword() {
        sut.generatePassword();
        assertEquals(password, sut.getAdminPassword());
        assertRandomWasUsed(20);
    }

    @Test
    public void testConstructorGeneratedPassword() {
        assertEquals(password, sut.getAdminPassword());
        assertRandomWasUsed(10);
    }

    @Test
    public void testCheckPasswordCorrect() {
        assertTrue(sut.checkAdminPassword(password));
    }

    @Test
    public void testCheckPasswordWrong() {
        assertFalse(sut.checkAdminPassword("abc"));
    }

    @Test
    public void testGetAllBoardsWrongPassword() {
        ResponseEntity<List<Board>> responseEntity = sut.getAllBoards("abc");
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllBoards() {
        Board b1 = new Board("B1", new ArrayList<>());
        b1.id = 1;

        Board b2 = new Board("B2", new ArrayList<>());
        b2.id = 2;

        boardRepository.boards.add(b1);
        boardRepository.boards.add(b2);
        ResponseEntity<List<Board>> responseEntity = sut.getAllBoards(password);
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(List.of(b1, b2), responseEntity.getBody());
    }

    public void assertRandomWasUsed(int times) {
        assertTrue(random.calledMethods.contains("nextInt"));
        assertEquals(times, random.calledMethods.size());
    }

    class TestRandom extends Random {

        public List<String> calledMethods = new ArrayList<>();

        @Override
        public int nextInt(int bound) {
            calledMethods.add("nextInt");
            return 0;
        }

    }

}
