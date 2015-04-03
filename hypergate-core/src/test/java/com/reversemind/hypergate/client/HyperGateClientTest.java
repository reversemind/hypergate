package com.reversemind.hypergate.client;

import com.reversemind.hypergate.Payload;
import com.reversemind.hypergate.PayloadStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HyperGateClientTest {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ClientPoolTest.class);

    private HyperGateClient hyperGateClient;

    @Before
    public void init() throws Exception {
        hyperGateClient = new HyperGateClient();
        hyperGateClient.start();
    }

    @After
    public void destroy() throws Exception {
        if(hyperGateClient != null){
            hyperGateClient.shutdown();
            hyperGateClient = null;
        }
    }

    @Test
    public void testSetClientTimeOut() throws Exception {
        hyperGateClient.setClientTimeOut(1);
    }

    @Test
    public void testIsRunning() throws Exception {
        assertTrue(hyperGateClient.isRunning());
    }

    @Test
    public void testGetPort() throws Exception {
        assertEquals(7000, hyperGateClient.getPort());
    }

    @Test
    public void testGetHost() throws Exception {
        assertEquals("localhost", hyperGateClient.getHost());
    }

    @Test
    public void testGetPayload() throws Exception {
        hyperGateClient = new HyperGateClient("localhost", 7000, 10);
        Payload payload = hyperGateClient.getPayload();
        LOG.info("Payload: " + payload);

        assertEquals(null, payload.getResultResponse());
        assertEquals(null, payload.getMethodName());
        assertEquals(null, payload.getArguments());
        assertEquals(payload.getServerTimestamp(), payload.getClientTimestamp());
        assertEquals(PayloadStatus.ERROR_SERVER_TIMEOUT, payload.getStatus());
        assertEquals(null, payload.getThrowable());


        HyperGateClient _hyperGateClient = mock(HyperGateClient.class);
        when(_hyperGateClient.getPayload()).thenReturn(
                new Payload(){
                    public Object getResultResponse() {
                        return "getResultResponse";
                    }
                }
        );

        assertEquals("getResultResponse", _hyperGateClient.getPayload().getResultResponse());
    }

    @Test
    public void testIsOccupied() throws Exception {
        assertFalse(hyperGateClient.isOccupied());
    }

    public void testSend() {
        boolean result = false;
        try{
            hyperGateClient.send(null);
        }catch (IOException ex){
            result = true;
        }
        assertTrue(result);
    }

    public void testShutdown() throws Exception {
        assertTrue(hyperGateClient.isRunning());

        hyperGateClient.shutdown();

        assertFalse(hyperGateClient.isRunning());
        assertFalse(hyperGateClient.isOccupied());
    }

    public void testRestart() throws Exception {

    }

    public void testRestart1() throws Exception {

    }

    public void testGetName() throws Exception {

    }

    public void testStart() throws Exception {

    }
}