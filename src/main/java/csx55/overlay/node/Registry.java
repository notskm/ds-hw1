package csx55.overlay.node;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.DataInputStream;
import csx55.overlay.wireformats.Register;

public class Registry {
    public static void main(String[] args) {
        parseArgs(args);

        ServerSocket serverSocket = openServerSocket();
        Socket nodeSocket = listenForConnections(serverSocket);

        try {
            DataInputStream in = new DataInputStream(nodeSocket.getInputStream());

            byte[] data = new byte[in.readInt()];
            in.readFully(data);
            Register register = new Register(data);

            System.out.println(register.getIpAddress());
            System.out.println(register.getPortNumber());
            in.close();
        }
        catch(IOException e) {
            System.err.println("Unable to read from socket");
            System.err.println(e.getMessage());
        }
    }
    
    public static ServerSocket openServerSocket() {
        try {
            return new ServerSocket(serverPort);
        }
        catch(IOException e) {
            System.err.println("Unable to start registry on port " + serverPort);
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
    
    public static Socket listenForConnections(ServerSocket socket) {
        while(true) {
            try {
                Socket connection = socket.accept();
                return connection;
            }
            catch(IOException e) {
                System.err.println("Unable to accept connection");
                System.err.println(e.getMessage());
            }
        }
    }
    
    private static void parseArgs(String[] args) {
        if(args.length >= 1) {
            try {
                serverPort = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }
    
    static private int serverPort = 5000;
}
