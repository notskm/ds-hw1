package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LinkWeightsTest {
    @Test
    void testLinkWeightsIsAnEvent() {
        LinkWeights linkWeights = new LinkWeights("", 1, "", 1, 1);
        assert (linkWeights instanceof Event);
    }

    @Test
    void testGetType() {
        LinkWeights linkWeights = new LinkWeights("", 1, "", 1, 1);
        assertEquals(Protocol.LINK_WEIGHTS.ordinal(), linkWeights.getType());
    }

    @ParameterizedTest
    @ValueSource(strings = { "localhost", "127.0.0.1", "123.234.345.456" })
    void testGetHostnameA(String hostname) {
        LinkWeights linkWeights = new LinkWeights(hostname, 1, "", 1, 1);
        assertEquals(hostname, linkWeights.getHostnameA());
    }

    @ParameterizedTest
    @ValueSource(strings = { "localhost", "127.0.0.1", "123.234.345.456" })
    void testGetHostnameB(String hostname) {
        LinkWeights linkWeights = new LinkWeights("", 1, hostname, 1, 1);
        assertEquals(hostname, linkWeights.getHostnameB());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void testGetPortA(int port) {
        LinkWeights linkWeights = new LinkWeights("", port, "", 1, 1);
        assertEquals(port, linkWeights.getPortA());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void testGetPortB(int port) {
        LinkWeights linkWeights = new LinkWeights("", 1, "", port, 1);
        assertEquals(port, linkWeights.getPortB());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
    void testGetWeight(int weight) {
        LinkWeights linkWeights = new LinkWeights("", 0, "", 0, weight);
        assertEquals(weight, linkWeights.getWeight());
    }
}
