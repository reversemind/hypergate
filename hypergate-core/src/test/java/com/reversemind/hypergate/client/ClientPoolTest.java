package com.reversemind.hypergate.client;

import com.reversemind.hypergate.proxy.ProxyFactory;
import com.reversemind.hypergate.server.SimpleServer;
import com.reversemind.hypergate.shared.ISimpleService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.*;

/**
 */
public class ClientPoolTest {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ClientPoolTest.class);

    private SimpleServer simpleServer;

    /**
     * Start SimpleServer for all these tests
     *
     */
    @Before
    public void init(){
        simpleServer = new SimpleServer();
        simpleServer.init();
    }

    @After
    public void destroy() throws InterruptedException {
        if(simpleServer != null){
            Thread.sleep(1000);
            simpleServer.destroy();
        }
    }

    @Test
    public void testNoClientPool() throws Exception {

        SimpleClient simpleClient = new SimpleClient();
        simpleClient.init();

        ISimpleService simpleService = (ISimpleService) ProxyFactory.getInstance().newProxyInstance(simpleClient.getHyperGateClient(),ISimpleService.class);
        LOG.info("Result from server:" + simpleService.getSimpleValue("1111111111111"));

        Thread.sleep(1000);
        simpleClient.destroy();
    }

    @Test
    public void testLoop() throws Exception {
        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);


        for(int j=0;j<10;j++){
            ClientPool clientPool = new ClientPool(clientPoolFactory,2);

            int count = 10;
            for(int i=0; i<count; i++){
                IHyperGateClient hyperGateClient = clientPool.borrowObject();
                LOG.info("Borrowed client" + hyperGateClient.getName());
                LOG.info("Pool metrics:" + clientPool.printPoolMetrics());
                Thread.sleep(500);
            }

            clientPool.forceClearClose();

            clientPool.clear();
            clientPool.close();

            clientPool = null;
            Thread.sleep(1000);
        }

        Thread.sleep(5000);

    }

    @Test
    public void testOne() throws Exception {

        // String contextXML, String beanName, Class<? extends IHyperGateClient> clientClazz
        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);

        ClientPool clientPool = new ClientPool(clientPoolFactory);

        IHyperGateClient hyperGateClient_ = null;
        int count = 3;
        for(int i=0; i<count; i++){
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

        if(!clientPool.isClosed()){
            clientPool.returnObject(hyperGateClient_);
        }

        clientPool = null;




        Thread.sleep(2000);
        clientPool = new ClientPool(clientPoolFactory);


        hyperGateClient_ = null;
        count = 3;
        for(int i=0; i<count; i++){
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

        System.out.println("DONE");
    }

//    @Test
//    public void testClientPool() throws Exception {
//        ClientPoolFactory testClientFactory = new ClientPoolFactory("META-INF/hypergate-client-context.xml", "hyperGateClientServerDiscovery", HyperGateClientServerDiscovery.class);
//        GenericObjectPool2<IHyperGateClient> pool = new GenericObjectPool2<IHyperGateClient>(testClientFactory, 5);
//
//        LOG.debug("pool.getMaxActive():" + pool.getMaxActive());
//        LOG.debug("pool.getMaxIdle():" + pool.getMaxIdle());
//        LOG.debug("pool.getNumActive():" + pool.getNumActive());
//        LOG.debug("pool.getNumIdle():" + pool.getNumIdle());
//
//        Thread.sleep(20000);
//
//        IHyperGateClient hyperGateClient = pool.borrowObject();
//
//        LOG.debug("IHyperGateClient hyperGateClient - " + hyperGateClient);
//        ProxyFactory proxyFactory = ProxyFactory.getInstance();
//
//        LOG.debug("pool.getMaxActive():" + pool.getMaxActive());
//        LOG.debug("pool.getMaxIdle():" + pool.getMaxIdle());
//        LOG.debug("pool.getNumActive():" + pool.getNumActive());
//        LOG.debug("pool.getNumIdle():" + pool.getNumIdle());
//
//        IServiceSimple proxyService = (IServiceSimple) proxyFactory.newProxyInstance(hyperGateClient, IServiceSimple.class);
//        LOG.debug("proxyService: " + proxyService.functionNumber1("1", "2"));
//
//        pool.returnObject(hyperGateClient);
//
//        LOG.debug("pool.getMaxActive():" + pool.getMaxActive());
//        LOG.debug("pool.getMaxIdle():" + pool.getMaxIdle());
//        LOG.debug("pool.getNumActive():" + pool.getNumActive());
//        LOG.debug("pool.getNumIdle():" + pool.getNumIdle());
//
//    }

    @Test
    public void testGetClientPoolFactory() throws Exception {

    }

    @Test
    public void testPrintPoolMetrics() throws Exception {

    }
}
