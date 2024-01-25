package csx55.overlay.wireformats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import csx55.overlay.wireformats.RegisterResponse.Status;

public class TestRegisterResponse {
    @Test
    void testConstruction() {
        RegisterResponse response = new RegisterResponse(Status.SUCCESS, "");
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals("", response.getInfo());
    }
    
    @Test
    void testGetStatusFailure() {
        RegisterResponse response = new RegisterResponse(Status.FAILURE, "");
        assertEquals(Status.FAILURE, response.getStatus());
    }
}
