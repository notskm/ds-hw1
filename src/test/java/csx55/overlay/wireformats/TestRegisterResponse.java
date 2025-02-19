package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import csx55.overlay.wireformats.RegisterResponse.Status;

public class TestRegisterResponse {
    @Test
    void testRegisterResponseIsAnEvent() {
        RegisterResponse response = new RegisterResponse(Status.SUCCESS, "");
        assert (response instanceof Event);
    }

    @Test
    void testConstruction() {
        RegisterResponse response = new RegisterResponse(Status.SUCCESS, "");
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals("", response.getInfo());
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    void testConstructionFromBytesWithStatus(Status status) {
        try {
            RegisterResponse expected = new RegisterResponse(status, "");
            RegisterResponse response = new RegisterResponse(expected.getBytes());
            assertArrayEquals(expected.getBytes(), response.getBytes());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings={"", "error message", "success message"})
    void testConstructionFromBytesWithMessage(String message) {
        try {
            RegisterResponse expected = new RegisterResponse(Status.SUCCESS, message);
            RegisterResponse response = new RegisterResponse(expected.getBytes());
            assertArrayEquals(expected.getBytes(), response.getBytes());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetType() {
        RegisterResponse response = new RegisterResponse(Status.SUCCESS, "");
        assertEquals(Protocol.REGISTER_RESPONSE.ordinal(), response.getType());
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    void testGetBytesStatus(Status status) {
        try {
            RegisterResponse response = new RegisterResponse(status, "");
            byte[] marshalledBytes = response.getBytes();
            byte[] expectedBytes = createMarshalledRegisterResponse(status, "");

            assertArrayEquals(expectedBytes, marshalledBytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "test", "hello world" })
    void testGetBytesInfoString(String info) {
        try {
            RegisterResponse response = new RegisterResponse(Status.SUCCESS, info);
            byte[] marshalledBytes = response.getBytes();
            byte[] expectedBytes = createMarshalledRegisterResponse(Status.SUCCESS, info);

            assertArrayEquals(expectedBytes, marshalledBytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    byte[] createMarshalledRegisterResponse(Status status, String info) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        dout.writeInt(Protocol.REGISTER_RESPONSE.ordinal());
        // dout.writeInt(status.ordinal());
        if(status == Status.SUCCESS) {
            dout.writeByte(0);
        }
        else {
            dout.writeByte(1);
        }
        dout.writeInt(info.length());
        dout.writeBytes(info);

        byte[] bytes = bout.toByteArray();

        dout.close();
        bout.close();

        return bytes;
    }

    @Test
    void testGetStatusFailure() {
        RegisterResponse response = new RegisterResponse(Status.FAILURE, "");
        assertEquals(Status.FAILURE, response.getStatus());
    }

    @Test
    void testGetInfo() {
        RegisterResponse response = new RegisterResponse(Status.SUCCESS, "information");
        assertEquals("information", response.getInfo());
    }
}
