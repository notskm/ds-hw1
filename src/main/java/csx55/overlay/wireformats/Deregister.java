package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Deregister implements Event {
    String ipAddress;
    int portNumber;

    public Deregister(String ip, int port) {
        ipAddress = ip;
        portNumber = port;
    }

    public Deregister(byte[] deregisterBytes) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(deregisterBytes);
        DataInputStream din = new DataInputStream(bain);
        
        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];

        din.readFully(ipBytes);
        
        portNumber = din.readInt();
        
        ipAddress = new String(ipBytes);
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(baout);

        dout.writeInt(ipAddress.length());
        dout.writeBytes(ipAddress);
        dout.writeInt(portNumber);
        
        byte[] marshalledBytes = baout.toByteArray();
        
        dout.close();
        baout.close();

        return marshalledBytes;
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
