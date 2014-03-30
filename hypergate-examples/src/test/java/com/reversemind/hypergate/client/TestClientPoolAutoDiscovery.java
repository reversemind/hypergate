package com.reversemind.hypergate.client;

import com.reversemind.hypergate.integration.pojo.client.ClientPOJODiscovery;
import com.reversemind.hypergate.integration.pojo.server.ServerPojoAdvertiser;
import com.reversemind.hypergate.proxy.ProxyFactoryPool;
import com.reversemind.hypergate.server.SimpleServer;
import com.reversemind.hypergate.shared.ISimpleService;
import com.reversemind.hypergate.zookeeper.EmbeddedZookeeper;
import com.reversemind.hypergate.zookeeper.StartEmbeddedZookeeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.CLASS_HYPERGATE_CLIENT;
import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.CLIENT_DEFAULT_CONTEXT_NAME;
import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.CLIENT_SIMPLE_BUILDER_NAME;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 *
 */
public class TestClientPoolAutoDiscovery extends StartEmbeddedZookeeper {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TestClientPoolAutoDiscovery.class);

    private ServerPojoAdvertiser serverPojoAdvertiser;

    @Override
    @Before
    public void init(){
        // start ZookeeperServer
        super.init();
        serverPojoAdvertiser = new ServerPojoAdvertiser();
        serverPojoAdvertiser.init();
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

        List<String> list = new ArrayList<String>();
        for(int i=0; i<1;i++){
            ISimpleService simpleService = clientPOJODiscovery.getProxy(ISimpleService.class);
            assertNotNull(simpleService);

            String value = simpleService.getSimpleValue("1111--" + i);
            LOG.info("Get value from remote server :" + i + " value:" + value);
            list.add(value);
            assertNotNull(value);

        }


        LOG.info("\n\n\n");
        int count = 0;
        for(String string: list){
            LOG.info( count++ + " -- " + string);
        }

        clientPOJODiscovery.destroy();
    }

    @Test
    public void testClientPoolMultiThread() throws InterruptedException, ExecutionException {

        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);
        ClientPool clientPool = new ClientPool(clientPoolFactory,1000);

        final int THREAD_SIZE = 100;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);

        List<FutureTask<String>> list = new ArrayList<FutureTask<String>>();
        for(int i =0; i<THREAD_SIZE; i++){
            list.add(new FutureTask<String>(new ClientProcess<String>("THREAD="+(i+1)+"=", ProxyFactoryPool.getInstance(), clientPool, ISimpleService.class)));
        }

        long bT = System.currentTimeMillis();

        for(FutureTask<String> futureTask: list){
            executor.execute(futureTask);
        }

        int readyCount = 0;
        while(readyCount != THREAD_SIZE){
            readyCount = 0;
            for(FutureTask<String> futureTask: list){
                if(futureTask.isDone()){
                    readyCount++;
                }
            }

            Thread.sleep(5);
        }
        System.out.println("\n\n\n\n BEFORE SLEEP  \n\n\n\n");





        Thread.sleep(6000);
        System.out.println("\n\n\n\n AFTER SLEEP  \n\n\n\n");
        executor.shutdown();

        clientPool.forceClearClose();
        clientPool.close();
        clientPool = null;

        long eT = System.currentTimeMillis();

        for(FutureTask<String> futureTask: list){
            System.out.println("" + futureTask.get());
        }

        System.out.println("\n\n\n\n - Spend time:" + ((eT - bT)-6000 ) + " per thread:" + ((eT-bT)-6000 )/THREAD_SIZE+ " ms\n\n\n\n");

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
            try{
                for(int i=0; i<10;i++){
                    ISimpleService simpleService = (ISimpleService) proxyFactoryPool.newProxyInstance(clientPool, interfaceClass);
                    String v = (String) simpleService.getSimpleValue(name + "1111--" + i);
                    LOG.info("\n\n\n\nFROM SERVER --- " + v + "\n\n\n");
                    all.append("\n").append(v);
                    Thread.sleep(1);
                }

            }   catch (Exception ex){
                System.out.println("CATCH IN CALL -- "+ name);
            }

            return (String)("---- " + name + " -- " + all.toString());
        }

    }




}
