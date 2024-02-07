package csx55.overlay.wireformats;

import java.util.concurrent.ThreadLocalRandom;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message extends Event {
    private int randomNumber;
    private MessagingNodeInfo[] routingPlan;
    private int index = 0;

    public Message(MessagingNodeInfo[] route) {
        randomNumber = ThreadLocalRandom.current().nextInt();
        routingPlan = route;
    }

    public Message(byte[] data) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(bain);

        if (din.readInt() != getType()) {
            throw new IOException();
        }

        randomNumber = din.readInt();

        routingPlan = new MessagingNodeInfo[din.readInt()];
        for (int i = 0; i < routingPlan.length; i++) {
            int hostnameLength = din.readInt();
            byte[] hostnameBytes = new byte[hostnameLength];
            din.readFully(hostnameBytes);

            String hostname = new String(hostnameBytes);

            routingPlan[i] = new MessagingNodeInfo(hostname, din.readInt());
        }

        index = din.readInt();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(baout);

        dout.writeInt(getType());
        dout.writeInt(randomNumber);
        dout.writeInt(routingPlan.length);

        for (MessagingNodeInfo node : routingPlan) {
            String hostname = node.getHostname();
            dout.writeInt(hostname.length());
            dout.writeBytes(hostname);
            dout.writeInt(node.getPort());
        }

        dout.writeInt(index);

        byte[] output = baout.toByteArray();
        dout.close();
        baout.close();

        return output;
    }

    public int getNumber() {
        return randomNumber;
    }

    public MessagingNodeInfo nextNode() {
        if (index < routingPlan.length) {
            MessagingNodeInfo next = routingPlan[index];
            index++;
            return next;
        }

        return null;
    }

    @Override
    public int getType() {
        return Protocol.MESSAGE.ordinal();
    }
}
