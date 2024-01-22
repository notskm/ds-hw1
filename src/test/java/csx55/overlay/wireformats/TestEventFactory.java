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
}
