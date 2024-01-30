package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Register extends Event {
    private String ipAddress;
    private int portNumber;

    public Register(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int typeValue = din.readInt();
        if (typeValue != Protocol.REGISTER_REQUEST.ordinal()) {
            throw new IOException("Bytes didn't correspond to a RegisterRequest.");
        }

        int ipAddressLength = din.readInt();
        byte[] ipAddressBytes = new byte[ipAddressLength];
        din.readFully(ipAddressBytes);

        ipAddress = new String(ipAddressBytes);
        portNumber = din.readInt();

        baInputStream.close();
        din.close();
    }

    public Register(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    @Override
    public int getType() {
        return Protocol.REGISTER_REQUEST.ordinal();
    }
}
