package csx55.overlay.wireformats;

import java.io.IOException;

public class TaskSummaryRequest extends Event {
    public TaskSummaryRequest() {

    }

    public TaskSummaryRequest(byte[] bytes) throws IOException {
        super(bytes);
    }

    @Override
    public int getType() {
        return Protocol.FULL_TRAFFIC_SUMMARY.ordinal();
    }
}
