package csx55.overlay.transport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTCPServerThread {
    ServerSocket serverSocket;

    @BeforeEach
    void createServerSocketMock() {
        serverSocket = mock(ServerSocket.class);
    }

    @Test
    void testTCPServerThreadIsAThread() {
        TCPServerThread serverThread = new TCPServerThread(serverSocket);
        assert (serverThread instanceof Thread);
    }

    @Test
    void testPollWithoutSocket() {
        TCPServerThread serverThread = new TCPServerThread(serverSocket);
        serverThread.start();

        sleep(100);

        Socket socket = serverThread.poll();
        assertNull(socket);
    }

    @Test
    void testPollWithSocket() {
        Socket mockSocket = null;
        try {
            mockSocket = mock(Socket.class);
            when(serverSocket.accept()).thenReturn(mockSocket);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        
        TCPServerThread serverThread = new TCPServerThread(serverSocket);
        serverThread.start();

        sleep(100);

        assertEquals(mockSocket, serverThread.poll());
    }
    
    void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch(InterruptedException e) {
            fail("Failed to sleep for the given time " + millis + "ms");
        }
    }
}
