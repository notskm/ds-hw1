package csx55.overlay.node;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import csx55.overlay.transport.TCPReceiverThread;
import csx55.overlay.transport.TCPServerThread;
import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.Protocol;
import csx55.overlay.wireformats.Register;
import csx55.overlay.wireformats.RegisterResponse;
import csx55.overlay.wireformats.RegisterResponse.Status;

public class Registry {
    private static ArrayList<TCPReceiverThread> receiverThreads;
    private static ArrayList<MessagingNodeInfo> messagingNodeInfo;

    public static void main(String[] args) {
        parseArgs(args);
        
        receiverThreads = new ArrayList<>();
        messagingNodeInfo = new ArrayList<>();

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
                        onEvent(event, thread.getSocket());
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

    private static void onEvent(Event event, Socket socket) {
        switch (Protocol.values()[event.getType()]) {
            case REGISTER_REQUEST:
                onRegisterRequest((Register) event, socket);
                break;
            default:
                break;
        }
    }

    private static void onRegisterRequest(Register register, Socket socket) {
        MessagingNodeInfo info = new MessagingNodeInfo(register.getIpAddress(), register.getPortNumber());
        
        RegisterResponse.Status status = Status.SUCCESS;
        String message = "";
        
        InetSocketAddress socketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
        String socketIP = socketAddress.getAddress().getHostAddress();
        if(!socketIP.equals(register.getIpAddress())){
            status = Status.FAILURE;

            message = "Registration request unsuccessful. The provided IP address ";
            message += register.getIpAddress();
            message += " does not match the node's ip address ";
            message += socketIP;
        }
        else if(messagingNodeInfo.contains(info)) {
            status = Status.FAILURE;
            message = "Registration request unsuccessful. The socket '";
            message += register.getIpAddress() + ":" + register.getPortNumber();
            message += "' already exists within the registry.";
        }
        else {
            messagingNodeInfo.add(info);
            int numberOfNodes = messagingNodeInfo.size();
            message = "Registration request successful. ";
            message += "The number of messaging nodes currently constituting the overlay is ";
            message += "(" + numberOfNodes + ")";
        }
        
        RegisterResponse response = new RegisterResponse(status, message);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);
            byte[] responseBytes = response.getBytes();
            dos.writeInt(responseBytes.length);
            dos.write(responseBytes);
            dos.flush();
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
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

class MessagingNodeInfo {
    private String ipAddress;
    private int portNumber;
    
    public MessagingNodeInfo(String address, int port) {
        ipAddress = address;
        portNumber = port;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public int getPort() {
        return portNumber;
    }
    
    @Override
    public boolean equals(Object other) {
        return
            other != null
            && other.getClass() == getClass()
            && ((MessagingNodeInfo)other).ipAddress.equals(ipAddress)
            && ((MessagingNodeInfo)other).portNumber == portNumber;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, portNumber);
    }
}
