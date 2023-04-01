package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private List<Tag> tagList;

    @BeforeEach
    public void start(){
        List<Tag> tagList = new ArrayList<>();
        Tag a = new Tag("title", "Black");
        Tag b = new Tag("secondTitle", "Red");
        tagList.add(a);
        tagList.add(b);
    }

    @Test
    public void checkConstructor() {
        var p = new Card("card", tagList);
        assertEquals("card", p.title);
    }

    @Test
    void notEqualsHashCode() {
        var a = new Card("card1", tagList);
        var b = new Card("card2", tagList);
        a.id = 0;
        b.id = 1;
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testEqualsHashCode() {
        var a = new Card("card", tagList);
        var b = new Card("card", tagList);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Card("card", tagList).toString();
        assertTrue(actual.contains(Card.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("card"));
        assertTrue(actual.contains("\n"));

    }
}