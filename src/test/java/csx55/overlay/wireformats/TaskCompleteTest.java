package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TaskCompleteTest {
    @Test
    void testTaskCompleteIsAnEvent() {
        TaskComplete event = new TaskComplete("", 0);
        assert(event instanceof Event);
    }
    
    @Test
    void testGetType() {
        TaskComplete event = new TaskComplete("", 0);
        assertEquals(Protocol.TASK_COMPLETE.ordinal(), event.getType());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"localhost", "127.0.0.1"})
    void testGetIp(String ip) {
        TaskComplete event = new TaskComplete(ip, 0);
        assertEquals(ip, event.getIp());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {8923, 5000, 7524})
    void testGetPort(int port) {
        TaskComplete event = new TaskComplete("", port);
        assertEquals(port, event.getPort());
    }
}
