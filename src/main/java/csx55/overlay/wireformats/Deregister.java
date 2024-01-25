package csx55.overlay.wireformats;

import java.io.IOException;

public class Deregister implements Event {
    String ipAddress;
    int portNumber;

    public Deregister(String ip, int port) {
        ipAddress = ip;
        portNumber = port;
    }

    @Override
    public byte[] getBytes() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBytes'");
    }

    @Override
    public int getType() {
        return Protocol.DEREGISTER_REQUEST.ordinal();
    }

    public Object getIpAddress() {
        return ipAddress;
    }

	public Integer getPort() {
        return portNumber;
	}
}
