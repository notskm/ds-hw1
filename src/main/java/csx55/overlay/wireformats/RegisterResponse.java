package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class RegisterResponse extends Event {
    public enum Status {
        SUCCESS,
        FAILURE
    }

    private byte responseStatus;
    private String information;
    
    public RegisterResponse(Status status, String info) {
        responseStatus = status == Status.SUCCESS ? (byte)0 : (byte)1;
        information = info;
    }
    
    public RegisterResponse(byte[] bytes) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(bain);

        int typeOrdinal = din.readInt();
        if(typeOrdinal != getType()) {
            throw new IOException("Read incorrect Protocol type: " + typeOrdinal);
        }
        
        responseStatus = din.readByte();

        int length = din.readInt();
        byte[] infoData = new byte[length];
        din.readFully(infoData);

        information = new String(infoData);
    }
    
    public Status getStatus() {
        if(responseStatus == (byte)0) {
            return Status.SUCCESS;
        }
        else {
            return Status.FAILURE;
        }
    }
    
    public String getInfo() {
        return information;
    }

    @Override
    public int getType() {
        return Protocol.REGISTER_RESPONSE.ordinal();
    }
}
