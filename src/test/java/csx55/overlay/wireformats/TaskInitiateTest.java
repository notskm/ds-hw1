package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TaskInitiateTest {
    @Test
    public void testTaskInitiateIsAnEvent() {
        TaskInitiate taskInitiate = new TaskInitiate(5);
        assert(taskInitiate instanceof Event);
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testGetRounds(int rounds) {
        TaskInitiate taskInitiate = new TaskInitiate(rounds);
        assertEquals(rounds, taskInitiate.getRounds());
    }
    
    @Test
    public void testGetType() {
        TaskInitiate taskInitiate = new TaskInitiate(0);
        assertEquals(Protocol.TASK_INITIATE.ordinal(), taskInitiate.getType());
    }
}
