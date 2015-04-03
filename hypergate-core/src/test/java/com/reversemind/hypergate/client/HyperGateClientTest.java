package com.reversemind.hypergate.client;

import com.reversemind.hypergate.Payload;
import com.reversemind.hypergate.PayloadStatus;
import com.reversemind.hypergate.server.SimpleServer;
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

    private SimpleServer simpleServer;
    private HyperGateClient hyperGateClient;

    private static final int SERVER_PORT = 7000; // look carefully value in META-INF/hypergate-server.properties

    @Before
    public void init() throws Exception {
        hyperGateClient = new HyperGateClient("localhost", SERVER_PORT, 1);
        hyperGateClient.start();
    }

    @After
    public void destroy() throws Exception {
        if(hyperGateClient != null){
            hyperGateClient.shutdown();
            hyperGateClient = null;
        }

        if(simpleServer != null){
            simpleServer.destroy();
            simpleServer = null;
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
        assertEquals(SERVER_PORT, hyperGateClient.getPort());
    }

    @Test
    public void testGetHost() throws Exception {
        assertEquals("localhost", hyperGateClient.getHost());
    }

    @Test
    public void testGetPayload() throws Exception {
        hyperGateClient = new HyperGateClient("localhost", SERVER_PORT, 10);
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

    @Test
    public void testSend() {
        boolean result = false;
        try{
            hyperGateClient.send(null);
        }catch (IOException ex){
            result = true;
        }
        assertTrue(result);
    }

    @Test
    public void testShutdown() throws Exception {
        assertTrue(hyperGateClient.isRunning());

        hyperGateClient.shutdown();

        assertFalse(hyperGateClient.isRunning());
        assertFalse(hyperGateClient.isOccupied());
    }

    @Test
    public void testRestart() throws Exception {
        assertTrue(hyperGateClient.isRunning());
        hyperGateClient.restart();
        assertTrue(hyperGateClient.isRunning());
    }

    @Test
    public void testRestart1() throws Exception {
        assertTrue(hyperGateClient.isRunning());
        hyperGateClient.restart("localhost", SERVER_PORT, 1);
        assertTrue(hyperGateClient.isRunning());
    }

    @Test
    public void testGetName() throws Exception {
        LOG.info("hyperGateClient name:" + hyperGateClient.getName());
        assertEquals("client-4a68687c-7a85-4943-bb18-e2eda86905dd".length(), hyperGateClient.getName().length());
        assertTrue(hyperGateClient.getName().indexOf("client-") == 0);
    }

    @Test
    public void testStart() throws Exception {
        hyperGateClient.shutdown();
        assertFalse(hyperGateClient.isRunning());

        hyperGateClient.start();
        assertTrue(hyperGateClient.isRunning());
    }
}