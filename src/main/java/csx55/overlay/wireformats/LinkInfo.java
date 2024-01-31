package csx55.overlay.wireformats;

public class LinkInfo {
    String hostnameA;
    int linkPortA;

    String hostnameB;
    int linkPortB;

    int linkWeight;

    public LinkInfo(String hostA, int portA, String hostB, int portB, int weight) {
        hostnameA = hostA;
        hostnameB = hostB;
        linkPortA = portA;
        linkPortB = portB;
        linkWeight = weight;
    }

    public String getHostnameA() {
        return hostnameA;
    }

    public String getHostnameB() {
        return hostnameB;
    }

    public int getPortA() {
        return linkPortA;
    }

    public int getPortB() {
        return linkPortB;
    }

    public Integer getWeight() {
        return linkWeight;
    }
}
