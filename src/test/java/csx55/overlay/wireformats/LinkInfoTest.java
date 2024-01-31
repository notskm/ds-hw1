package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LinkInfoTest {
    @ParameterizedTest
    @ValueSource(strings = {"localhost", "127.0.0.1", "192.168.0.1"})
    void testGetHostnameA(String ip) {
        LinkInfo link = new LinkInfo(ip, 0, "", 0, 0);
        assertEquals(ip, link.getHostnameA());
    }

    @ParameterizedTest
    @ValueSource(strings = {"localhost", "127.0.0.1", "192.168.0.1"})
    void testGetHostnameB(String ip) {
        LinkInfo link = new LinkInfo("", 0, ip, 0, 0);
        assertEquals(ip, link.getHostnameB());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {8000, 5000, 8234})
    void testGetPortA(int port) {
        LinkInfo link = new LinkInfo("", port, "", 0, 0);
        assertEquals(port, link.getPortA());
    }

    @ParameterizedTest
    @ValueSource(ints = {8000, 5000, 8234})
    void testGetPortB(int port) {
        LinkInfo link = new LinkInfo("", 0, "", port, 0);
        assertEquals(port, link.getPortB());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testGetWeight(int weight) {
        LinkInfo link = new LinkInfo("", 0, "", 0, weight);
        assertEquals(weight, link.getWeight());
    }
}
