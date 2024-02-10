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
        validateArgumentsOrThrow(nodes, connectionLimit);

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

    private void validateArgumentsOrThrow(Map<MessagingNodeInfo, Socket> nodes, int connectionLimit) {
        if (nodes.size() < connectionLimit) {
            String message = "Cannot create overlay. The number of connections ";
            message += "(" + connectionLimit + ") ";
            message += "is greater than the number of nodes ";
            message += "(" + nodes.size() + ")";
            throw new IllegalArgumentException(message);
        }
    }

    public LinkInfo[] createOverlay() throws IOException {
        connectNodes();
        publishConnections();
        return links.toArray(new LinkInfo[links.size()]);
    }

    private void connectNodes() {
        final int connectionCycles = calculateConnectionCycles();
        final boolean connectOpposites = shouldConnectOpposites();

        for (int i = 1; i <= connectionCycles; i++) {
            connectNodesByJumpingAhead(i);
        }

        if (connectOpposites) {
            connectOpposites();
        }
    }

    private int calculateConnectionCycles() {
        // If we have 1 vertex, it should not connect to anything.
        if (messagingNodes.size() <= 1) {
            return 0;
        }

        // We should use connectOpposites() if we only have 2 nodes to ensure
        // that they do not connect to each other twice.
        if (messagingNodes.size() == 2) {
            return 0;
        }

        return perNodeConnectionLimit / 2;
    }

    private boolean shouldConnectOpposites() {
        // If we have 1 vertex, it should not connect to anything.
        if (messagingNodes.size() <= 1) {
            return false;
        }

        // If we have 2 vertices and the connection limit isn't 0, then they
        // should be allowed to make one connection with each other.
        if (messagingNodes.size() == 2 && perNodeConnectionLimit > 0) {
            return true;
        }

        // It is impossible to have an odd number of connections with an odd
        // number of vertices, so we should not connect opposite vertices.
        if (messagingNodes.size() % 2 == 1) {
            return false;
        }

        return perNodeConnectionLimit % 2 != 0;
    }

    private void connectNodesByJumpingAhead(int jump) {
        final int numberOfNodes = messagingNodes.size();

        for (int i = 0; i < numberOfNodes; i++) {
            final int toConnect = (i + jump) % numberOfNodes;
            connect(i, toConnect);
        }
    }

    private void connectOpposites() {
        final int numberOfNodes = messagingNodes.size() / 2;

        for (int i = 0; i < numberOfNodes; i++) {
            final int toConnect = i + numberOfNodes;
            connect(i, toConnect);
        }
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
        LinkInfo link1 = new LinkInfo(node1.getHostname(), node1.getPort(), node2.getHostname(), node2.getPort(),
                weight);
        LinkInfo link2 = new LinkInfo(node2.getHostname(), node2.getPort(), node1.getHostname(), node1.getPort(),
                weight);
        links.add(link1);
        links.add(link2);
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
