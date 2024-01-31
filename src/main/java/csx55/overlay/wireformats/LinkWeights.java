package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LinkWeights extends Event {
    LinkInfo[] linkInfo;

    public LinkWeights(LinkInfo[] links) {
        linkInfo = links;
    }
    
    public LinkWeights(byte[] bytes) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(bain);
        
        if(!(din.readInt() == Protocol.LINK_WEIGHTS.ordinal())) {
            throw new IOException();
        }
        
        readLinks(din);
        
        din.close();
    }
    
    private void readLinks(DataInputStream din) throws IOException {
        int numberOfLinks = din.readInt();
        linkInfo = new LinkInfo[numberOfLinks];

        for(int i = 0; i < numberOfLinks; i++) {
            String hostA = readHostname(din);
            int portA = readPort(din);
            String hostB = readHostname(din);
            int portB = readPort(din);
            int weight = readWeight(din);

            linkInfo[i] = new LinkInfo(hostA, portA, hostB, portB, weight);
        }
    }
    
    private String readHostname(DataInputStream din) throws IOException {
        int length = din.readInt();
        byte[] hostnameBytes = new byte[length];
        din.readFully(hostnameBytes);
        String hostname = new String(hostnameBytes);
        return hostname;
    }
    
    private int readPort(DataInputStream din) throws IOException {
        return din.readInt();
    }
    
    private int readWeight(DataInputStream din) throws IOException {
        return din.readInt();
    }

    public LinkInfo[] getLinks() {
        return linkInfo;
    }

    @Override
    public int getType() {
        return Protocol.LINK_WEIGHTS.ordinal();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        dout.writeInt(Protocol.LINK_WEIGHTS.ordinal());
        dout.writeInt(linkInfo.length);
        for (LinkInfo link : linkInfo) {
            dout.writeInt(link.getHostnameA().length());
            dout.writeBytes(link.getHostnameA());
            dout.writeInt(link.getPortA());
            dout.writeInt(link.getHostnameB().length());
            dout.writeBytes(link.getHostnameB());
            dout.writeInt(link.getPortB());
            dout.writeInt(link.getWeight());
        }

        byte[] bytes = bout.toByteArray();
        dout.close();

        return bytes;
    }
}
