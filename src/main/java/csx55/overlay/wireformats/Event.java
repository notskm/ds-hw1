package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;

abstract public class Event {
    Socket originSocket;
    String originIp;

    public Event() {

    }

    public Event(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bain = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(bain);

        if (getType() != din.readInt()) {
            throw new IOException();
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getType() == String.class) {
                    int size = din.readInt();
                    byte[] stringData = new byte[size];
                    din.readFully(stringData);
                    field.set(this, new String(stringData));
                } else if (field.getType() == int.class) {
                    field.set(this, din.readInt());
                } else if (field.getType() == byte.class) {
                    field.set(this, din.readByte());
                }
            } catch (IllegalAccessException e) {

            }
        }
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DataOutputStream daout = new DataOutputStream(baout);

        daout.writeInt(getType());

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                if (field.getType() == String.class) {
                    String stringField = (String) field.get(this);
                    daout.writeInt(stringField.length());
                    daout.writeBytes(stringField);
                } else if (field.getType() == int.class) {
                    int intField = (int) field.get(this);
                    daout.writeInt(intField);
                } else if (field.getType() == byte.class) {
                    byte byteField = (byte) field.get(this);
                    daout.writeByte(byteField);
                }
            } catch (IllegalAccessException e) {
            }
        }

        byte[] bytes = baout.toByteArray();
        return bytes;
    }

    abstract public int getType();

    public Socket getOriginSocket() {
        return originSocket;
    }

    public void setOriginSocket(Socket socket) {
        originSocket = socket;
    }

    public String getOriginIp() {
        return originIp;
    };

    public void setOriginIp(String ip) {
        originIp = ip;
    }
}
