package com.reversemind.hypergate.example.discovery;

import com.reversemind.hypergate.client.HyperGateClientServerDiscovery;
import com.reversemind.hypergate.server.IHyperGateServer;
import com.reversemind.hypergate.server.PayloadProcessor;
import com.reversemind.hypergate.server.ServerFactory;
import com.reversemind.hypergate.servicediscovery.ServerSelectorSimpleStrategy;
import com.reversemind.hypergate.zookeeper.StartEmbeddedZookeeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import static org.junit.Assert.*;

/**
 *
 * Copyright (c) 2013-2014 Eugene Kalinin
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
public class TestHyperGateClientServerDiscovery extends StartEmbeddedZookeeper implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(TestHyperGateClientServerDiscovery.class);

    private IHyperGateServer hyperGateServer;

    @Override
    @Before
    public void init() {
        super.init();

        // Advertize server in Zookeeper = .setType(ServerFactory.Builder.Type.ZOOKEEPER_ADVERTISER)
        hyperGateServer = ServerFactory.builder()
                .setPayloadWorker(new PayloadProcessor())
                .setName(SERVICE_HYPER_GATE_NAME)
                .setInstanceName(SERVER_INSTANCE_NAME_1)
                .setZookeeperHosts(ZOOKEEPER_CONNECTION_STRING)
                .setServiceBasePath(SERVICE_BASE_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .setType(ServerFactory.Builder.Type.ZOOKEEPER_ADVERTISER)
                .build();

        hyperGateServer.start();
    }

    @Override
    @After
    public void shutDown(){
        if(hyperGateServer != null){
            hyperGateServer.shutdown();
        }
        super.shutDown();
    }

    @Test
    public void testClient() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_CONNECTION_STRING, new ExponentialBackoffRetry(500, 3));
        client.start();

        Stat stat = client.checkExists().forPath(SERVICE_BASE_PATH + "/" + SERVICE_HYPER_GATE_NAME + "/" + SERVER_INSTANCE_NAME_1);
        LOG.info("STAT:" + stat);

        assertNotNull(stat);
        client.close();
    }

    @Test
    public void testClientSelfDiscovery() throws Exception {

        final long clientTimeOut = 3 * 1000;
        HyperGateClientServerDiscovery client = new HyperGateClientServerDiscovery(ZOOKEEPER_CONNECTION_STRING, SERVICE_BASE_PATH, SERVICE_HYPER_GATE_NAME, clientTimeOut, new ServerSelectorSimpleStrategy());

        client.start();

        Thread.sleep(500);

        LOG.info("Is client running:" + client.isRunning());
        assertTrue(client.isRunning());
        Thread.sleep(clientTimeOut);

        client.shutdown();
    }

}
