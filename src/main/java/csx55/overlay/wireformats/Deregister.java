package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Deregister extends Event {
    String ipAddress;
    int portNumber;

    public Deregister(String ip, int port) {
        ipAddress = ip;
        portNumber = port;
    }

    public Deregister(byte[] deregisterBytes) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(deregisterBytes);
        DataInputStream din = new DataInputStream(bain);
        
        int type = din.readInt();
        if(type != Protocol.DEREGISTER_REQUEST.ordinal()) {
            throw new IOException();
        }
        
        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];

        din.readFully(ipBytes);
        
        portNumber = din.readInt();
        
        ipAddress = new String(ipBytes);
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
