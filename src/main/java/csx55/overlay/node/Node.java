package csx55.overlay.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import csx55.overlay.transport.TCPReceiverThread;
import csx55.overlay.transport.TCPServerThread;
import csx55.overlay.wireformats.*;

public class Node {
    private int actualServerPort;
    protected TCPServerThread serverThread;
    protected ArrayList<TCPReceiverThread> receiverThreads;

    Node() {
        receiverThreads = new ArrayList<>();
    }

    public void run(int serverPort) throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverPort);
        serverThread = new TCPServerThread(serverSocket);
        serverThread.start();

        actualServerPort = serverSocket.getLocalPort();

        initialize();

        while (true) {
            pollForSockets();
            pollForEvents();
        }
    }

    protected void initialize() {
    }

    protected int getActualServerPort() {
        return actualServerPort;
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
