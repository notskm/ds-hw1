package csx55.overlay.node;

import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;

import csx55.overlay.transport.TCPSender;
import csx55.overlay.util.OverlayCreator;
import csx55.overlay.wireformats.Deregister;
import csx55.overlay.wireformats.DeregisterResponse;
import csx55.overlay.wireformats.LinkInfo;
import csx55.overlay.wireformats.MessagingNodeInfo;
import csx55.overlay.wireformats.Register;
import csx55.overlay.wireformats.RegisterResponse;
import csx55.overlay.wireformats.RegisterResponse.Status;

public class Registry extends Node {
    private HashMap<MessagingNodeInfo, Socket> messagingNodes;
    private LinkInfo[] links;
    private static int serverPort = 5000;

    public static void main(String[] args) {
        try {
            parseArgs(args);
            new Registry(args).run(serverPort);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    Registry(String[] args) {
        super();
        parseArgs(args);
        messagingNodes = new HashMap<>();
        links = new LinkInfo[0];
    }

    @Override
    final protected void onRegisterRequest(Register register) {
        MessagingNodeInfo node = new MessagingNodeInfo(register.getIpAddress(), register.getPortNumber());

        RegisterResponse response = null;

        String socketIP = register.getOriginIp();
        String requestIp = register.getIpAddress();
        Socket originSocket = register.getOriginSocket();

        if (!socketIP.equals(requestIp)) {
            response = getRegistrationIpMismatchResponse(register, socketIP);
        } else if (messagingNodes.containsKey(node)) {
            response = getRegistrationNodeAlreadyExistsResponse(register);
        } else {
            registerNode(node, originSocket);
            response = getRegistrationSuccessfulMessage(register);
        }

        try {
            new TCPSender(originSocket).send(response);
        } catch (IOException e) {
            deregisterNode(node);
        }
    }

    private void registerNode(MessagingNodeInfo key, Socket socket) {
        messagingNodes.put(key, socket);
    }

    private void deregisterNode(MessagingNodeInfo key) {
        messagingNodes.remove(key);
    }

    private RegisterResponse getRegistrationSuccessfulMessage(Register register) {
        String message = "Registration request successful. ";
        message += "The number of messaging nodes currently constituting the overlay is ";
        message += "(" + getRegisteredCount() + ")";
        return new RegisterResponse(Status.FAILURE, message);
    }

    private RegisterResponse getRegistrationIpMismatchResponse(Register event, String originIp) {
        String message = "Registration request unsuccessful. The provided IP address ";
        message += event.getIpAddress();
        message += " does not match the node's ip address ";
        message += originIp;

        return new RegisterResponse(Status.FAILURE, message);
    }

    private RegisterResponse getRegistrationNodeAlreadyExistsResponse(Register event) {
        String message = "Registration request unsuccessful. The node '";
        message += event.getIpAddress() + ":" + event.getPortNumber();
        message += "' already exists within the registry.";

        return new RegisterResponse(Status.FAILURE, message);
    }

    private int getRegisteredCount() {
        return messagingNodes.size();
    }

    private static void parseArgs(String[] args) {
        if (args.length >= 1) {
            try {
                serverPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Expected a port number, got " + args[0]);
            }
        }
    }

    @Override
    protected final void listMessagingNodes() {
        for (MessagingNodeInfo nodes : messagingNodes.keySet()) {
            String message = "";
            message += nodes.getHostname();
            message += ":";
            message += nodes.getPort();
            System.out.println(message);
        }
    }

    @Override
    protected final void listWeights() {
        for (LinkInfo link : links) {
            String linkString = link.getHostnameA();
            linkString += ":";
            linkString += link.getPortA();
            linkString += " ";
            linkString += link.getHostnameB();
            linkString += ":";
            linkString += link.getPortB();
            linkString += " ";
            linkString += link.getWeight();
            System.out.println(linkString);
        }
    }

    @Override
    protected final void setupOverlay(int connectionLimit) {
        int nodeCount = messagingNodes.size();
        if (nodeCount < connectionLimit) {
            String errorMessage = "Connection limit (%d) is greater than the number of registered nodes (%d)%n";
            System.out.printf(errorMessage, connectionLimit, nodeCount);
            return;
        }

        try {
            OverlayCreator creator = new OverlayCreator(messagingNodes, connectionLimit);
            links = creator.createOverlay();
        } catch (IOException e) {
        }
    }

    @Override
    protected final void onDeregisterRequest(Deregister event) {
        MessagingNodeInfo node = new MessagingNodeInfo(event.getIpAddress(), event.getPort());

        DeregisterResponse.Status status = null;
        String message = null;

        String socketIP = event.getOriginIp();
        String requestIp = event.getIpAddress();
        Socket originSocket = event.getOriginSocket();

        if (!socketIP.equals(requestIp)) {
            status = DeregisterResponse.Status.FAILURE;
            message = "Deregistration request unsuccessful. The provided IP address ";
            message += event.getIpAddress();
            message += " does not match the node's ip address ";
            message += socketIP;
        } else if (!messagingNodes.containsKey(node)) {
            status = DeregisterResponse.Status.FAILURE;
            message = "not in registry";
            message = "Deregistration request unsuccessful. The node '";
            message += node.getHostname() + ":" + node.getPort();
            message += " does not exist in the registry";
        } else {
            deregisterNode(node);
            status = DeregisterResponse.Status.SUCCESS;
            message = "Deregistration successful.";
        }

        DeregisterResponse response = new DeregisterResponse(status, message);

        try {
            new TCPSender(originSocket).send(response);
        } catch (IOException e) {
            deregisterNode(node);
        }
    }
}
