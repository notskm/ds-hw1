package csx55.overlay.wireformats;

public class RegisterResponse {
    enum Status {
        SUCCESS,
        FAILURE
    }
    
    private Status response_status;
    
    RegisterResponse(Status status, String info) {
        response_status = status;
    }
    
    public Status getStatus() {
        return response_status;
    }
    
    public String getInfo() {
        return "";
    }
}
