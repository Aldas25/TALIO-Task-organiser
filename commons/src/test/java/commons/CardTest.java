package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void checkConstructor() {
        var p = new Card("card");
        assertEquals("card", p.title);
    }

    @Test
    void notEqualsHashCode() {
        var a = new Card("card1");
        var b = new Card("card2");
        a.id = 0;
        b.id = 1;
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testEqualsHashCode() {
        var a = new Card("card");
        var b = new Card("card");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Card("card").toString();
        assertTrue(actual.contains(Card.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("card"));
        assertTrue(actual.contains("\n"));

    }
}