package csx55.overlay.wireformats;

import java.util.Objects;

public class MessagingNodeInfo {
    private String hostname;
    private int portNum;

    public MessagingNodeInfo(String host, int port) {
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return portNum;
    }

    @Override
    public boolean equals(Object other) {
        return other != null
                && other.getClass() == getClass()
                && ((MessagingNodeInfo) other).hostname.equals(hostname)
                && ((MessagingNodeInfo) other).portNum == portNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, portNum);
    }
}
