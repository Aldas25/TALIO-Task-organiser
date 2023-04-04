package client.services;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServerUtilsTest {

    private ServerUtils sut;

    @BeforeEach
    public void setUp() {
        sut = new ServerUtils();
    }

    @Test
    public void testGetServerNull() {
        assertNull(sut.getServer());
    }

    @Test
    public void testSetAndGetServer() {
        sut.setServer("server");
        assertEquals("server", sut.getServer());
    }

    @Test
    public void testGetHttpServer() {
        sut.setServer("server");
        assertEquals("http://server", sut.getHttpServer());
    }

    @Test
    public void testGetWebSocketServerWithoutEndingSlash() {
        sut.setServer("server");
        assertEquals("ws://server/websocket", sut.getWebSocketServer());
    }

    @Test
    public void testGetWebSocketServer() {
        sut.setServer("server");
        assertEquals("ws://server/websocket", sut.getWebSocketServer());
    }

}
