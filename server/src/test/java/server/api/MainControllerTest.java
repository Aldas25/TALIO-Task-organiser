package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.MainController;

import static org.junit.jupiter.api.Assertions.*;

public class MainControllerTest {

    private MainController sut;

    @BeforeEach
    public void setup() {
        sut = new MainController();
    }

    @Test
    public void testIndex() {
        assertEquals("Application is running!", sut.index());
    }

    @Test
    public void testOk() {
        assertTrue(sut.ok());
    }

}
