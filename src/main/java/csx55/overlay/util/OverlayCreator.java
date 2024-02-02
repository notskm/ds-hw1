package csx55.overlay.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import csx55.overlay.transport.TCPSender;
import csx55.overlay.wireformats.LinkInfo;
import csx55.overlay.wireformats.MessagingNodeInfo;
import csx55.overlay.wireformats.MessagingNodesList;

public class OverlayCreator {
    MessagingNodeInfo[] messagingNodes;
    ArrayList<LinkInfo> links;
    int[] connectionCounts;
    ArrayList<ArrayList<Integer>> connections;
    int perNodeConnectionLimit;

    OverlayCreator(MessagingNodeInfo[] nodes, int connectionLimit) {
        messagingNodes = nodes;
        connectionCounts = new int[nodes.length];
        connections = new ArrayList<>(nodes.length);
        perNodeConnectionLimit = connectionLimit;
    }

    public void createOverlay() throws IOException {
        connectNodes();
        publishConnections();
    }

    private void connectNodes() {
        int jump = 1;

        while (jump < messagingNodes.length) {
            for (int node = 0; node < messagingNodes.length; node++) {
                int nextNode = node + jump;
                if (isAvailableForConnection(node) && isAvailableForConnection(nextNode)) {
                    connect(node, nextNode);
                } else {
                    break;
                }
            }
            jump++;
        }
    }

    private boolean isAvailableForConnection(int socketIndex) {
        if (socketIndex > connectionCounts.length) {
            return false;
        }
        if (connectionCounts[socketIndex] >= perNodeConnectionLimit) {
            return false;
        }
        return true;
    }

    private void connect(int socketIndex1, int socketIndex2) {
        connectionCounts[socketIndex1]++;
        connectionCounts[socketIndex2]++;
        connections.get(socketIndex1).add(socketIndex2);
        addLinkInfo(socketIndex1, socketIndex2);
    }

    private void addLinkInfo(int socketIndex1, int socketIndex2) {
        MessagingNodeInfo node1 = messagingNodes[socketIndex1];
        MessagingNodeInfo node2 = messagingNodes[socketIndex2];
        int weight = new Random().nextInt(10 - 1 + 1) + 1;
        LinkInfo link = new LinkInfo(node1.getHostname(), node1.getPort(), node2.getHostname(), node2.getPort(),
                weight);
        links.add(link);
    }

    private void publishConnections() throws IOException {
        for (int i = 0; i < connections.size(); i++) {
            ArrayList<Integer> toConnect = connections.get(i);

            MessagingNodeInfo[] nodeInfos = new MessagingNodeInfo[toConnect.size()];
            for (int connection = 0; connection < toConnect.size(); connection++) {
                int nodeIndex = toConnect.get(connection);
                nodeInfos[i] = messagingNodes[nodeIndex];
            }

            MessagingNodesList message = new MessagingNodesList(nodeInfos);

            new TCPSender(messagingNodes[i].getSocket()).send(message);
        }
    }
}
