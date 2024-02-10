package csx55.overlay.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.EventFactory;

public class TCPReceiverThread extends Thread {
    Socket receiverSocket;
    Queue<Event> eventQueue;
    EventFactory eventFactory;
    final String socketIp;
    DataInputStream dis;

    public TCPReceiverThread(Socket socket, BlockingQueue<Event> events) throws IOException {
        receiverSocket = socket;
        eventQueue = events;
        eventFactory = EventFactory.getInstance();
        socketIp = socket.getInetAddress().getCanonicalHostName();
        dis = getDataStream();
    }

    private DataInputStream getDataStream() throws IOException {
        InputStream is = receiverSocket.getInputStream();
        return new DataInputStream(is);
    }

    public void run() {
        while (!receiverSocket.isClosed()) {
            waitThenQueueEvent(dis);
        }
    }

    private void waitThenQueueEvent(DataInputStream dis) {
        try {
            Event event = readEvent(dis);
            event.setOriginIp(socketIp);
            event.setOriginSocket(receiverSocket);
            enqueueEvent(event);
        } catch (IOException e) {
            closeSocket();
        }
    }

    private Event readEvent(DataInputStream dis) throws IOException {
        int length = dis.readInt();
        byte[] data = new byte[length];
        dis.readFully(data);
        Event event = eventFactory.getEvent(data);
        return event;
    }

    private synchronized void enqueueEvent(Event event) {
        eventQueue.add(event);
    }

    private void closeSocket() {
        try {
            dis.close();
        } catch (IOException e) {
        }
    }
}
