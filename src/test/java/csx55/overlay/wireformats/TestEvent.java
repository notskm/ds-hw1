package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class TestEvent {
    @Test
    public void testConstructionOfConcreteEvent() {
        Event event = new EmptyEvent();
        assertNotNull(event);
    }
    
    @Test
    public void testGettingBytes() {
        Event event = new EmptyEvent();
        byte[] output = event.getBytes();
        byte[] expected = new byte[]{1};
        assertEquals(expected[0], output[0]);
    }
    
    @Test
    public void testGettingType() {
        Event event = new EmptyEvent();
        int type = event.getType();
        assertEquals(0, type);
    }
}

class EmptyEvent implements Event {
    @Override
    public byte[] getBytes() {
        return new byte[]{1};
    }

    @Override
    public int getType() {
        return 0;
    }
}
