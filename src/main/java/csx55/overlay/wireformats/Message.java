package csx55.overlay.wireformats;

import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;

public class Message extends Event {
    private int randomNumber;

    public Message() {
        randomNumber = ThreadLocalRandom.current().nextInt();
    }

    public Message(byte[] data) throws IOException {
        super(data);
    }

    public int getNumber() {
        return randomNumber;
    }

    @Override
    public int getType() {
        return Protocol.MESSAGE.ordinal();
    }
}
