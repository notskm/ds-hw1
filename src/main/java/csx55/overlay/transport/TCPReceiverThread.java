package csx55.overlay.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.EventFactory;

public class TCPReceiverThread extends Thread {
    Socket recieverSocket;
    Queue<Event> eventQueue;
    EventFactory eventFactory;

    public TCPReceiverThread(Socket socket) {
        recieverSocket = socket;
        eventQueue = new LinkedList<>();
        eventFactory = EventFactory.getInstance();
    }

    public void run() {
        try (DataInputStream dis = getDataStream()) {
            while (!recieverSocket.isClosed()) {
                waitThenQueueEvent(dis);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void waitThenQueueEvent(DataInputStream dis) throws IOException {
        Event event = readEvent(dis);
        enqueueEvent(event);
    }

    private DataInputStream getDataStream() throws IOException {
        InputStream is = recieverSocket.getInputStream();
        return new DataInputStream(is);
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

    public synchronized Event poll() {
        return eventQueue.poll();
    }
}
