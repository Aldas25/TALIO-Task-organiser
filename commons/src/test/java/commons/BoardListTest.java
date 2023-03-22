package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardListTest {

    @Test
    public void checkConstructor() {
        var p = new BoardList("board list");
        assertEquals("board list", p.title);

    }

    @Test
    void notEqualsHashCode() {
        var p = new BoardList("bl1");
        var q = new BoardList("bl2");
        assertNotEquals(p, q);
        assertNotEquals(p.hashCode(), q.hashCode());
    }


    @Test
    void testEqualsHashCode() {
        var p = new BoardList("bl");
        var q = new BoardList("bl");
        assertEquals(p, q);
        assertEquals(p.hashCode(), q.hashCode());
    }

    @Test
    void testToString() {
        var actual = new BoardList( "title").toString();
        assertTrue(actual.contains(BoardList.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("title"));
    }
}