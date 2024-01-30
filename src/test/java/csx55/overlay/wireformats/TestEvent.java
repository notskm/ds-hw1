package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestEvent {
    @Test
    public void testConstructionOfConcreteEvent() {
        Event event = new EmptyEvent();
        assertNotNull(event);
    }

    @Test
    public void testGettingBytes() {
        Event event = new EmptyEvent();

        try {
            byte[] output = event.getBytes();
            byte[] expected = new byte[] { 0, 0, 0, 0 };
            assertArrayEquals(expected, output);
        } catch (IOException e) {
            System.err.println(e);
            fail("Exception thrown when getting bytes from Event.");
        }
    }

    @Test
    public void testGettingType() {
        Event event = new EmptyEvent();
        int type = event.getType();
        assertEquals(0, type);
    }
}

class EmptyEvent extends Event {
    @Override
    public int getType() {
        return 0;
    }
}
