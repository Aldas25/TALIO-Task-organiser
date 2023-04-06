package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    public void checkConstructor() {
        Board b = new Board("b1", new ArrayList<>());
        assertEquals("b1", b.title);
        assertEquals(new ArrayList<CardList>(), b.lists);
        assertNotNull(b.inviteKey);
        assertEquals(4, b.inviteKey.length());
    }

    @Test
    public void checkEmptyConstructor() {
        Board b = new Board();
        assertNull(b.title);
        assertNull(b.lists);
        assertNull(b.inviteKey);
    }

    @Test
    void notEqualsHashCode() {
        Board p = new Board("b1", new ArrayList<>());
        Board q = new Board("b2", new ArrayList<>());
        p.id = 0;
        q.id = 1;
        assertNotEquals(p, q);
        assertNotEquals(p.hashCode(), q.hashCode());
    }


    @Test
    void testEqualsHashCode() {
        Board p = new Board("b1", new ArrayList<>());
        Board q = new Board("b1", new ArrayList<>());
        p.id = 5;
        q.id = 5;
        assertEquals(p, q);
        assertEquals(p.hashCode(), q.hashCode());
    }

    @Test
    void testToString() {
        String actual = new Board( "b1", new ArrayList<>()).toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("b1"));
    }
}
