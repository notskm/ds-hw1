package csx55.overlay.wireformats;

import java.io.IOException;

public class LinkWeights implements Event {

    @Override
    public byte[] getBytes() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBytes'");
    }

    @Override
    public int getType() {
        return Protocol.LINK_WEIGHTS.ordinal();
    }
    
}
