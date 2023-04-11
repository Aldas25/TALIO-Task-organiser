package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    /**
     * Tests the constructor
     */
    @Test
    public void checkConstructor() {
        Card p = new Card("card");
        assertEquals("card", p.title);
    }

    /**
     * Tests empty constructor
     */
    @Test
    public void checkEmptyConstructor() {
        Card c = new Card();
        assertNull(c.title);
    }

    /**
     * Tests if HashCode is different for 2 different objects
     */
    @Test
    void notEqualsHashCode() {
        Card a = new Card("card1");
        Card b = new Card("card2");
        a.id = 0;
        b.id = 1;
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Tests if hashcode is the same for 2 equal objects
     */
    @Test
    void testEqualsHashCode() {
        Card a = new Card("card");
        Card b = new Card("card");
        a.id = 5;
        b.id = 5;
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Tests if the toString prints the correct String
     */
    @Test
    void testToString() {
        String actual = new Card("card").toString();
        assertTrue(actual.contains(Card.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("card"));
    }
}