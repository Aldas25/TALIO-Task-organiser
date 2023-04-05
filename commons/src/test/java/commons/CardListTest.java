package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    @Test
    public void checkConstructor() {
        CardList p = new CardList("title", new ArrayList<>());
        assertEquals("title", p.title);
        assertEquals(new ArrayList<Card>(), p.cards);
    }

    @Test
    public void checkEmptyConstructor() {
        CardList list = new CardList();
        assertNull(list.title);
        assertNull(list.cards);
    }

    @Test
    void notEqualsHashCode() {
        var p = new CardList("title", new ArrayList<>());
        var q = new CardList("other", new ArrayList<>());
        p.id = 0;
        q.id = 1;
        assertNotEquals(p, q);
        assertNotEquals(p.hashCode(), q.hashCode());
    }


    @Test
    void testEqualsHashCode() {
        var p = new CardList("title", new ArrayList<>());
        var q = new CardList("title", new ArrayList<>());
        p.id = 5;
        q.id = 5;
        assertEquals(p, q);
        assertEquals(p.hashCode(), q.hashCode());
    }

    @Test
    void testToString() {
        var actual = new CardList( "title", new ArrayList<>()).toString();
        assertTrue(actual.contains(CardList.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("title"));
    }
}