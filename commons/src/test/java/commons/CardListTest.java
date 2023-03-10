package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    @Test
    public void checkConstructor() {
        var p = new CardList("title");
        assertEquals("title", p.title);

    }

    @Test
    void notEqualsHashCode() {
        var p = new CardList("title");
        var q = new CardList("other");
        assertNotEquals(p, q);
        assertNotEquals(p.hashCode(), q.hashCode());
    }


    @Test
    void testEqualsHashCode() {
        var p = new CardList("title");
        var q = new CardList("title");
        assertEquals(p, q);
        assertEquals(p.hashCode(), q.hashCode());
    }

    @Test
    void testToString() {
        var actual = new CardList( "title").toString();
        assertTrue(actual.contains(CardList.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("title"));
    }
}