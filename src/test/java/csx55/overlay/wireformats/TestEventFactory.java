package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import csx55.overlay.wireformats.RegisterResponse.Status;

public class TestEventFactory {
    EventFactory factory;

    @BeforeEach
    public void setup() {
        factory = EventFactory.getInstance();
    }

    @Test
    public void testGetInstance() {
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
        Event event = factory.getEvent(new byte[0]);
        assertNull(event);
    }
    
    @Test
    public void testGetEventReturnsRegisterEvent() {
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
    
    @Test
    public void testGetEventReturnsRegisterResponse() {
        RegisterResponse registerResponse = new RegisterResponse(Status.SUCCESS, "");
        
        try {
            Event event = factory.getEvent(registerResponse.getBytes());
            assertTrue(event instanceof RegisterResponse);
            assertEquals(registerResponse.getType(), Protocol.REGISTER_RESPONSE.ordinal());
            assertArrayEquals(registerResponse.getBytes(), event.getBytes());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
