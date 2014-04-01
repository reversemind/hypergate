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
    static final String SPLITTER = "-splitter-";

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

    /**
     * THREAD=5-split-0
     * SPLITTER -split-
     *
     * @param splitterString
     * @return
     */
    private long resultCount(String splitterString){
        long _result = 0;

        if(splitterString != null && splitterString.length()>0){

            String[] strings = splitterString.split("\n");
            if(strings.length >0){

                for(String valueString: strings){
                    String[] value = valueString.split(SPLITTER);
                    if(value != null && value.length>=2){
                        try{
                            _result += Long.parseLong(value[1].trim());
                        }catch (Exception ex){

                        }
                    }
                }

            }
        }

        return _result;
    }

    @Test
    public void testCountResultSplitter(){

        String result = "THREAD=1=" + SPLITTER + "1\n";
        result += "THREAD=1=" + SPLITTER + "2\n";
        result += "THREAD=1=" + SPLITTER + "3\n";
        result += "THREAD=1=" + SPLITTER + "4\n";
        result += "THREAD=1=" + SPLITTER + "5\n";

        LOG.info("SUM result:" + this.resultCount(result));

    }

    private long summCounter(long maxIndex){
        long _result = 0;
        for(int i=1; i<maxIndex+1; i++){
            _result += 1;
        }
        return _result;
    }

    @Test
    public void testClientAutoDiscoverServerViaZookeeper() throws InterruptedException, ExecutionException {

        final long JUST_SLEEP = 5000;

        // final int LOOP_SIZE = 1000;
        // THREAD_SIZE 10 - 200 JVM  1 min
        // THREAD_SIZE 20 - 360 JVM  1 min 30 sec
        // THREAD_SIZE 30 - 520 JVM  2 min
        final int THREAD_SIZE = 30;
        final int LOOP_SIZE = 1000;
        final int INNER_LOOP_SIZE = 1;

        long threadSummResult = 0;
        final long RIGHT_VALUE = summCounter(INNER_LOOP_SIZE) * THREAD_SIZE * LOOP_SIZE;
        LOG.info("RIGHT_VALUE:" + RIGHT_VALUE);

        System.setProperty("java.net.preferIPv4Stack", "true");

        ClientPOJODiscovery clientPOJODiscovery = new ClientPOJODiscovery();
        clientPOJODiscovery.init();

        for(int k=0; k<LOOP_SIZE; k++){

            LOG.info("\n\n\n\n*********[" + k + "]*******************************************\n\n\n\n");


            ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);

            List<FutureTask<String>> list = new ArrayList<FutureTask<String>>();
            for (int i = 0; i < THREAD_SIZE; i++) {
                list.add(new FutureTask<String>(new ClientProcess<String>("THREAD=" + (i + 1) + "=", clientPOJODiscovery, IExampleSimpleService.class, INNER_LOOP_SIZE)));
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
                LOG.info("TASK RESULT:" + futureTask.get() + "| SUMM:" + this.resultCount(futureTask.get()));
                threadSummResult += this.resultCount(futureTask.get());
            }

            LOG.info("\n\n\n\n - Spend time:" + ((eT - bT) - JUST_SLEEP) + " per thread:" + ((eT - bT) - JUST_SLEEP) / (THREAD_SIZE * INNER_LOOP_SIZE) + " ms\n\n\n\n");
        }

        clientPOJODiscovery.destroy();

        LOG.info("\n\nthreadSummResult = " + threadSummResult + " RIGHT_VALUE:" + RIGHT_VALUE + "\n\n");
        LOG.info("DELTA:" + (RIGHT_VALUE - threadSummResult) + "  -- %" + ((double)(100.0-(1.0 * threadSummResult * 100.0 / (1.0 * RIGHT_VALUE)))) );
        assertEquals(threadSummResult, RIGHT_VALUE);
    }

    /**
     *
     * @param <String>
     */
    private class ClientProcess<String> implements Callable<String> {

        String name;

        ClientPOJODiscovery clientPOJODiscovery;
        Class interfaceClass;
        int innerLoopSize = 1;

        ClientProcess(String name, ClientPOJODiscovery clientPOJODiscovery, Class interfaceClass, int innerLoopSize) {
            this.name = name;
            this.clientPOJODiscovery = clientPOJODiscovery;
            this.interfaceClass = interfaceClass;
            this.innerLoopSize = innerLoopSize;
        }

        @Override
        public String call() throws Exception {

            StringBuffer result = new StringBuffer();
            try {
                for (int i = 0; i < innerLoopSize; i++) {
                    IExampleSimpleService simpleService = (IExampleSimpleService) clientPOJODiscovery.getProxy(interfaceClass);
                    String v = (String) simpleService.getSimpleValue(name + SPLITTER + (1));
//                    LOG.info("\n\n\n\nFROM SERVER --- " + v + "\n\n\n");
                    result.append("\n").append(v);
                    Thread.sleep(1);
                }

            } catch (Exception ex) {
                result.append("\n").append("EXCEPTION in getting for IExampleSimpleService");
                LOG.error("CATCH IN CALL -- " + name, ex);
            }

            return (String)result.toString();
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
