package csx55.overlay.wireformats;

public class MessagingNodeInfo {
    private String hostname;
    private int portNum;
    
    public MessagingNodeInfo(String host, int port) {
        hostname = host;
        portNum = port;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public int getPort() {
        return portNum;
    }
}
