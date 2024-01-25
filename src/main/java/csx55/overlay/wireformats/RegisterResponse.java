package csx55.overlay.wireformats;

public class RegisterResponse {
    enum Status {
        SUCCESS,
        FAILURE
    }
    
    RegisterResponse(Status status, String info) {

    }
    
    public Status getStatus() {
        return Status.SUCCESS;
    }
    
    public String getInfo() {
        return "";
    }
}
