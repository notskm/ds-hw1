package csx55.overlay.node;

import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.io.IOException;

import csx55.overlay.transport.TCPReceiverThread;
import csx55.overlay.transport.TCPServerThread;
import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.Protocol;
import csx55.overlay.wireformats.Register;

public class Registry {
    private static ArrayList<TCPReceiverThread> receiverThreads;

    public static void main(String[] args) {
        parseArgs(args);
        
        receiverThreads = new ArrayList<>();

        try {
            TCPServerThread serverThread = createServerThread(serverPort);
            serverThread.start();

            while (true) {
                Socket nodeSocket = serverThread.poll();

                if (nodeSocket != null) {
                    TCPReceiverThread thread = new TCPReceiverThread(nodeSocket);
                    receiverThreads.add(thread);
                    thread.start();
                }
                
                for(TCPReceiverThread thread : receiverThreads) {
                    Event event = thread.poll();
                    if(event != null) {
                        onEvent(event);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static TCPServerThread createServerThread(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        TCPServerThread thread = new TCPServerThread(socket);
        return thread;
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
