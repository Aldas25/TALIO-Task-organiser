package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    public void checkConstructor() {
        var p = new Board("b1", new ArrayList<>());
        assertEquals("b1", p.title);
    }

    @Test
    void notEqualsHashCode() {
        var p = new Board("b1", new ArrayList<>());
        var q = new Board("b2", new ArrayList<>());
        p.id = 0;
        q.id = 1;
        assertNotEquals(p, q);
        assertNotEquals(p.hashCode(), q.hashCode());
    }


    @Test
    void testEqualsHashCode() {
        var p = new Board("b1", new ArrayList<>());
        var q = new Board("b1", new ArrayList<>());
        assertEquals(p, q);
        assertEquals(p.hashCode(), q.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Board( "b1", new ArrayList<>()).toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("b1"));
    }
}
