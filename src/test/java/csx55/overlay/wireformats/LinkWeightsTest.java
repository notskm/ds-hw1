package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LinkWeightsTest {
    @Test
    void testLinkWeightsIsAnEvent() {
        LinkWeights linkWeights = new LinkWeights();
        assert (linkWeights instanceof Event);
    }
    
    @Test
    void testGetType() {
        LinkWeights linkWeights = new LinkWeights();
        assertEquals(Protocol.LINK_WEIGHTS.ordinal(), linkWeights.getType());
    }
    
}
