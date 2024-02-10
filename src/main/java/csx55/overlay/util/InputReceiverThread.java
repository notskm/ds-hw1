package csx55.overlay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class InputReceiverThread extends Thread {
    private Queue<String> inputQueue;

    public InputReceiverThread() {
        inputQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                addLine(reader.readLine());
            }
        } catch (IOException e) {

        }
    }

    private synchronized void addLine(String line) {
        inputQueue.add(line);
    }

    public synchronized String poll() {
        return inputQueue.poll();
    }
}
