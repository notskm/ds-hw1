package csx55.overlay.transport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import csx55.overlay.wireformats.Register;

import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class TCPSenderTest {
    @Test
    void testSendEvent() {
        try {
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(baout);

            Socket outSocket = mock(Socket.class);
            when(outSocket.getOutputStream()).thenReturn(dout);

            Register register = new Register("localhost", 5000);
            TCPSender sender = new TCPSender(outSocket);

            sender.send(register);

            byte[] receivedBytes = baout.toByteArray();
            
            ByteArrayInputStream bain = new ByteArrayInputStream(receivedBytes);
            DataInputStream din = new DataInputStream(bain);
            
            int length = din.readInt();
            byte[] data = new byte[length];
            din.readFully(data);
            
            byte[] registerBytes = register.getBytes();

            assertEquals(registerBytes.length, length);
            assertArrayEquals(registerBytes, data);
            assertEquals(0, din.available());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
