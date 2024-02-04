package csx55.overlay.util;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import csx55.overlay.transport.TCPSender;
import csx55.overlay.wireformats.LinkInfo;
import csx55.overlay.wireformats.MessagingNodeInfo;
import csx55.overlay.wireformats.MessagingNodesList;

public class OverlayCreator {
    final ArrayList<MessagingNodeInfo> messagingNodes;
    final int perNodeConnectionLimit;

    final Map<MessagingNodeInfo, Socket> socketTable;

    int[] connectionCounts;
    ArrayList<ArrayList<Integer>> connections;
    ArrayList<LinkInfo> links;

    public OverlayCreator(Map<MessagingNodeInfo, Socket> nodes, int connectionLimit) {
        socketTable = nodes;
        perNodeConnectionLimit = connectionLimit;

        messagingNodes = new ArrayList<>();
        for (MessagingNodeInfo nodeInfo : nodes.keySet()) {
            messagingNodes.add(nodeInfo);
        }

        connectionCounts = new int[nodes.size()];
        connections = new ArrayList<>(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            connections.add(new ArrayList<>());
        }
        links = new ArrayList<>();
    }

    public LinkInfo[] createOverlay() throws IOException {
        connectNodes();
        publishConnections();
        return (LinkInfo[]) links.toArray();
    }

    private void connectNodes() {
        int jump = 1;

        while (jump < messagingNodes.size()) {
            for (int node = 0; node < messagingNodes.size(); node++) {
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
        if (socketIndex >= connectionCounts.length) {
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
        MessagingNodeInfo node1 = messagingNodes.get(socketIndex1);
        MessagingNodeInfo node2 = messagingNodes.get(socketIndex2);
        int weight = new Random().nextInt(10 - 1 + 1) + 1;
        LinkInfo link = new LinkInfo(node1.getHostname(), node1.getPort(), node2.getHostname(), node2.getPort(),
                weight);
        links.add(link);
    }

    private void publishConnections() throws IOException {
        for (int i = 0; i < connections.size(); i++) {
            ArrayList<Integer> toConnect = connections.get(i);

            MessagingNodeInfo[] nodeInfos = new MessagingNodeInfo[toConnect.size()];
            for (int j = 0; j < toConnect.size(); j++) {
                int nodeIndex = toConnect.get(j);
                nodeInfos[j] = messagingNodes.get(nodeIndex);
            }

            MessagingNodesList message = new MessagingNodesList(nodeInfos);

            final Socket socket = socketTable.get(messagingNodes.get(i));
            new TCPSender(socket).send(message);
        }
    }
}
