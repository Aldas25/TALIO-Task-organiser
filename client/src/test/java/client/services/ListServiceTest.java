package client.services;

import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class ListServiceTest {

    private ListService sut;

    private TestServerUtils server;

    /**
     * The setup of the variables
     */
    @BeforeEach
    public void setup() {
        server = new TestServerUtils();
        sut = new ListService(server);
    }

    /**
     * Tests the updateListTitle function from ListService
     */
    @Test
    public void testUpdateListTitle(){
        CardList list = new CardList("Title", new ArrayList<>());
        sut.updateListTitle(list, "New Title");
        assertEquals("New Title", list.title);
    }
}
