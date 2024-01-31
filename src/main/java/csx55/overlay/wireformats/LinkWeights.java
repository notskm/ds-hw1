package csx55.overlay.wireformats;

import java.io.IOException;

public class LinkWeights extends Event {
    String hostnameA;
    String hostnameB;
    int portNumberA;
    int portNumberB;
    int linkWeight;

    public LinkWeights(String ipA, int portA, String ipB, int portB, int weight) {
        hostnameA = ipA;
        hostnameB = ipB;
        portNumberA = portA;
        portNumberB = portB;
        linkWeight = weight;
    }
    
    public LinkWeights(byte[] bytes) throws IOException {
        super(bytes);
    }

    public String getHostnameA() {
        return hostnameA;
    }

    public String getHostnameB() {
        return hostnameB;
    }

    public int getPortA() {
        return portNumberA;
    }

    public int getPortB() {
        return portNumberB;
    }

    public int getWeight() {
        return linkWeight;
    }

    @Override
    public int getType() {
        return Protocol.LINK_WEIGHTS.ordinal();
    }
}
