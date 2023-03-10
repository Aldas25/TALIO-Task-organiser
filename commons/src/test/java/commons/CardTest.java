package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void checkConstructor() {
        var p = new Card(1, "card");
        assertEquals(1, p.cardListId);
        assertEquals("card", p.title);
    }

    @Test
    void notEqualsHashCode() {
        var a = new Card(1, "card");
        var b = new Card(2, "cards");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testEqualsHashCode() {
        var a = new Card(1, "card");
        var b = new Card(1, "card");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Card(1, "card").toString();
        assertTrue(actual.contains(Card.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("card"));
    }
}