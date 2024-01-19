package csx55.overlay.node;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class Registry {
    public static void main(String[] args) {
        ServerSocket serverSocket = openServerSocket();
        Socket nodeSocket = listenForConnections(serverSocket);
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
    
    static private int serverPort = 5000;
}
