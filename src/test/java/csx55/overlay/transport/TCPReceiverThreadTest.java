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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.Register;

public class TCPReceiverThreadTest {
    Socket receiverSocket;
    InetAddress receiverAddress;

    @BeforeEach
    void setup() {
        receiverSocket = mock(Socket.class);
        receiverAddress = mock(InetAddress.class);

        when(receiverSocket.getInetAddress()).thenReturn(receiverAddress);
        when(receiverAddress.getCanonicalHostName()).thenReturn("0.0.0.0");
    }

    @Test
    void testTCPReceiverThreadIsAThread() {
        TCPReceiverThread thread = new TCPReceiverThread(receiverSocket);
        assert (thread instanceof Thread);
    }

    @Test
    void testPollWhenNoEventSent() {
        TCPReceiverThread thread = new TCPReceiverThread(receiverSocket);
        thread.start();

        try {
            receiverSocket.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        Event event = thread.poll();
        assertNull(event);
    }

    @Test
    void testPollWhenEventSent() {
        Register registerEvent = new Register("localhost", 5000);

        addEventToSocketInputStream(registerEvent);

        TCPReceiverThread thread = new TCPReceiverThread(receiverSocket);
        thread.start();

        closeSocketAndJoinThread(receiverSocket, thread);

        Event event = thread.poll();

        try {
            assertArrayEquals(registerEvent.getBytes(), event.getBytes());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetSocket() {
        TCPReceiverThread thread = new TCPReceiverThread(receiverSocket);
        Socket socket = thread.getSocket();
        assertEquals(socket, receiverSocket);
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

    void closeSocketAndJoinThread(Socket socket, Thread thread) {
        try {
            socket.close();
            thread.join();
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

}
