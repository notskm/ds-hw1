package csx55.overlay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class InputReceiverThread extends Thread {
    private Queue<String> inputQueue;
    private boolean running = true;

    public InputReceiverThread() {
        inputQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            readInputForever();
        } catch (IOException e) {

        }
    }

    private void readInputForever() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (isRunning()) {
            if (reader.ready()) {
                addLine(reader.readLine());
            } else {
                sleep();
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
    }

    private synchronized void addLine(String line) {
        inputQueue.add(line);
    }

    public synchronized String poll() {
        return inputQueue.poll();
    }

    public synchronized void shutdown() {
        running = false;
        interrupt();
    }

    public synchronized boolean isRunning() {
        return running;
    }
}
