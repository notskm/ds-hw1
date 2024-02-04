package csx55.overlay.wireformats;

public class LinkInfo {
    MessagingNodeInfo nodeA;
    MessagingNodeInfo nodeB;

    int linkWeight;

    public LinkInfo(String hostA, int portA, String hostB, int portB, int weight) {
        nodeA = new MessagingNodeInfo(hostA, portA);
        nodeB = new MessagingNodeInfo(hostB, portB);
        linkWeight = weight;
    }

    public String getHostnameA() {
        return nodeA.getHostname();
    }

    public String getHostnameB() {
        return nodeB.getHostname();
    }

    public int getPortA() {
        return nodeA.getPort();
    }

    public int getPortB() {
        return nodeB.getPort();
    }

    public Integer getWeight() {
        return linkWeight;
    }

    @Override
    public String toString() {
        return nodeA.toString() + ' ' + nodeB.toString() + ' ' + getWeight();
    }
}
