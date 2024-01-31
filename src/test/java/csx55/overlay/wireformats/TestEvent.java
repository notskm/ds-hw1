package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import csx55.overlay.wireformats.RegisterResponse.Status;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.stream.Stream;

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
    
    @ParameterizedTest
    @MethodSource("provideEvents")
    public void testConstruction(Event event) {
        try {
            Constructor<?> ctor = event.getClass().getConstructor(byte[].class);
            Event newEvent = (Event)ctor.newInstance(event.getBytes());
            assertArrayEquals(event.getBytes(), newEvent.getBytes());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
    private static Stream<Arguments> provideEvents() {
        LinkInfo[] links = new LinkInfo[] {
            new LinkInfo("localhost", 5000, "localhost", 50001, 10),
            new LinkInfo("127.0.0.1", 8342, "192.168.0.1", 8430, 5)
        };

        return Stream.of(
            Arguments.of(new Register("localhost", 5000)),
            Arguments.of(new RegisterResponse(Status.SUCCESS, "info")),
            Arguments.of(new Deregister("127.0.0.1", 5012)),
            Arguments.of(new LinkWeights(links))
        );
    }
}

class EmptyEvent extends Event {
    @Override
    public int getType() {
        return 0;
    }
}
