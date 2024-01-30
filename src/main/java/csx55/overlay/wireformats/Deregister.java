package csx55.overlay.wireformats;

import java.io.IOException;

public class Deregister extends Event {
    String ipAddress;
    int portNumber;

    public Deregister(String ip, int port) {
        ipAddress = ip;
        portNumber = port;
    }

    public Deregister(byte[] deregisterBytes) throws IOException {
        super(deregisterBytes);
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
