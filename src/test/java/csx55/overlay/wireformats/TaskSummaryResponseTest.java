package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TaskSummaryResponseTest {
    @Test
    void testTaskSummaryResponseIsAnEvent() {
        TaskSummaryResponse response = new TaskSummaryResponse("", 0, 0, 0, 0, 0, 0);
        assert(response instanceof Event);
    }
    
    @Test
    void testGetType() {
        TaskSummaryResponse response = new TaskSummaryResponse("", 0, 0, 0, 0, 0, 0);
        assertEquals(Protocol.TRAFFIC_SUMMARY.ordinal(), response.getType());
    }

    @ParameterizedTest
    @ValueSource(strings = {"localhost", "127.0.0.1"})
    void testGetIp(String ip) {
        TaskSummaryResponse response = new TaskSummaryResponse(ip, 0, 0, 0, 0, 0 ,0);
        assertEquals(ip, response.getIp());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetPort(int port) {
        TaskSummaryResponse response = new TaskSummaryResponse("", port, 0, 0, 0, 0, 0);
        assertEquals(port, response.getPort());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetNumberOfMessagesSent(int numMessages) {
        TaskSummaryResponse response = new TaskSummaryResponse("", 0, numMessages, 0, 0, 0, 0);
        assertEquals(numMessages, response.getNumberOfMessagesSent());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetSummationOfMessagesSent(int sumMessages) {
        TaskSummaryResponse response = new TaskSummaryResponse("", 0, 0, sumMessages, 0, 0, 0);
        assertEquals(sumMessages, response.getSummationOfMessagesSent());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetNumberOfMessagesReceived(int numMessages) {
        TaskSummaryResponse response = new TaskSummaryResponse("", 0, 0, 0, numMessages, 0, 0);
        assertEquals(numMessages, response.getNumberOfMessagesReceived());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetSummationOfMessagesReceived(int sumMessages) {
        TaskSummaryResponse response = new TaskSummaryResponse("", 0, 0, 0, 0, sumMessages, 0);
        assertEquals(sumMessages, response.getSummationOfMessagesReceived());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetNumberOfMessagesRelayed(int numMessages) {
        TaskSummaryResponse response = new TaskSummaryResponse("", 0, 0, 0, 0, 0, numMessages);
        assertEquals(numMessages, response.getNumberOfMessagesRelayed());
    }
}
