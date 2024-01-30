package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterResponse implements Event {
    public enum Status {
        SUCCESS,
        FAILURE
    }

    private Status response_status;
    private String information;
    
    public RegisterResponse(Status status, String info) {
        response_status = status;
        information = info;
    }
    
    public RegisterResponse(byte[] bytes) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(bain);

        int typeOrdinal = din.readInt();
        if(typeOrdinal != getType()) {
            throw new IOException("Read incorrect Protocol type: " + typeOrdinal);
        }
        
        int status = din.readInt();
        response_status = Status.values()[status];

        int length = din.readInt();
        byte[] infoData = new byte[length];
        din.readFully(infoData);

        information = new String(infoData);
    }
    
    public Status getStatus() {
        return response_status;
    }
    
    public String getInfo() {
        return information;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        
        dout.writeInt(getType());
        dout.writeInt(response_status.ordinal());
        dout.writeInt(information.length());
        dout.writeBytes(information);

        byte[] bytes = bout.toByteArray();

        dout.close();
        bout.close();
        
        return bytes;
    }

    @Override
    public int getType() {
        return Protocol.REGISTER_RESPONSE.ordinal();
    }
}
