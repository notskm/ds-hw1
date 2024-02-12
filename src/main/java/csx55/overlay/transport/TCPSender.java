package csx55.overlay.transport;

import java.net.Socket;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import csx55.overlay.wireformats.Event;

public class TCPSender {
    Socket sendSocket;

    public TCPSender(Socket socket) {
        sendSocket = socket;
    }

    public void send(Event event) throws IOException {
        OutputStream out = sendSocket.getOutputStream();
        BufferedOutputStream bout = new BufferedOutputStream(out);
        DataOutputStream dout = new DataOutputStream(bout);

        byte[] bytes = event.getBytes();

        dout.writeInt(bytes.length);
        dout.write(bytes);
        dout.flush();
    }
}
