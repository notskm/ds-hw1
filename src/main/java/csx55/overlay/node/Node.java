package csx55.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import csx55.overlay.transport.TCPReceiverThread;
import csx55.overlay.transport.TCPServerThread;
import csx55.overlay.util.InputReceiverThread;
import csx55.overlay.wireformats.*;

public class Node {
    private int actualServerPort;
    private ServerSocket serverSocket;
    private TCPServerThread serverThread;
    private InputReceiverThread inputThread;
    private BlockingQueue<Event> eventQueue;
    private String hostname;
    private List<Socket> knownSockets;

    protected Map<MessagingNodeInfo, Socket> messagingNodes;

    public Node() {
        try {
            messagingNodes = new HashMap<>();
            inputThread = new InputReceiverThread();
            hostname = InetAddress.getLocalHost().getCanonicalHostName();
            eventQueue = new LinkedBlockingQueue<>();
            knownSockets = new LinkedList<>();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void run(int serverPort) {
        try {
            serverSocket = new ServerSocket(serverPort);
            serverThread = new TCPServerThread(serverSocket);
            serverThread.start();

            inputThread.start();

            actualServerPort = serverSocket.getLocalPort();

            initialize();

            while (!serverSocket.isClosed()) {
                pollForInput();
                pollForSockets();
                pollForEvents();
                removeDeadSockets();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            shutdown();
        }
    }

    private void removeDeadSockets() {
        knownSockets.removeIf((Socket socket) -> socket.isClosed());
    }

    public void shutdown() {
        inputThread.shutdown();

        try {
            serverSocket.close();
        } catch (IOException e) {
        }

        for (Socket socket : knownSockets) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }

        for (Socket socket : messagingNodes.values()) {
            try {
                socket.close();
            } catch (IOException e) {
            }

        }
    }

    protected void initialize() {
    }

    protected String getServerHostname() {
        return hostname;
    }

    protected int getActualServerPort() {
        return actualServerPort;
    }

    private void pollForInput() {
        String input = inputThread.poll();
        if (input == null || input.isEmpty()) {
            return;
        }

        String[] args = input.split("\\s+");

        String commandName = args[0];
        try {
            if (commandName.equals("list-messaging-nodes")) {
                listMessagingNodes();
            } else if (commandName.equals("list-weights")) {
                listWeights();
            } else if (commandName.equals("setup-overlay") && args.length > 1) {
                final int connections = Integer.parseInt(args[1]);
                setupOverlay(connections);
            } else if (commandName.equals("send-overlay-link-weights")) {
                sendOverlayLinkWeights();
            } else if (commandName.equals("start") && args.length > 1) {
                final int rounds = Integer.parseInt(args[1]);
                start(rounds);
            } else if (commandName.equals("print-shortest-path")) {
                printShortestPath();
            } else if (commandName.equals("exit-overlay")) {
                exitOverlay();
            } else {
            }
        } catch (NumberFormatException | UnsupportedOperationException e) {
        }
    }

    protected void listMessagingNodes() {
        throw new UnsupportedOperationException("Unimplemented method 'listMessagingNodes'");
    }

    protected void listWeights() {
        throw new UnsupportedOperationException("Unimplemented method 'listMessagingNodes'");
    }

    protected void setupOverlay(int connections) {
        throw new UnsupportedOperationException("Unimplemented method 'listMessagingNodes'");
    }

    protected void sendOverlayLinkWeights() {
        throw new UnsupportedOperationException("Unimplemented method 'listMessagingNodes'");
    }

    protected void start(int rounds) {
        throw new UnsupportedOperationException("Unimplemented method 'listMessagingNodes'");
    }

    protected void printShortestPath() {
        throw new UnsupportedOperationException("Unimplemented method 'listMessagingNodes'");
    }

    protected void exitOverlay() {
        throw new UnsupportedOperationException("Unimplemented method 'listMessagingNodes'");
    }

    private void pollForSockets() {
        Socket socket = serverThread.poll();
        if (socket != null) {
            try {
                startReceiverThread(socket);
            } catch (IOException e) {

            }

        }
    }

    private void cacheSocket(Socket socket) {
        knownSockets.add(socket);
    }

    protected void startReceiverThread(Socket socket) throws IOException {
        TCPReceiverThread thread = new TCPReceiverThread(socket, eventQueue);
        thread.start();
        cacheSocket(socket);
    }

    private void pollForEvents() {
        if (!eventQueue.isEmpty()) {
            onEvent(eventQueue.remove());
        }
    }

    public void onEvent(Event event) {
        Protocol eventType = Protocol.values()[event.getType()];

        switch (eventType) {
            case REGISTER_REQUEST:
                onRegisterRequest((Register) event);
                break;
            case REGISTER_RESPONSE:
                onRegisterResponse((RegisterResponse) event);
                break;
            case DEREGISTER_REQUEST:
                onDeregisterRequest((Deregister) event);
                break;
            case DEREGISTER_RESPONSE:
                onDeregisterResponse((DeregisterResponse) event);
                break;
            case MESSAGING_NODES_LIST:
                onMessagingNodesList((MessagingNodesList) event);
                break;
            case LINK_WEIGHTS:
                onLinkWeights((LinkWeights) event);
                break;
            case TASK_INITIATE:
                onTaskInitiate((TaskInitiate) event);
                break;
            case TASK_COMPLETE:
                onTaskComplete((TaskComplete) event);
                break;
            case FULL_TRAFFIC_SUMMARY:
                onTaskSummaryRequest((TaskSummaryRequest) event);
                break;
            case TRAFFIC_SUMMARY:
                onTaskSummaryResponse((TaskSummaryResponse) event);
                break;
            case MESSAGE:
                onMessage((Message) event);
                break;
            default:
                break;
        }
    }

    protected void onRegisterRequest(Register event) {
    }

    protected void onRegisterResponse(RegisterResponse event) {
    }

    protected void onDeregisterRequest(Deregister event) {

    }

    protected void onDeregisterResponse(DeregisterResponse event) {

    }

    protected void onMessagingNodesList(MessagingNodesList event) {

    }

    protected void onLinkWeights(LinkWeights event) {

    }

    protected void onTaskInitiate(TaskInitiate event) {

    }

    protected void onTaskComplete(TaskComplete event) {

    }

    protected void onTaskSummaryRequest(TaskSummaryRequest event) {

    }

    protected void onTaskSummaryResponse(TaskSummaryResponse event) {

    }

    protected void onMessage(Message event) {

    }
}
