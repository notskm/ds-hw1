package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestDeregister {
    @Test
    void testDeregisterIsAnEvent() {
        Deregister deregister = new Deregister("localhost", 5000);
        assert(deregister instanceof Event);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"localhost", "192.169.0.1"})
    void testGetIpAddress(String ipAddress) {
        Deregister deregister = new Deregister(ipAddress, 0);
        assertEquals(ipAddress, deregister.getIpAddress());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {5000, 5001, 8000})
    void testGetPort(int port) {
        Deregister deregister = new Deregister("localhost", port);
        assertEquals(port, deregister.getPort());
    }
    
    @Test
    void testGetType() {
        Deregister deregister = new Deregister("localhost", 5000);
        assertEquals(Protocol.DEREGISTER_REQUEST.ordinal(), deregister.getType());
    }
}
