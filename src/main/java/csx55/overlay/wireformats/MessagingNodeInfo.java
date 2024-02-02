package csx55.overlay.wireformats;

import java.net.Socket;
import java.util.Objects;

public class MessagingNodeInfo {
    private String hostname;
    private int portNum;
    private Socket nodeSocket;

    public MessagingNodeInfo(String host, int port) {
        this(host, port, null);
    }

    public MessagingNodeInfo(String host, int port, Socket socket) {
        hostname = host;
        portNum = port;
        nodeSocket = socket;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return portNum;
    }

    public Socket getSocket() {
        return nodeSocket;
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
