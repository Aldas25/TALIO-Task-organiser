package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    public void checkConstructor() {
        var t = new Tag("tag", "Black");
        assertEquals("tag", t.title);
    }

    @Test
    void notEqualsHashCode() {
        var t = new Tag("tag", "Black");
        var c = new Tag("secondTag", "Red");
        assertNotEquals(t, c);
        assertNotEquals(t.hashCode(), c.hashCode());
    }

    @Test
    void testEqualsHashCode() {
        var t = new Tag("tag", "Black");
        var c = new Tag("tag", "Black");
        assertEquals(t, c);
        assertEquals(t.hashCode(), c.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Tag("tag", "Black").toString();
        assertTrue(actual.contains(Tag.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("tag"));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("Black"));
    }
}