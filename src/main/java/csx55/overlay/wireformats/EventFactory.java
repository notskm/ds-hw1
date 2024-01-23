package csx55.overlay.wireformats;

import java.io.IOException;

public class EventFactory {
    private static EventFactory instance = null;

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
            return new Register(data);
        }
        catch(IOException e) {
            System.err.println(e);
            return null;
        }
    }
}
