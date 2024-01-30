package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class EventFactory {
    private static EventFactory instance = null;
    private static Protocol[] protocolValues = Protocol.values();

    public static EventFactory getInstance() {
        if(instance == null) {
            instance = new EventFactory();
        }

        return instance;
    }
    
    private EventFactory() {
    }
    
    public Event getEvent(byte[] data) {
        if(data.length < 1) {
            return null;
        }

        try {
            Protocol type = readEventType(data);
            switch(type) {
                case REGISTER_REQUEST:
                    return new Register(data);
                case REGISTER_RESPONSE:
                    return new RegisterResponse(data);
                default:
                    return null;
            }
        }
        catch(IOException e) {
            System.err.println(e);
            return null;
        }
    }
    
    private Protocol readEventType(byte[] data) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(bain);
        
        int typeOrdinal = din.readInt();

        return protocolValues[typeOrdinal];
    }
}
