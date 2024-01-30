package csx55.overlay.node;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.DataInputStream;

import csx55.overlay.transport.TCPSender;
import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.EventFactory;
import csx55.overlay.wireformats.Register;
import csx55.overlay.wireformats.RegisterResponse;

public class MessagingNode {
    public static void main(String[] args) {
        parseArgs(args);

        try (Socket registrySocket = new Socket(registryHost, registryPort)) {
            sendRegisterRequest(registrySocket);
            DataInputStream dis = new DataInputStream(registrySocket.getInputStream());
            int dataLength = dis.readInt();
            byte[] data = new byte[dataLength];
            dis.readFully(data);
            Event event = EventFactory.getInstance().getEvent(data);
            System.out.println(((RegisterResponse)event).getInfo());
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + registryHost);
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to reach registry at " + registryHost + ":" + registryPort);
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void sendRegisterRequest(Socket socket) throws IOException {
        new TCPSender(socket).send(new Register("127.0.0.1", 5001));
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

    static private String registryHost = "127.0.0.1";
    static private int registryPort = 5000;
}
