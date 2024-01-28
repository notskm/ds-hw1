package csx55.overlay.transport;

import org.junit.jupiter.api.Test;

public class TCPReceiverThreadTest {
    @Test
    void testTCPReceiverThreadIsAThread() {
        TCPReceiverThread thread = new TCPReceiverThread();
        assert(thread instanceof Thread);
    }
    
}
