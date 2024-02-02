package csx55.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MessagingNodesList extends Event {
    private MessagingNodeInfo[] nodeInfo;

    public MessagingNodesList(MessagingNodeInfo[] nodes) {
        nodeInfo = nodes;
    }

    public MessagingNodesList(byte[] bytes) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(bain);

        int type = din.readInt();
        if (type != getType()) {
            throw new IOException();
        }

        int nodeInfoLength = din.readInt();
        nodeInfo = new MessagingNodeInfo[nodeInfoLength];
        for (int i = 0; i < nodeInfoLength; i++) {
            int hostnameLength = din.readInt();
            byte[] hostnameBytes = new byte[hostnameLength];
            din.readFully(hostnameBytes);
            String hostname = new String(hostnameBytes);
            int port = din.readInt();
            nodeInfo[i] = new MessagingNodeInfo(hostname, port);
        }
    }

    public MessagingNodeInfo[] getNodeInfo() {
        return nodeInfo;
    }

    @Override
    public int getType() {
        return Protocol.MESSAGING_NODES_LIST.ordinal();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(baout);

        dout.writeInt(getType());
        dout.writeInt(nodeInfo.length);
        for (MessagingNodeInfo node : nodeInfo) {
            dout.writeInt(node.getHostname().length());
            dout.writeBytes(node.getHostname());
            dout.writeInt(node.getPort());
        }

        byte[] bytes = baout.toByteArray();
        baout.close();
        return bytes;
    }
}
