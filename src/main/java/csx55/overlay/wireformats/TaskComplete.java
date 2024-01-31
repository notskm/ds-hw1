package csx55.overlay.wireformats;

import java.io.IOException;

public class TaskComplete extends Event {
    String nodeIp;
    int nodePort;

    public TaskComplete(String ip, int port) {
        nodeIp = ip;
        nodePort = port;
    }
    
    public TaskComplete(byte[] bytes) throws IOException {
        super(bytes);
    }

	public String getIp() {
        return nodeIp;
	}

    public Integer getPort() {
        return nodePort;
    }

    @Override
    public int getType() {
        return Protocol.TASK_COMPLETE.ordinal();
    }
}
