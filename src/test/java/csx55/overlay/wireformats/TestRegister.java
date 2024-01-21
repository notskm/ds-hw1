package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.junit.jupiter.api.Test;

public class TestRegister {
    @Test
    public void testGettingIpAddress() {
        Register register = new Register("localhost", 5000);
        assertEquals(register.getIpAddress(), "localhost");
    }

    @Test
    public void testGettingPortNumber() {
        Register register = new Register("localhost", 5000);
        assertEquals(register.getPortNumber(), 5000);
    }

    @Test
    public void testConstructionFromBytes() {
        byte[] marshalledBytes = createMarshalledRegister(Register.Type.REGISTER_REQUEST, "localhost", 5000);
        try {
            Register register = new Register(marshalledBytes);

            assertEquals(register.getIpAddress(), "localhost");
            assertEquals(register.getPortNumber(), 5000);
        } catch (Exception e) {
            System.err.println(e);
            fail("Exception thrown when constructing Register from bytes.");
        }
    }

    private byte[] createMarshalledRegister(Register.Type type, String ipAddress, int portNumber) {
        try {
            ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(baOutputStream);
            dout.writeInt(type.ordinal());
            dout.writeInt(ipAddress.length());
            dout.writeBytes(ipAddress);
            dout.writeInt(portNumber);
            dout.flush();

            byte[] marshalledBytes = baOutputStream.toByteArray();
            dout.close();

            return marshalledBytes;
        } catch (Exception e) {
            System.err.println(e);
            fail("Exception thrown when marshalling Register.");
            return null;
        }
    }
}
