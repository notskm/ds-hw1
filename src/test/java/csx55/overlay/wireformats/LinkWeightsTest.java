package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LinkWeightsTest {
    @Test
    void testLinkWeightsIsAnEvent() {
        LinkWeights linkWeights = new LinkWeights(new LinkInfo[0]);
        assert (linkWeights instanceof Event);
    }

    @Test
    void testGetType() {
        LinkWeights linkWeights = new LinkWeights(new LinkInfo[0]);
        assertEquals(Protocol.LINK_WEIGHTS.ordinal(), linkWeights.getType());
    }

    @Test
    void testGetLinks() {
        LinkInfo[] expected = new LinkInfo[5];
        expected[0] = new LinkInfo("localhost", 5000, "localhost", 5001, 1);
        expected[1] = new LinkInfo("127.0.0.1", 8327, "localhost", 7439, 2);
        expected[2] = new LinkInfo("localhost", 5000, "localhost", 5001, 5);
        expected[3] = new LinkInfo("localhost", 5000, "localhost", 5001, 3);
        expected[4] = new LinkInfo("localhost", 5000, "localhost", 5001, 10);

        LinkWeights linkWeights = new LinkWeights(expected);
        LinkInfo[] links = linkWeights.getLinks();

        assertEquals(expected, links);
    }
}
