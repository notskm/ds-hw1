package csx55.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterResponse implements Event {
    enum Status {
        SUCCESS,
        FAILURE
    }

    private Status response_status;
    private String information;
    
    RegisterResponse(Status status, String info) {
        response_status = status;
        information = info;
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
