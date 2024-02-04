package csx55.overlay.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import csx55.overlay.transport.TCPReceiverThread;
import csx55.overlay.transport.TCPServerThread;
import csx55.overlay.util.InputReceiverThread;
import csx55.overlay.wireformats.*;

public class Node {
    private int actualServerPort;
    protected TCPServerThread serverThread;
    protected ArrayList<TCPReceiverThread> receiverThreads;
    private InputReceiverThread inputThread;

    Node() {
        receiverThreads = new ArrayList<>();
        inputThread = new InputReceiverThread();
    }

    public void run(int serverPort) throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverPort);
        serverThread = new TCPServerThread(serverSocket);
        serverThread.start();

        inputThread.start();

        actualServerPort = serverSocket.getLocalPort();

        initialize();

        while (true) {
            pollForInput();
            pollForSockets();
            pollForEvents();
        }
    }

    protected void initialize() {
    }

    protected int getActualServerPort() {
        return actualServerPort;
    }

    private void pollForInput() {
        String input = inputThread.poll();
        if (input == null) {
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
                System.out.println("Invalid command: " + input);
            }
        } catch (NumberFormatException | UnsupportedOperationException e) {
            System.out.println("Invalid command: " + input);
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
            TCPReceiverThread thread = new TCPReceiverThread(socket);
            receiverThreads.add(thread);
            thread.start();
        }
    }

    private void pollForEvents() {
        for (TCPReceiverThread thread : receiverThreads) {
            Event event = thread.poll();
            if (event != null) {
                onEvent(event);
            }
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
}
