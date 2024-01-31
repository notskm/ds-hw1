package csx55.overlay.wireformats;

import java.io.IOException;

public class TaskSummaryResponse extends Event {
    String nodeIP;
    int nodePort;
    int numMessagesSent;
    int sumMessagesSent;
    int numMessagesReceived;
    int sumMessagesReceived;
    int numMessagesRelayed;

    public TaskSummaryResponse(String ip, int port, int numSent, int sumSent, int numReceived, int sumReceived, int numRelayed) {
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

	public int getSummationOfMessagesSent() {
        return sumMessagesSent;
	}

	public int getNumberOfMessagesReceived() {
        return numMessagesReceived;
	}

	public int getSummationOfMessagesReceived() {
        return sumMessagesReceived;
	}

	public int getNumberOfMessagesRelayed() {
        return numMessagesRelayed;
	}
}
