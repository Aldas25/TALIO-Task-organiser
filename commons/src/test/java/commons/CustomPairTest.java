package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CustomPairTest {

    /**
     * Tests the constructor
     */
    @Test
    public void checkConstructor() {
        Card card = new Card();
        Board board = new Board();
        CustomPair pair = new CustomPair(card, board);
        assertEquals(card, pair.id);
        assertEquals(board, pair.var);
    }

    /**
     * Tests empty constructor
     */
    @Test
    public void checkEmptyConstructor() {
        CustomPair pair = new CustomPair();
        assertNull(pair.id);
        assertNull(pair.var);
    }

    @Test
    public void testGetId() {
        Card card = new Card();
        Board board = new Board();
        CustomPair pair = new CustomPair(card, board);
        assertEquals(card, pair.getId());
    }

    @Test
    public void testGetVar() {
        Card card = new Card();
        Board board = new Board();
        CustomPair pair = new CustomPair(card, board);
        assertEquals(board, pair.getVar());
    }

    /**
     * Tests if HashCode is different for 2 different objects and different id
     */
    @Test
    void notEqualsHashCodeDifferentId() {
        Card card1 = new Card();
        card1.id = 1;
        Card card2 = new Card();
        card2.id = 2;

        Board board = new Board();

        CustomPair pair1 = new CustomPair(card1, board);
        CustomPair pair2 = new CustomPair(card2, board);
        assertNotEquals(pair1, pair2);
        assertNotEquals(pair1.hashCode(), pair2.hashCode());
    }

    /**
     * Tests if HashCode is different for 2 different objects and different var
     */
    @Test
    void notEqualsHashCodeDifferentVar() {
        Card card = new Card();
        Board board1 = new Board();
        Board board2 = new Board();
        board1.id = 1;
        board2.id = 2;

        CustomPair pair1 = new CustomPair(card, board1);
        CustomPair pair2 = new CustomPair(card, board2);
        assertNotEquals(pair1, pair2);
        assertNotEquals(pair1.hashCode(), pair2.hashCode());
    }

    /**
     * Tests if HashCode is same for 2 equal objects
     */
    @Test
    void testEqualsHashCode() {
        Card card = new Card();
        Board board = new Board();
        CustomPair pair1 = new CustomPair(card, board);
        CustomPair pair2 = new CustomPair(card, board);
        assertEquals(pair1, pair2);
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }
    
    /**
     * Tests if the toString prints the correct String
     */
    @Test
    void testToString() {
        Card card = new Card("Card title");
        Board board = new Board("Board title", new ArrayList<>());
        CustomPair pair = new CustomPair(card, board);
        String actual = pair.toString();

        assertTrue(actual.contains(CustomPair.class.getSimpleName()));
        assertTrue(actual.contains(Card.class.getSimpleName()));
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("Card title"));
        assertTrue(actual.contains("Board title"));
        assertTrue(actual.contains("\n"));
    }
}