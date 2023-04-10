package client.services;

import client.utils.ImageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImageUtilsTest {

    private ImageUtils sut;

    @BeforeEach
    public void setUp() {
        sut = new ImageUtils();
    }

    @Test
    public void testConstructFilename() {
        String original = "img.png";
        String actual = sut.constructFilename(original);
        String expected = "client/src/main/resources/client/images/img.png";
        assertEquals(expected, actual);
    }

}
