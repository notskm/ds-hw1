package csx55.overlay.transport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.Register;

public class TCPReceiverThreadTest {
    Socket receiverSocket;
    InetAddress receiverAddress;
    BlockingQueue<Event> eventQueue;

    @BeforeEach
    void setup() {
        eventQueue = new LinkedBlockingQueue<>();
        receiverSocket = mock(Socket.class);
        receiverAddress = mock(InetAddress.class);

        when(receiverSocket.getInetAddress()).thenReturn(receiverAddress);
        when(receiverAddress.getCanonicalHostName()).thenReturn("0.0.0.0");
    }

    @Test
    void testTCPReceiverThreadIsAThread() {
        TCPReceiverThread thread = makeThread();
        assert (thread instanceof Thread);
    }

    @Test
    void testThreadQueuesEvents() {
        Register registerEvent = new Register("localhost", 5000);
        addEventToSocketInputStream(registerEvent);

        TCPReceiverThread thread = makeThread();
        thread.start();

        Event event = null;
        while (event == null) {
            event = eventQueue.poll();
        }

        try {
            assertArrayEquals(registerEvent.getBytes(), event.getBytes());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    byte[] addLengthToByteArray(byte[] bytes) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(bytes.length);
        dos.write(bytes);
        return bos.toByteArray();
    }

    void addEventToSocketInputStream(Event event) {
        try {
            byte[] bytes = addLengthToByteArray(event.getBytes());
            InputStream is = new ByteArrayInputStream(bytes);

            when(receiverSocket.getInputStream()).thenReturn(is);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    void closeSocketAndJoinThread(Thread thread) {
        try {
            receiverSocket.close();
            thread.join();
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    TCPReceiverThread makeThread() {
        try {
            TCPReceiverThread thread = new TCPReceiverThread(receiverSocket, eventQueue);
            return thread;
        } catch (IOException e) {
            fail(e.getMessage());
            return null;
        }
    }
}
