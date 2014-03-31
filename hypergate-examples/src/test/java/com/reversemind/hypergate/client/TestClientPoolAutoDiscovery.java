package com.reversemind.hypergate.client;

import com.reversemind.hypergate.integration.pojo.client.ClientPOJODiscovery;
import com.reversemind.hypergate.integration.pojo.server.ServerPojoAdvertiser;
import com.reversemind.hypergate.shared.IExampleSimpleService;
import com.reversemind.hypergate.zookeeper.EmbeddedZookeeper;
import com.reversemind.hypergate.zookeeper.StartEmbeddedZookeeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class TestClientPoolAutoDiscovery extends StartEmbeddedZookeeper {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TestClientPoolAutoDiscovery.class);

    private ServerPojoAdvertiser serverPojoAdvertiser;

    @Override
    @Before
    public void init(){
        System.setProperty("java.net.preferIPv4Stack", "true");
        // start ZookeeperServer
        super.init();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.error("Could not wait for Zookeeper", e);
        }
        serverPojoAdvertiser = new ServerPojoAdvertiser();
        serverPojoAdvertiser.init();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.error("Could not wait for Zookeeper", e);
        }
        LOG.info("ZOOKEEPER TEMP DIRECTORY = " + EmbeddedZookeeper.EMBEDDED_ZOOKEEPER_DIRECTORY);
    }

    @Override
    @After
    public void shutDown() throws InterruptedException {
        if(serverPojoAdvertiser != null){
            Thread.sleep(1000);
            serverPojoAdvertiser.destroy();
        }
        super.shutDown();
    }

    @Test
    public void testServerDiscovery(){

        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_CONNECTION_STRING, new ExponentialBackoffRetry(500, 3));
            client.start();

            // /baloo/services/HYPERGATE.ADDRESS
            List<String> list = client.getChildren().forPath(SERVICE_BASE_PATH + "/" + SERVICE_HYPER_GATE_NAME);
            LOG.info(" children -- " + list);
            assertNotNull(list);
            assertTrue(list.size()>0);
            assertEquals(list.get(0), serverPojoAdvertiser.getServer().getInstanceName());

            client.close();

            LOG.info("Server successfully Advertised");
        } catch (Exception e) {
            LOG.warn("NODE EXIST", e);
        }

    }

    @Test
    public void testClientFindServer() throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");

        ClientPOJODiscovery clientPOJODiscovery = new ClientPOJODiscovery();
        clientPOJODiscovery.init();

//        for(int j=0; j<10; j++){
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 1; i++) {
            IExampleSimpleService simpleService = clientPOJODiscovery.getProxy(IExampleSimpleService.class);
            assertNotNull(simpleService);

            String value = simpleService.getSimpleValue("1111--" + i);
            LOG.info("Get value from remote server :" + i + " value:" + value);
            list.add(value);
            assertNotNull(value);

        }

        LOG.info("\n\n\n");
        int count = 0;
        for (String string : list) {
            LOG.info("SCAN: = " + count++ + " -- " + string);
        }
        Thread.sleep(100);

