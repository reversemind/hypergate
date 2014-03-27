package com.reversemind.hypergate.client;

import com.reversemind.hypergate.server.SimpleServer;

import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.CLASS_HYPERGATE_CLIENT;
import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.CLIENT_DEFAULT_CONTEXT_NAME;
import static com.reversemind.hypergate.client.AbstractContainerHyperGateClient.CLIENT_SIMPLE_BUILDER_NAME;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 3/27/14
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Go {

    public static void main(String... args) throws Exception {

        SimpleServer simpleServer = new SimpleServer();
        simpleServer.init();


        // String contextXML, String beanName, Class<? extends IHyperGateClient> clientClazz
        ClientPoolFactory clientPoolFactory = new ClientPoolFactory(CLIENT_DEFAULT_CONTEXT_NAME, CLIENT_SIMPLE_BUILDER_NAME, CLASS_HYPERGATE_CLIENT);

        ClientPool clientPool = new ClientPool(clientPoolFactory,5);

        int count = 5;
        for(int i=0; i<count; i++){
            IHyperGateClient hyperGateClient = clientPool.borrowObject();
            System.out.println("Borrowed client" + hyperGateClient.getName());
            System.out.println("Pool metrics:" + clientPool.printPoolMetrics());
        }


        clientPool.clear();
        clientPool.close();
        clientPool = null;

        if(simpleServer != null){
            Thread.sleep(1000);
            simpleServer.destroy();
        }
    }
}
