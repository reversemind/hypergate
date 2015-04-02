/**
 * Copyright (c) 2013-2015 Eugene Kalinin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.reversemind.hypergate.client;

import com.reversemind.hypergate.proxy.ProxyFactory;
import com.reversemind.hypergate.proxy.ProxyFactoryPool;
import com.reversemind.hypergate.server.SimpleServer;
import com.reversemind.hypergate.shared.ISimpleService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.*;
import static junit.framework.Assert.assertEquals;

/**
 */
public class ClientPoolTest {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ClientPoolTest.class);

    private SimpleServer simpleServer;

    /**
     * Start SimpleServer for all these tests
     */
    @Before
    public void init() {
        simpleServer = new SimpleServer();
        simpleServer.init();
    }

    @After
    public void destroy() throws InterruptedException {
        if (simpleServer != null) {
            Thread.sleep(1000);
            simpleServer.destroy();
        }
    }

    /**
     * Test & Example of the simplest interaction test Client & Server
     *
     * @throws Exception
     */
    @Test
    public void testNoClientPool() throws Exception {

        SimpleClient simpleClient = new SimpleClient();
        simpleClient.init();

        ISimpleService simpleService = (ISimpleService) ProxyFactory.getInstance().newProxyInstance(simpleClient.getHyperGateClient(), ISimpleService.class);

        final String sendFromClient = "sendFromClient";
        final String receivedFromServer = simpleService.getSimpleValue(sendFromClient);
        LOG.info("Result from server:" + receivedFromServer);

        Thread.sleep(1000);
        simpleClient.destroy();

        assertEquals(ISimpleService.RETURN_VALUE + sendFromClient, receivedFromServer);
    }

    /**
     * Using pool of clients
     */
    @Test
    public void testClientPool() {

        int numberOfThreads = 2;

        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);
        ClientPool clientPool = new ClientPool(clientPoolFactory, numberOfThreads);

        for (int i = 0; i < 100; i++) {
            ISimpleService simpleService = (ISimpleService) ProxyFactoryPool.getInstance().newProxyInstance(clientPool, ISimpleService.class);

            final String receivedFromServer = simpleService.getSimpleValue("number-" + i);
            assertEquals(ISimpleService.RETURN_VALUE + "number-" + i, receivedFromServer);
            LOG.info("\n\n\n\nFROM SERVER --- " + simpleService.getSimpleValue("number-" + i) + "\n\n\n");
        }

        clientPool.forceClearClose();
        clientPool.close();
        clientPool = null;

    }

    /**
     * Using Client Pool under multi thread - 100 threads for 2 client pool
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testClientPoolMultiThread() throws InterruptedException, ExecutionException {

        final int numberOfThreads = 2;

        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);
        ClientPool clientPool = new ClientPool(clientPoolFactory, numberOfThreads);

        final int THREAD_SIZE = 100;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);

        List<FutureTask<String>> list = new ArrayList<FutureTask<String>>();
        for (int i = 0; i < THREAD_SIZE; i++) {
            list.add(new FutureTask<String>(new ClientProcess<String>("THREAD=" + (i + 1) + "=", ProxyFactoryPool.getInstance(), clientPool, ISimpleService.class)));
        }

        long bT = System.currentTimeMillis();

        for (FutureTask<String> futureTask : list) {
            executor.execute(futureTask);
        }

        int readyCount = 0;
        while (readyCount != THREAD_SIZE) {
            readyCount = 0;
            for (FutureTask<String> futureTask : list) {
                if (futureTask.isDone()) {
                    readyCount++;
                }
            }

            Thread.sleep(5);
        }
        LOG.info("\n\n\n\n BEFORE SLEEP  \n\n\n\n");


        Thread.sleep(6000);
        LOG.info("\n\n\n\n AFTER SLEEP  \n\n\n\n");
        executor.shutdown();



        long eT = System.currentTimeMillis();

        for (FutureTask<String> futureTask : list) {
            LOG.info("" + futureTask.get());
        }

        LOG.info("\n\n\n\n - Spend time:" + ((eT - bT) - 6000) + " per thread:" + ((eT - bT) - 6000) / THREAD_SIZE + " ms\n\n\n\n");

        clientPool.forceClearClose();
        clientPool.close();
        clientPool = null;

    }

    public class ClientProcess<String> implements Callable<String> {

        String name;

        ProxyFactoryPool proxyFactoryPool;
        ClientPool clientPool;
        Class interfaceClass;

        ClientProcess(String name, ProxyFactoryPool proxyFactoryPool, ClientPool clientPool, Class interfaceClass) {
            this.name = name;
            this.proxyFactoryPool = proxyFactoryPool;
            this.clientPool = clientPool;
            this.interfaceClass = interfaceClass;
        }

        @Override
        public String call() throws Exception {

            StringBuffer all = new StringBuffer();
            try {
                for (int i = 0; i < 10; i++) {
                    ISimpleService simpleService = (ISimpleService) proxyFactoryPool.newProxyInstance(clientPool, interfaceClass);
                    String v = (String) simpleService.getSimpleValue(name + "call()--" + i);
                    LOG.info("\n\n\n\nFROM SERVER --- " + v + "\n\n\n");
                    all.append("\n").append(v);
                    Thread.sleep(1);
                }

            } catch (Exception ex) {
                LOG.error("CATCH IN CALL -- " + name);
            }

            return (String) ("---- " + name + " -- " + all.toString());
        }

    }

    /**
     * Test sequence for create ClientPool and Destroy of ClientPool
     * @throws Exception
     */
    @Test
    public void testLoop() throws Exception {
        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);

        int count = 10;

        for (int j = 0; j < 2; j++) {
            ClientPool clientPool = new ClientPool(clientPoolFactory, count);

            for (int i = 0; i < count; i++) {
                IHyperGateClient hyperGateClient = clientPool.borrowObject();
                LOG.info("Borrowed client" + hyperGateClient.getName());
                LOG.info("Pool metrics:" + clientPool.printPoolMetrics());

//                clientPool.returnObject(hyperGateClient);
            }

            clientPool.forceClearClose();

            clientPool.clear();
            clientPool.close();

            clientPool = null;
            Thread.sleep(500);
        }

        Thread.sleep(3000);

    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testOne() throws Exception {

        // String contextXML, String beanName, Class<? extends IHyperGateClient> clientClazz
        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);

        ClientPool clientPool = new ClientPool(clientPoolFactory);

        IHyperGateClient hyperGateClient_ = null;
        int count = 3;
        for (int i = 0; i < count; i++) {
            IHyperGateClient hyperGateClient = clientPool.borrowObject();
            LOG.info("Borrowed client" + hyperGateClient.getName());
            LOG.info("Pool metrics:" + clientPool.printPoolMetrics());
            hyperGateClient_ = hyperGateClient;
        }

        clientPool.returnObject(hyperGateClient_);

        clientPool.forceClearClose();
        clientPool.clear();
        clientPool.close();

        clientPool.isClosed();

        if (!clientPool.isClosed()) {
            clientPool.returnObject(hyperGateClient_);
        }

        clientPool = null;

        Thread.sleep(2000);
        clientPool = new ClientPool(clientPoolFactory);

        hyperGateClient_ = null;
        count = 3;
        for (int i = 0; i < count; i++) {
            IHyperGateClient hyperGateClient = clientPool.borrowObject();
            LOG.info("Borrowed client" + hyperGateClient.getName());
            LOG.info("Pool metrics:" + clientPool.printPoolMetrics());
            hyperGateClient_ = hyperGateClient;
        }

        clientPool.returnObject(hyperGateClient_);
        clientPool.forceClearClose();
        clientPool.clear();
        clientPool.close();

        Thread.sleep(2000);
        LOG.info("DONE");
    }

}
