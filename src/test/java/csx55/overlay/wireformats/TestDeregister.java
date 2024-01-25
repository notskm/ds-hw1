package csx55.overlay.wireformats;

import org.junit.jupiter.api.Test;

public class TestDeregister {
    @Test
    void testDeregisterIsAnEvent() {
        Deregister deregister = new Deregister();
        assert(deregister instanceof Event);
    }
}
