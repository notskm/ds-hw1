package csx55.overlay.wireformats;

import java.io.IOException;

public class TaskInitiate extends Event {
    int numberOfRounds;

    public TaskInitiate(int rounds) {
        numberOfRounds = rounds;
    }
    
    public TaskInitiate(byte[] bytes) throws IOException {
        super(bytes);
    }

    public int getRounds() {
        return numberOfRounds;
    }

    @Override
    public int getType() {
        return Protocol.TASK_INITIATE.ordinal();
    }
}
