package csx55.overlay.wireformats;

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
}
