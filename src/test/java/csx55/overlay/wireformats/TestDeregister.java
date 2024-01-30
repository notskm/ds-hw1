package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestDeregister {
    @Test
    void testDeregisterIsAnEvent() {
        Deregister deregister = new Deregister("localhost", 5000);
        assert (deregister instanceof Event);
    }

    @ParameterizedTest
    @ValueSource(strings = { "localhost", "192.168.0.1" })
    void testGetIpAddress(String ipAddress) {
        Deregister deregister = new Deregister(ipAddress, 0);
        assertEquals(ipAddress, deregister.getIpAddress());
    }

    @ParameterizedTest
    @ValueSource(strings = { "localhost", "192.168.0.1" })
    void testGetIpAddressWhenConstructedFromBytes(String ipAddress) {
        try {
            byte[] deregisterBytes = new Deregister(ipAddress, 0).getBytes();
            Deregister deregister = new Deregister(deregisterBytes);

            assertEquals(ipAddress, deregister.getIpAddress());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 5000, 5001, 8000 })
    void testGetPort(int port) {
        Deregister deregister = new Deregister("localhost", port);
        assertEquals(port, deregister.getPort());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 5000, 8000, 3472 })
    void testGetPortWhenConstructedFromBytes(int port) {
        try {
            byte[] deregisterBytes = new Deregister("localhost", port).getBytes();
            Deregister deregister = new Deregister(deregisterBytes);

            assertEquals(port, deregister.getPort());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetType() {
        Deregister deregister = new Deregister("localhost", 5000);
        assertEquals(Protocol.DEREGISTER_REQUEST.ordinal(), deregister.getType());
    }

    @ParameterizedTest
    @ValueSource(strings = { "localhost", "192.168.0.1" })
    void testGetBytesGivenIPAddress(String ipAddress) {
        try {
            Deregister deregister = new Deregister(ipAddress, 5000);
            byte[] marshalledBytes = deregister.getBytes();
            byte[] expectedBytes = createDeregisterByteArray(ipAddress, 5000);

            assertArrayEquals(expectedBytes, marshalledBytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 5000, 8000 })
    void testGetBytesGivenIPAddress(int port) {
        try {
            Deregister deregister = new Deregister("", port);
            byte[] marshalledBytes = deregister.getBytes();
            byte[] expectedBytes = createDeregisterByteArray("", port);

            assertArrayEquals(expectedBytes, marshalledBytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private byte[] createDeregisterByteArray(String ipAddress, int port) throws IOException {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(baout);

        dout.writeInt(Protocol.DEREGISTER_REQUEST.ordinal());
        dout.writeInt(ipAddress.length());
        dout.writeBytes(ipAddress);
        dout.writeInt(port);

        byte[] marshalledBytes = baout.toByteArray();

        dout.close();
        baout.close();

        return marshalledBytes;
    }
}
