package csx55.overlay.wireformats;

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
        super(bytes);
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
