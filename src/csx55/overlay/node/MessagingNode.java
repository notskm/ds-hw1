package csx55.overlay.node;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

public class MessagingNode {
    public static void main(String[] args) {
        try {
            Socket registrySocket = new Socket(registryHost, registryPort);
            registrySocket.close();
        }
        catch(UnknownHostException e) {
            System.err.println("Unknown host: " + registryHost);
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch(IOException e) {
            System.err.println("Unable to reach registry at " + registryHost + ":" + registryPort);
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    
    static private String registryHost = "127.0.0.1";
    static private int registryPort = 5000;
}
