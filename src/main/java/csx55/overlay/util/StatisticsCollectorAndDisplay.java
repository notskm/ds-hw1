package csx55.overlay.util;

import csx55.overlay.wireformats.TaskSummaryResponse;
import java.util.List;
import java.util.ArrayList;

public class StatisticsCollectorAndDisplay {
    private List<Statistics> statistics;
    Statistics grandTotal;

    public StatisticsCollectorAndDisplay() {
        statistics = new ArrayList<>();
        grandTotal = new Statistics();
        grandTotal.nodeName = "total";
    }

    public void addStats(TaskSummaryResponse taskInfo) {
        statistics.add(new Statistics(taskInfo));
        updateGrandTotals(taskInfo);
    }

    private void updateGrandTotals(TaskSummaryResponse taskInfo) {
        grandTotal.numberOfMessagesReceived += taskInfo.getNumberOfMessagesReceived();
        grandTotal.numberOfMessagesSent += taskInfo.getNumberOfMessagesSent();
        grandTotal.summationOfReceivedMessages += taskInfo.getSummationOfMessagesReceived();
        grandTotal.summationOfSentMessages += taskInfo.getSummationOfMessagesSent();
    }

    public void display() {
        printHeader();
        printAllStatistics();
        printGrandTotals();
    }

    private void printHeader() {
        System.out.print("Node");
        System.out.print(" | ");
        System.out.print("Number of messages sent");
        System.out.print(" | ");
        System.out.print("Number of messages recieved");
        System.out.print(" | ");
        System.out.print("Summation of sent messages");
        System.out.print(" | ");
        System.out.print("Summation of received messages");
        System.out.print(" | ");
        System.out.print("Number of messages relayed");
        System.out.println();
    }

    private void printAllStatistics() {
        for (Statistics stat : statistics) {
            printStat(stat);
        }
    }

    private void printStat(Statistics stat) {
        System.out.print(stat.nodeName);
        System.out.print(" | ");
        System.out.print(stat.numberOfMessagesSent);
        System.out.print(" | ");
        System.out.print(stat.numberOfMessagesReceived);
        System.out.print(" | ");
        System.out.print(stat.summationOfSentMessages);
        System.out.print(" | ");
        System.out.print(stat.summationOfReceivedMessages);
        System.out.print(" | ");
        System.out.print(stat.numberOfMessagesRelayed);
        System.out.println();
    }

    private void printGrandTotals() {
        System.out.print(grandTotal.nodeName);
        System.out.print(" | ");
        System.out.print(grandTotal.numberOfMessagesSent);
        System.out.print(" | ");
        System.out.print(grandTotal.numberOfMessagesReceived);
        System.out.print(" | ");
        System.out.print(grandTotal.summationOfSentMessages);
        System.out.print(" | ");
        System.out.print(grandTotal.summationOfReceivedMessages);
        System.out.println();
    }

    private class Statistics {
        public String nodeName = "";
        public int numberOfMessagesSent = 0;
        public long summationOfSentMessages = 0;
        public int numberOfMessagesReceived = 0;
        public long summationOfReceivedMessages = 0;
        public int numberOfMessagesRelayed = 0;

        public Statistics() {

        }

        public Statistics(TaskSummaryResponse taskInfo) {
            nodeName = taskInfo.getIp() + ':' + taskInfo.getPort();
            numberOfMessagesSent = taskInfo.getNumberOfMessagesSent();
            numberOfMessagesReceived = taskInfo.getNumberOfMessagesReceived();
            numberOfMessagesRelayed = taskInfo.getNumberOfMessagesRelayed();

            summationOfReceivedMessages = taskInfo.getSummationOfMessagesReceived();
            summationOfSentMessages = taskInfo.getSummationOfMessagesSent();
        }
    }
}
