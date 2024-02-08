package csx55.overlay.wireformats;

import java.io.IOException;

public class TaskSummaryResponse extends Event {
    private String nodeIP;
    private int nodePort;
    private int numMessagesSent;
    private long sumMessagesSent;
    private int numMessagesReceived;
    private long sumMessagesReceived;
    private int numMessagesRelayed;

    public TaskSummaryResponse(String ip, int port, int numSent, long sumSent, int numReceived, long sumReceived,
            int numRelayed) {
        nodeIP = ip;
        nodePort = port;
        numMessagesSent = numSent;
        sumMessagesSent = sumSent;
        numMessagesReceived = numReceived;
        sumMessagesReceived = sumReceived;
        numMessagesRelayed = numRelayed;
    }

    public TaskSummaryResponse(byte[] bytes) throws IOException {
        super(bytes);
    }

    @Override
    public int getType() {
        return Protocol.TRAFFIC_SUMMARY.ordinal();
    }

    public String getIp() {
        return nodeIP;
    }

    public int getPort() {
        return nodePort;
    }

    public int getNumberOfMessagesSent() {
        return numMessagesSent;
    }

    public long getSummationOfMessagesSent() {
        return sumMessagesSent;
    }

    public int getNumberOfMessagesReceived() {
        return numMessagesReceived;
    }

    public long getSummationOfMessagesReceived() {
        return sumMessagesReceived;
    }

    public int getNumberOfMessagesRelayed() {
        return numMessagesRelayed;
    }
}
