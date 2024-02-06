package csx55.overlay.node;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import csx55.overlay.dijkstra.Graph;
import csx55.overlay.dijkstra.ShortestPath;
import csx55.overlay.transport.TCPReceiverThread;
import csx55.overlay.transport.TCPSender;
import csx55.overlay.wireformats.*;

public class MessagingNode extends Node {
    static private String registryHost = "127.0.0.1";
    static private int registryPort = 5000;
    Socket registrySocket;
    Map<MessagingNodeInfo, Socket> messagingNodes;
    Map<MessagingNodeInfo, MessagingNodeInfo> shortestPaths = new HashMap<>();
    Graph overlayGraph = new Graph(new LinkInfo[0]);

    public static void main(String[] args) {
        try {
            parseArgs(args);
            new MessagingNode().run(0);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void parseArgs(String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args.length >= 1) {
            registryHost = args[0];
        }

        if (args.length >= 2) {
            try {
                registryPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[1]);
                System.exit(1);
            }
        }
    }

    private MessagingNode() {
        messagingNodes = new HashMap<>();
    }

    @Override
    protected void onRegisterResponse(RegisterResponse response) {
        System.out.println(response.getInfo());
    }

    @Override
    protected void onMessagingNodesList(MessagingNodesList nodes) {
        try {
            int numConnections = 0;
            for (MessagingNodeInfo node : nodes.getNodeInfo()) {
                Socket socket = new Socket(node.getHostname(), node.getPort());
                Register register = new Register(getServerHostname(), getActualServerPort());
                new TCPSender(socket).send(register);

                receiverThreads.add(new TCPReceiverThread(socket));
                messagingNodes.put(node, socket);
                numConnections++;
            }
            System.out.printf("All connections are established. Number of connections: %d%n", numConnections);
        } catch (IOException e) {

        }
    }

    @Override
    protected void onRegisterRequest(Register register) {
        MessagingNodeInfo node = new MessagingNodeInfo(register.getIpAddress(), register.getPortNumber());
        messagingNodes.put(node, register.getOriginSocket());
    }

    @Override
    protected final void initialize() {
        try {
            registrySocket = new Socket(registryHost, registryPort);
            TCPReceiverThread registryReceiver = new TCPReceiverThread(registrySocket);
            registryReceiver.start();
            receiverThreads.add(registryReceiver);

            sendRegisterRequest(registrySocket);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open registry socket");
        }
    }

    private void sendRegisterRequest(Socket socket) throws IOException {
        int port = getActualServerPort();
        String ipAddress = InetAddress.getLocalHost().getHostAddress();

        new TCPSender(socket).send(new Register(ipAddress, port));
    }

    @Override
    protected final void exitOverlay() {
        try {
            Deregister deregister = new Deregister(getServerHostname(), getActualServerPort());
            new TCPSender(registrySocket).send(deregister);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    @Override
    protected final void onLinkWeights(LinkWeights weights) {
        constructMessagingNodeGraph(weights.getLinks());
        System.out.println("Link weights received and processed. Ready to send messages.");
    }

    private void constructMessagingNodeGraph(LinkInfo[] links) {
        try {
            ShortestPath pathComputer = new ShortestPath(links);
            overlayGraph = new Graph(links);
            MessagingNodeInfo thisNode = new MessagingNodeInfo(getServerHostname(), getActualServerPort());
            Map<MessagingNodeInfo, MessagingNodeInfo> paths = pathComputer.computeShortestPaths(thisNode);
            shortestPaths = paths;
        } catch (UnknownHostException e) {
            System.out.println("Error");
        }
    }

    @Override
    protected final void printShortestPath() {
        for (MessagingNodeInfo node : shortestPaths.keySet()) {
            printPathToNode(node);
        }
    }

    private void printPathToNode(MessagingNodeInfo destination) {
        String output = destination.toString();

        MessagingNodeInfo predecessor = shortestPaths.get(destination);

        while (predecessor != null) {
            int weight = overlayGraph.edge(predecessor, destination).weight;
            output = predecessor + "--" + weight + "--" + output;
            destination = predecessor;
            predecessor = shortestPaths.get(predecessor);
        }

        System.out.println(output);
    }

    @Override
    protected final void onDeregisterResponse(DeregisterResponse response) {
        System.out.println(response.getInfo());
        System.exit(0);
    }
}
