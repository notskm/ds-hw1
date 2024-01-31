package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskSummaryRequestTest {
    @Test
    void testTaskSummaryRequestIsAnEvent() {
        TaskSummaryRequest request = new TaskSummaryRequest();
        assert(request instanceof Event);
    }
    
    @Test
    void testGetType() {
        TaskSummaryRequest request = new TaskSummaryRequest();
        assertEquals(Protocol.FULL_TRAFFIC_SUMMARY.ordinal(), request.getType());
    }
}
