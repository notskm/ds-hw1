package csx55.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class TCPServerThread extends Thread {
    private ServerSocket serverSocket;
    private Queue<Socket> nodeQueue;

    public TCPServerThread(ServerSocket socket) {
        serverSocket = socket;
        nodeQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            waitThenQueueSocket();
        }

        closeServerSocket();
    }

    private void waitThenQueueSocket() {
        try {
            Socket socket = serverSocket.accept();
            queueSocket(socket);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private synchronized boolean queueSocket(Socket socket) {
        return nodeQueue.add(socket);
    }

    public synchronized Socket poll() {
        return nodeQueue.poll();
    }
}
