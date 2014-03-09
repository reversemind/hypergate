package com.test.test;

import com.reversemind.glia.client.ClientPool;
import com.reversemind.glia.client.ClientPoolFactory;
import com.reversemind.glia.client.GliaClientServerDiscovery;
import com.reversemind.glia.client.IGliaClient;
import com.reversemind.glia.proxy.ProxyFactory;
import com.test.pool.ClientFactory;
import ejb.client.ClientSimple;
import ejb.server.ServerSimple;
import ejb.server.service.ServiceSimple;
import ejb.shared.IServiceSimple;
import ejb.zookeeper.RunZookeeper;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 *
 * Copyright (c) 2013 Eugene Kalinin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(Arquillian.class)
public class ClientServerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClientServerTest.class);

    @Inject
    IServiceSimple simpleService;

    @Inject
    ClientSimple clientSimple;

    @Deployment
    @TargetsContainer("jbossas-managed")
    public static Archive<?> createDeployment() {
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

        // Не стучимся на remote репы
        resolver.goOffline();

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "ServerTest.war")


                // tip:
                // .artifact("GROUPID:ARTIFACTID:TYPE:VERSION")
                .addAsLibraries(resolver

                        .artifact("commons-pool:commons-pool:1.6")

                        .artifact("org.springframework:spring-core:3.0.7.RELEASE")
                        .artifact("org.springframework:spring-context:3.0.7.RELEASE")

                        .artifact("postgresql:postgresql:9.1-901.jdbc4")

                        .artifact("org.apache.commons:commons-lang3:3.1")

                        .artifact("log4j:log4j:1.2.16")

                        .artifact("com.reversemind:glia-core:1.9.2-SNAPSHOT")

                        .artifact("net.sf.dozer:dozer:5.4.0")
                        .artifact("com.google.code.gson:gson:2.2.4")
                        .artifact("com.google.guava:guava:14.0.1")

                        .resolveAsFiles())

                .addPackages(true, IServiceSimple.class.getPackage())
                .addPackages(true, ServiceSimple.class.getPackage())
                .addPackages(true, ServerSimple.class.getPackage())
                .addPackages(true, RunZookeeper.class.getPackage())
                .addPackages(true, ClientSimple.class.getPackage())
                .addPackages(true, ClientFactory.class.getPackage())

                .addAsResource("META-INF/glia-interface-map.xml", "META-INF/glia-interface-map.xml")
                .addAsResource("META-INF/glia-server-context.xml", "META-INF/glia-server-context.xml")
                .addAsResource("META-INF/glia-server.properties", "META-INF/glia-server.properties")

                .addAsResource("META-INF/glia-client-context.xml", "META-INF/glia-client-context.xml")
                .addAsResource("META-INF/glia-client.properties", "META-INF/glia-client.properties")

                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        LOG.debug("archive:" + archive.toString(true));
        return archive;
    }

    @Test
    public void testClientPoolFactory() throws Exception {
        ClientPoolFactory clientPoolFactory = new ClientPoolFactory("META-INF/glia-client-context.xml", "gliaClientServerDiscovery", GliaClientServerDiscovery.class);
        ClientPool clientPool = new ClientPool(clientPoolFactory);

        LOG.debug(clientPool.printPoolMetrics());
        IGliaClient gliaClient = clientPool.borrowObject();

        LOG.debug(clientPool.printPoolMetrics());
    }

    @Test
    public void testPoolSize() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/glia-client-context.xml");
        int poolSize = applicationContext.getBean("poolSize", java.lang.Integer.class);
        LOG.debug("Pool size:" + poolSize);
    }


    @Test
    public void testClientPool() throws Exception {
        ClientFactory clientFactory = new ClientFactory("META-INF/glia-client-context.xml", "gliaClientServerDiscovery", GliaClientServerDiscovery.class);
        GenericObjectPool<IGliaClient> pool = new GenericObjectPool<IGliaClient>(clientFactory, 5);

        LOG.debug("pool.getMaxActive():" + pool.getMaxActive());
        LOG.debug("pool.getMaxIdle():" + pool.getMaxIdle());
        LOG.debug("pool.getNumActive():" + pool.getNumActive());
        LOG.debug("pool.getNumIdle():" + pool.getNumIdle());

        Thread.sleep(20000);

        IGliaClient gliaClient = pool.borrowObject();

        LOG.debug("IGliaClient gliaClient - " + gliaClient);
        ProxyFactory proxyFactory = ProxyFactory.getInstance();

        LOG.debug("pool.getMaxActive():" + pool.getMaxActive());
        LOG.debug("pool.getMaxIdle():" + pool.getMaxIdle());
        LOG.debug("pool.getNumActive():" + pool.getNumActive());
        LOG.debug("pool.getNumIdle():" + pool.getNumIdle());

        IServiceSimple proxyService = (IServiceSimple) proxyFactory.newProxyInstance(gliaClient, IServiceSimple.class);
        LOG.debug("proxyService: " + proxyService.functionNumber1("1", "2"));

        pool.returnObject(gliaClient);

        LOG.debug("pool.getMaxActive():" + pool.getMaxActive());
        LOG.debug("pool.getMaxIdle():" + pool.getMaxIdle());
        LOG.debug("pool.getNumActive():" + pool.getNumActive());
        LOG.debug("pool.getNumIdle():" + pool.getNumIdle());

    }

    @Test
    public void testSimple() throws InterruptedException {
        LOG.debug("############");
        LOG.debug("info:" + simpleService.functionNumber1("1", "2"));
    }

    @Test
    public void testClient() throws Exception {
        IServiceSimple proxyService = clientSimple.getProxy(IServiceSimple.class);
        LOG.debug("proxyService: " + proxyService.functionNumber1("1", "2"));
    }

    @Test
    public void testMutliThreaded() throws Exception {

        // Number of threads
        final int size = 10;

        LOG.debug("clientSimple1:" + clientSimple);

        IServiceSimple proxyService = clientSimple.getProxy(IServiceSimple.class);
        LOG.debug("proxyService:" + proxyService);


        List<ClientCallable> clientCallableList = new ArrayList<ClientCallable>();

        for (int i = 0; i < size; i++) {
            clientCallableList.add(new ClientCallable(proxyService, i));
        }

        List<FutureTask<String>> futureTaskList = new ArrayList<FutureTask<String>>();
        for (ClientCallable clientCallable : clientCallableList) {
            futureTaskList.add(new FutureTask<String>(clientCallable));
        }

        long beginTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(futureTaskList.size());
        for (FutureTask<String> futureTask : futureTaskList) {
            executor.execute(futureTask);
        }

        boolean ready = false;
        int[] dones = new int[futureTaskList.size()];
        String[] writes = new String[futureTaskList.size()];

        int indexValue = 0;
        while (!ready) {

            int count = 0;
            indexValue = 0;
            for (FutureTask<String> futureTask : futureTaskList) {
                if (futureTask.isDone() & dones[indexValue] == 0) {
                    writes[indexValue] = futureTask.get();
                    dones[indexValue] = 1;
                }
                indexValue++;
            }

            for (int k = 0; k < dones.length; k++) {
                if (dones[k] == 1) {
                    count++;
                }
            }

            if (count == futureTaskList.size()) {
                ready = true;
            }

//            Thread.sleep(500);
        }

        LOG.debug("\n\n\n ====== DONE ====== ");
        LOG.debug("  time:" + (System.currentTimeMillis() - beginTime) + "ms\n\n");
        executor.shutdown();

        for (int i = 0; i < writes.length; i++) {
            LOG.debug("- " + writes[i]);
        }
        LOG.debug("\n\n\n ====== DONE ====== \n\n");
    }


    @Test
    public void testMutliThreadProxyClient() throws Exception {

        // Number of threads
        final int size = 20;

        LOG.debug("clientSimple1:" + clientSimple);

        List<IServiceSimple> serviceSimpleList = new ArrayList<IServiceSimple>();
        for (int i = 0; i < size; i++) {
            IServiceSimple proxyService = clientSimple.getProxy(IServiceSimple.class);
            LOG.debug("proxyService:" + proxyService);
            serviceSimpleList.add(proxyService);
        }

        List<ClientCallable> clientCallableList = new ArrayList<ClientCallable>();

        for (int i = 0; i < size; i++) {
            clientCallableList.add(new ClientCallable(serviceSimpleList.get(i), i));
        }

        List<FutureTask<String>> futureTaskList = new ArrayList<FutureTask<String>>();
        for (ClientCallable clientCallable : clientCallableList) {
            futureTaskList.add(new FutureTask<String>(clientCallable));
        }

        long beginTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(futureTaskList.size());
        for (FutureTask<String> futureTask : futureTaskList) {
            executor.execute(futureTask);
        }

        boolean ready = false;
        int[] dones = new int[futureTaskList.size()];
        String[] writes = new String[futureTaskList.size()];

        int indexValue = 0;
        while (!ready) {

            int count = 0;
            indexValue = 0;
            for (FutureTask<String> futureTask : futureTaskList) {
                if (futureTask.isDone() & dones[indexValue] == 0) {
                    writes[indexValue] = futureTask.get();
                    dones[indexValue] = 1;
                }
                indexValue++;
            }

            for (int k = 0; k < dones.length; k++) {
                if (dones[k] == 1) {
                    count++;
                }
            }

            if (count == futureTaskList.size()) {
                ready = true;
            }

//            Thread.sleep(500);
        }

        LOG.debug("\n\n\n ====== DONE ====== ");
        LOG.debug("  time:" + (System.currentTimeMillis() - beginTime) + "ms\n\n");
        executor.shutdown();

        for (int i = 0; i < writes.length; i++) {
            LOG.debug("- " + writes[i]);
        }
        LOG.debug("\n\n\n ====== DONE ====== \n\n");

        Thread.sleep(20000);
        LOG.debug("\n\n\n\n+++++++++++++++++++++++++");
        LOG.debug("New system:");
        IServiceSimple proxyService2 = clientSimple.getProxy(IServiceSimple.class);
        proxyService2.functionNumber1("1", "1");

    }


    class ClientCallable implements Callable<String> {

        private IServiceSimple serviceSimple;
        private int number = 0;
        private String resultValue = "";

        ClientCallable(IServiceSimple serviceSimple, int number) {
            this.serviceSimple = serviceSimple;
            this.number = number;
        }

        @Override
        public String call() throws Exception {

            int currentValue = this.number;
            if (this.number >= 5) {
                currentValue = this.number % 5;
            }

            try {
                switch (currentValue) {
                    case 0:
                        this.resultValue = this.serviceSimple.functionNumber1("1", "1");
                        break;
                    case 1:
                        this.resultValue = this.serviceSimple.functionNumber2("2", "2");
                        break;
                    case 2:
                        this.resultValue = this.serviceSimple.functionNumber3("3", "3");
                        break;
                    case 3:
                        this.resultValue = this.serviceSimple.functionNumber4("4", "4");
                        break;
                    case 4:
                        this.resultValue = this.serviceSimple.functionNumber5("5", "5");
                        break;
                    default:
                        this.resultValue = this.serviceSimple.functionNumber1("1", "1");
                        break;
                }

            } catch (Exception ex) {
                LOG.debug("Callable exception");
                ex.printStackTrace();
                return "CL:" + (this.number + 1) + " " + "EXCEPTION!!!!";
            }
            return "CL:" + (this.number + 1) + " " + this.resultValue;
        }
    }

}
