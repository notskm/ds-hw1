package csx55.overlay.wireformats;
public class Register {
    private String ipAddress;
    private int portNumber;
    
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
}