//            LOG.info("\n\n\n\n ===== ["+j+"] ===STEP======================================================================\n\n\n" );
//        }

        clientPOJODiscovery.destroy();
    }

    @Test
    public void testClientAutoDiscoverServerViaZookeeper() throws InterruptedException, ExecutionException {

        final long JUST_SLEEP = 5000;

        System.setProperty("java.net.preferIPv4Stack", "true");

        ClientPOJODiscovery clientPOJODiscovery = new ClientPOJODiscovery();
        clientPOJODiscovery.init();



        for(int k=0; k<10000; k++){

            LOG.info("\n\n\n\n*********[" + k + "]*******************************************\n\n\n\n");

            final int THREAD_SIZE = 10;
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);

            List<FutureTask<String>> list = new ArrayList<FutureTask<String>>();
            for (int i = 0; i < THREAD_SIZE; i++) {
                list.add(new FutureTask<String>(new ClientProcess<String>("THREAD=" + (i + 1) + "=", clientPOJODiscovery, IExampleSimpleService.class)));
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

                Thread.sleep(1);
            }
            LOG.info("\n\n\n\n BEFORE SLEEP  \n\n\n\n");

    //        Thread.sleep(JUST_SLEEP);

            LOG.info("\n\n\n\n AFTER SLEEP  \n\n\n\n");
            executor.shutdown();
            long eT = System.currentTimeMillis();

            for (FutureTask<String> futureTask : list) {
                LOG.info("TASK RESULT:" + futureTask.get());
            }

            LOG.info("\n\n\n\n - Spend time:" + ((eT - bT) - JUST_SLEEP) + " per thread:" + ((eT - bT) - JUST_SLEEP) / THREAD_SIZE + " ms\n\n\n\n");
        }
        clientPOJODiscovery.destroy();
    }

    /**
     *
     * @param <String>
     */
    private class ClientProcess<String> implements Callable<String> {

        String name;

        ClientPOJODiscovery clientPOJODiscovery;
        Class interfaceClass;

        ClientProcess(String name, ClientPOJODiscovery clientPOJODiscovery, Class interfaceClass) {
            this.name = name;
            this.clientPOJODiscovery = clientPOJODiscovery;
            this.interfaceClass = interfaceClass;
        }

        @Override
        public String call() throws Exception {

            StringBuffer all = new StringBuffer();
            try {
                for (int i = 0; i < 1; i++) {
                    IExampleSimpleService simpleService = (IExampleSimpleService) clientPOJODiscovery.getProxy(interfaceClass);
                    String v = (String) simpleService.getSimpleValue(name + "1111--" + i);
                    LOG.info("\n\n\n\nFROM SERVER --- " + v + "\n\n\n");
                    all.append("\n").append(v);
                    Thread.sleep(1);
                }

            } catch (Exception ex) {
                all.append("\n").append("EXCEPTION in getting for IExampleSimpleService");
                LOG.error("CATCH IN CALL -- " + name, ex);
            }

            return (String)("---- " + name + " -- " + all.toString());
        }

    }

//    @Test
//    public void testClientPoolMultiThread() throws InterruptedException, ExecutionException {
//
//        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);
//        ClientPool clientPool = new ClientPool(clientPoolFactory,1000);
//
//        final int THREAD_SIZE = 2;
//        ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);
//
//        List<FutureTask<String>> list = new ArrayList<FutureTask<String>>();
//        for(int i =0; i<THREAD_SIZE; i++){
//            list.add(new FutureTask<String>(new ClientProcess<String>("THREAD="+(i+1)+"=", ProxyFactoryPool.getInstance(), clientPool, IExampleSimpleService.class)));
//        }
//
//        long bT = System.currentTimeMillis();
//
//        for(FutureTask<String> futureTask: list){
//            executor.execute(futureTask);
//        }
//
//        int readyCount = 0;
//        while(readyCount != THREAD_SIZE){
//            readyCount = 0;
//            for(FutureTask<String> futureTask: list){
//                if(futureTask.isDone()){
//                    readyCount++;
//                }
//            }
//
//            Thread.sleep(5);
//        }
//        System.out.println("\n\n\n\n BEFORE SLEEP  \n\n\n\n");
//
//
//
//
//
//        Thread.sleep(6000);
//        System.out.println("\n\n\n\n AFTER SLEEP  \n\n\n\n");
//        executor.shutdown();
//
//        clientPool.forceClearClose();
//        clientPool.close();
//        clientPool = null;
//
//        long eT = System.currentTimeMillis();
//
//        for(FutureTask<String> futureTask: list){
//            System.out.println("" + futureTask.get());
//        }
//
//        System.out.println("\n\n\n\n - Spend time:" + ((eT - bT)-6000 ) + " per thread:" + ((eT-bT)-6000 )/THREAD_SIZE+ " ms\n\n\n\n");
//
//    }





}
