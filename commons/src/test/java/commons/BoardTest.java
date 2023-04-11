package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    /**
     * Tests the constructor
     */
    @Test
    public void checkConstructor() {
        Board b = new Board("b1", new ArrayList<>());
        assertEquals("b1", b.title);
        assertEquals(new ArrayList<CardList>(), b.lists);
        assertNotNull(b.inviteKey);
        assertEquals(4, b.inviteKey.length());
    }

    /**
     * Tests empty constructor
     */
    @Test
    public void checkEmptyConstructor() {
        Board b = new Board();
        assertNull(b.title);
        assertNull(b.lists);
        assertNull(b.inviteKey);
    }

    /**
     * Tests if HashCode is different for 2 different objects
     */
    @Test
    void notEqualsHashCode() {
        Board p = new Board("b1", new ArrayList<>());
        Board q = new Board("b2", new ArrayList<>());
        p.id = 0;
        q.id = 1;
        assertNotEquals(p, q);
        assertNotEquals(p.hashCode(), q.hashCode());
    }


    /**
     * Tests if hashcode is the same for 2 equal objects
     */
    @Test
    void testEqualsHashCode() {
        Board p = new Board("b1", new ArrayList<>());
        Board q = new Board("b1", new ArrayList<>());
        p.id = 5;
        q.id = 5;
        assertEquals(p, q);
        assertEquals(p.hashCode(), q.hashCode());
    }

    /**
     * Tests if the toString prints the correct String
     */
    @Test
    void testToString() {
        String actual = new Board( "b1", new ArrayList<>()).toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("b1"));
    }
}
