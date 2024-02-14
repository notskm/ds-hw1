package csx55.overlay.node;

import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Map;
import java.io.IOException;

import csx55.overlay.dijkstra.Graph;
import csx55.overlay.dijkstra.ShortestPath;
import csx55.overlay.transport.TCPSender;
import csx55.overlay.wireformats.*;

public class MessagingNode extends Node {
    static private String registryHost = "127.0.0.1";
    static private int registryPort = 5000;
    Socket registrySocket;
    Map<MessagingNodeInfo, MessagingNodeInfo> shortestPaths = new HashMap<>();
    Graph overlayGraph = new Graph(new LinkInfo[0]);

    private int sent = 0;
    private int received = 0;
    private int relayed = 0;
    private long sumSent = 0;
    private long sumReceived = 0;

    public static void main(String[] args) {
        parseArgs(args);
        new MessagingNode().run(0);
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

    public MessagingNode() {
        super();
        messagingNodes = new HashMap<>();
    }

    @Override
    protected void onMessagingNodesList(MessagingNodesList nodes) {
        disconnectFromAllMessagingNodes();
        clearCachedRoutes();

        try {
            int numConnections = 0;
            for (MessagingNodeInfo node : nodes.getNodeInfo()) {
                Socket socket = new Socket(node.getHostname(), node.getPort());

                startReceiverThread(socket);

                messagingNodes.put(node, socket);
                sendRegisterRequest(socket);

                numConnections++;
            }
            System.out.printf("All connections are established. Number of connections: %d%n", numConnections);
        } catch (IOException e) {
        }
    }

    private void disconnectFromAllMessagingNodes() {
        for (Socket socket : messagingNodes.values()) {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
        messagingNodes.clear();
    }

    private void clearCachedRoutes() {
        overlayGraph = new Graph(new LinkInfo[0]);
        shortestPaths.clear();
    }

    @Override
    protected void onRegisterRequest(Register register) {
        MessagingNodeInfo node = new MessagingNodeInfo(register.getIpAddress(), register.getPortNumber());
        registerNode(node, register.getOriginSocket());
    }

    private void registerNode(MessagingNodeInfo key, Socket socket) {
        messagingNodes.put(key, socket);
    }

    @Override
    protected final void initialize() {
        try {
            registrySocket = new Socket(registryHost, registryPort);
            startReceiverThread(registrySocket);

            sendRegisterRequest(registrySocket);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open registry socket");
        }
    }

    private void sendRegisterRequest(Socket socket) throws IOException {
        int port = getActualServerPort();
        String ipAddress = getServerHostname();

        new TCPSender(socket).send(new Register(ipAddress, port));
    }

    @Override
    protected final void exitOverlay() {
        try {
            Deregister deregister = new Deregister(getServerHostname(), getActualServerPort());
            new TCPSender(registrySocket).send(deregister);
        } catch (IOException e) {
            shutdown();
        }
    }

    @Override
    protected final void onLinkWeights(LinkWeights weights) {
        constructMessagingNodeGraph(weights.getLinks());
        System.out.println("Link weights received and processed. Ready to send messages.");
    }

    private void constructMessagingNodeGraph(LinkInfo[] links) {
        ShortestPath pathComputer = new ShortestPath(links);
        overlayGraph = new Graph(links);
        MessagingNodeInfo thisNode = new MessagingNodeInfo(getServerHostname(), getActualServerPort());
        Map<MessagingNodeInfo, MessagingNodeInfo> paths = pathComputer.computeShortestPaths(thisNode);
        shortestPaths = paths;
    }

    @Override
    protected final void printShortestPath() {
        for (MessagingNodeInfo node : shortestPaths.keySet()) {
            printPathToNode(node);
        }
    }

    private void printPathToNode(MessagingNodeInfo destination) {
        String output = shortHostName(destination.getHostname());

        MessagingNodeInfo predecessor = shortestPaths.get(destination);

        while (predecessor != null) {
            int weight = overlayGraph.edge(predecessor, destination).weight;
            String predecessorName = shortHostName(predecessor.getHostname());
            output = predecessorName + "--" + weight + "--" + output;
            destination = predecessor;
            predecessor = shortestPaths.get(predecessor);
        }

        System.out.println(output);
    }

    private String shortHostName(String host) {
        String[] parts = host.split("\\.");
        if (parts.length == 0) {
            return host;
        }

        if (parts[0].isEmpty()) {
            return host;
        }

        return parts[0];
    }

    @Override
    protected final void onDeregisterResponse(DeregisterResponse response) {
        System.out.println(response.getInfo());
        shutdown();
    }

    @Override
    protected void onTaskInitiate(TaskInitiate event) {
        executeNRounds(event.getRounds());
        tellRegistryAllRoundsAreComplete();
    }

    private void executeNRounds(int n) {
        for (int round = 0; round < n; round++) {
            executeRound();
        }
    }

    private void tellRegistryAllRoundsAreComplete() {
        TaskComplete response = new TaskComplete(getServerHostname(), getActualServerPort());
        sendEvent(response, registrySocket);
    }

    private void executeRound() {
        MessagingNodeInfo destination = getRandomNode();
        Socket destinationSocket = messagingNodes.get(destination);
        MessagingNodeInfo[] path = getPath(destination);

        for (int i = 0; i < 5; i++) {
            Message message = new Message(path);

            sent++;
            sumSent += message.getNumber();

            sendMessage(message, destinationSocket);
        }
    }

    private MessagingNodeInfo getRandomNode() {
        List<MessagingNodeInfo> nodes = new ArrayList<>(messagingNodes.keySet());
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(nodes.size());
        MessagingNodeInfo destination = nodes.get(randomIndex);
        return destination;
    }

    private MessagingNodeInfo[] getPath(MessagingNodeInfo destination) {
        ArrayList<MessagingNodeInfo> output = new ArrayList<>();
        output.add(destination);

        MessagingNodeInfo predecessor = shortestPaths.get(destination);

        while (predecessor != null) {
            output.add(0, predecessor);
            predecessor = shortestPaths.get(predecessor);
        }

        MessagingNodeInfo[] path = new MessagingNodeInfo[output.size()];
        return output.toArray(path);
    }

    protected void sendMessage(Message message, Socket socket) {
        sendEvent(message, socket);
    }

    @Override
    protected void onMessage(Message event) {
        MessagingNodeInfo nextNode = event.nextNode();

        if (nextNode == null) {
            received++;
            sumReceived += event.getNumber();
        } else {
            relayed++;
            sendEvent(event, messagingNodes.get(nextNode));
        }
    }

    @Override
    protected void onTaskSummaryRequest(TaskSummaryRequest event) {
        String ip = getServerHostname();
        int port = getActualServerPort();
        TaskSummaryResponse response = new TaskSummaryResponse(ip, port, sent, sumSent, received, sumReceived, relayed);

        sendEvent(response, event.getOriginSocket());

        sent = 0;
        received = 0;
        relayed = 0;
        sumSent = 0;
        sumReceived = 0;
    }

    private void sendEvent(Event event, Socket socket) {
        try {
            new TCPSender(socket).send(event);
        } catch (IOException e) {
        }
    }

}
