package csx55.overlay.transport;

import org.junit.jupiter.api.Test;

public class TestTCPServerThread {
    @Test
    void testTCPServerThreadIsAThread() {
        TCPServerThread serverThread = new TCPServerThread();
        assert(serverThread instanceof Thread);
    }
}
