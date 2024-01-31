package csx55.overlay.wireformats;

import java.io.IOException;

public class Register extends Event {
    private String ipAddress;
    private int portNumber;

    public Register(byte[] marshalledBytes) throws IOException {
        super(marshalledBytes);
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
