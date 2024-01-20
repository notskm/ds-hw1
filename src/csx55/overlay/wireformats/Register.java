package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Register {
    private String ipAddress;
    private int portNumber;
    
    public Register(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
        
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
    
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        
        dout.writeInt(ipAddress.length());
        dout.writeBytes(ipAddress);
        dout.writeInt(portNumber);
        
        dout.flush();
        byte[] marshalledBytes = baOutputStream.toByteArray();
        
        baOutputStream.close();
        dout.close();
        
        return marshalledBytes;
    }
}
