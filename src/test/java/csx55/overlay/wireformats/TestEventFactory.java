package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestEventFactory {
    @Test
    public void testGetInstance() {
        EventFactory factory = EventFactory.getInstance();
        assert(factory != null);
    }
    
    @Test
    public void testInstanceAreTheSame() {
        EventFactory factory1 = EventFactory.getInstance();
        EventFactory factory2 = EventFactory.getInstance();
        assertEquals(factory1, factory2);
    }
    
    @Test
    public void testGetEventReturnsNullGivenEmptyByteArray() {
        EventFactory factory = EventFactory.getInstance();
        Event event = factory.getEvent(new byte[0]);
        assertNull(event);
    }
    
    @Test
    public void testGetEventReturnsRegisterEvent() {
        EventFactory factory = EventFactory.getInstance();
        Register registerEvent = new Register("localhost", 5000);

        try {
            Event event = factory.getEvent(registerEvent.getBytes());
            assertTrue(event instanceof Register);
            assertEquals(event.getType(), Protocol.REGISTER_REQUEST.ordinal());
            assertArrayEquals(registerEvent.getBytes(), event.getBytes());
        }
        catch(Exception e) {
            System.err.println(e);
            fail("Exception thrown when getting Register event from factory.");
        }
    }
}
