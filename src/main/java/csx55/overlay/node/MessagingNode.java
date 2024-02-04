package csx55.overlay.node;

import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;

import csx55.overlay.transport.TCPReceiverThread;
import csx55.overlay.transport.TCPSender;
import csx55.overlay.wireformats.*;

public class MessagingNode extends Node {
    static private String registryHost = "127.0.0.1";
    static private int registryPort = 5000;
    Socket registrySocket;

    public static void main(String[] args) {
        try {
            parseArgs(args);
            new MessagingNode().run(0);
        } catch (Exception e) {
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

    @Override
    protected void onRegisterResponse(RegisterResponse response) {
        System.out.println(response.getInfo());
    }

    @Override
    protected void onMessagingNodesList(MessagingNodesList nodes) {
        for (MessagingNodeInfo node : nodes.getNodeInfo()) {
            System.out.println(node.getHostname());
            System.out.println(node.getPort());
        }
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

    }

    @Override
    protected final void onDeregisterResponse(DeregisterResponse response) {
        System.out.println(response.getInfo());
        System.exit(0);
    }
}
