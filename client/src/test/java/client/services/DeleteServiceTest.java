package client.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteServiceTest {

    private DeleteService deleteService;
    private TestServerUtils server;

    @BeforeEach
    public void setUp() {
        server = new TestServerUtils();
        deleteService = new DeleteService(server);
    }

    @Test
    public void testGetObjectToDeleteNull() {
        assertNull(deleteService.getObjectToDelete());
    }

    @Test
    public void testSetObjectToDelete() {
        Object o = new Object();
        deleteService.setObjectToDelete(o);
        assertEquals(o, deleteService.getObjectToDelete());
    }

    @Test
    public void testDeleteNull() {
        deleteService.setObjectToDelete(null);
        deleteService.deleteSelectedObject();

        // code should work, and not throw errors
        assertNull(deleteService.getObjectToDelete());
    }

    @Test
    public void testDeleteUnknown() {
        String toDel = "str";
        deleteService.setObjectToDelete(toDel);
        assertThrows(RuntimeException.class, () -> {
            deleteService.deleteSelectedObject();
        });
    }

    @Test
    public void testDeleteCard() {
        Card c = new Card("Title");
        c.id = 5;

        deleteService.setObjectToDelete(c);
        deleteService.deleteSelectedObject();
        assertNull(deleteService.getObjectToDelete());

        // check that server function was called
        assertTrue(server.log.contains("removeCard 5 Title"));
    }

    @Test
    public void testDeleteCardList() {
        CardList list = new CardList("Title", new ArrayList<>());
        list.id = 5;

        deleteService.setObjectToDelete(list);
        deleteService.deleteSelectedObject();
        assertNull(deleteService.getObjectToDelete());

        // check that server function was called
        assertTrue(server.log.contains("removeCardList 5 Title"));
    }

    @Test
    public void testDeleteBoard() {
        Board board = new Board("Title", new ArrayList<>());
        board.id = 5;

        deleteService.setObjectToDelete(board);
        deleteService.deleteSelectedObject();
        assertNull(deleteService.getObjectToDelete());

        // check that server function was called
        assertTrue(server.log.contains("removeBoard 5 Title"));
    }

}
