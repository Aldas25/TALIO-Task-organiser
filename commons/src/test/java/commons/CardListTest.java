package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    /**
     * Tests the constructor
     */
    @Test
    public void checkConstructor() {
        CardList p = new CardList("title", new ArrayList<>());
        assertEquals("title", p.title);
        assertEquals(new ArrayList<Card>(), p.cards);
    }

    /**
     * Tests empty constructor
     */
    @Test
    public void checkEmptyConstructor() {
        CardList list = new CardList();
        assertNull(list.title);
        assertNull(list.cards);
    }

    /**
     * Tests if HashCode is different for 2 different objects
     */
    @Test
    void notEqualsHashCode() {
        var p = new CardList("title", new ArrayList<>());
        var q = new CardList("other", new ArrayList<>());
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
        var p = new CardList("title", new ArrayList<>());
        var q = new CardList("title", new ArrayList<>());
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
        var actual = new CardList( "title", new ArrayList<>()).toString();
        assertTrue(actual.contains(CardList.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("title"));
    }
}