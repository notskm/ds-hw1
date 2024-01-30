package csx55.overlay.wireformats;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

abstract public class Event {
    public Event() {
        
    }

    public Event(byte[] marshalledBytes) {
         
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DataOutputStream daout = new DataOutputStream(baout);
        
        daout.writeInt(getType());

        for(Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            
            try {
                if(field.getType() == String.class) {
                    String stringField = (String)field.get(this);
                    daout.writeInt(stringField.length());
                    daout.writeBytes(stringField);
                }
                else if(field.getType() == int.class) {
                    int intField = (int)field.get(this);
                    daout.writeInt(intField);
                }
                else if(field.getType() == byte.class) {
                    byte byteField = (byte)field.get(this);
                    daout.writeByte(byteField);
                }
            } catch(IllegalAccessException e) {
            }
        }
        
        byte[] bytes = baout.toByteArray();
        return bytes;
    }

    abstract public int getType();
}
