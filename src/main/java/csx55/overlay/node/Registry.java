package csx55.overlay.node;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.DataInputStream;

import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.EventFactory;
import csx55.overlay.wireformats.Protocol;
import csx55.overlay.wireformats.Register;

public class Registry {
    public static void main(String[] args) {
        parseArgs(args);

        ServerSocket serverSocket = openServerSocket();
        Socket nodeSocket = listenForConnections(serverSocket);

        try {
            Event event = readData(nodeSocket);
            onEvent(event);
        } catch (IOException e) {
            System.err.println("Unable to read from socket");
            System.err.println(e.getMessage());
        }
    }

    private static Event readData(Socket nodeSocket) throws IOException {
        DataInputStream in = new DataInputStream(nodeSocket.getInputStream());
        byte[] data = new byte[in.readInt()];
        in.readFully(data);
        in.close();

        return EventFactory.getInstance().getEvent(data);
    }

    private static void onEvent(Event event) {
        switch (Protocol.values()[event.getType()]) {
            case REGISTER_REQUEST:
                onRegisterRequest((Register) event);
                break;
            default:
                break;
        }
    }

    private static void onRegisterRequest(Register register) {
        System.out.println(register.getIpAddress());
        System.out.println(register.getPortNumber());
    }

    public static ServerSocket openServerSocket() {
        try {
            return new ServerSocket(serverPort);
        } catch (IOException e) {
            System.err.println("Unable to start registry on port " + serverPort);
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static Socket listenForConnections(ServerSocket socket) {
        while (true) {
            try {
                Socket connection = socket.accept();
                return connection;
            } catch (IOException e) {
                System.err.println("Unable to accept connection");
                System.err.println(e.getMessage());
            }
        }
    }

    private static void parseArgs(String[] args) {
        if (args.length >= 1) {
            try {
                serverPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    static private int serverPort = 5000;
}
