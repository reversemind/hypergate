package com.reversemind.hypergate.example.discovery;

import com.reversemind.hypergate.server.IHyperGateServer;
import com.reversemind.hypergate.server.IPayloadProcessor;
import com.reversemind.hypergate.server.PayloadProcessor;
import com.reversemind.hypergate.server.ServerFactory;
import com.reversemind.hypergate.servicediscovery.ServiceDiscoverer;
import com.reversemind.hypergate.servicediscovery.serializer.ServerMetadata;
import com.reversemind.hypergate.servicediscovery.serializer.ServerMetadataBuilder;
import com.reversemind.hypergate.zookeeper.StartEmbeddedZookeeper;
import junit.framework.Assert;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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
public class TestHyperGateServerAdvertiser extends StartEmbeddedZookeeper implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(TestHyperGateServerAdvertiser.class);

    /**
     * Advertise two servers in Zookeeper at /baloo/services/HYPERGATE.ADDRESS/INSTANCE.001 and /baloo/services/HYPERGATE.ADDRESS/INSTANCE.002
     *
     * @throws InterruptedException
     */
    @Test
    public void testServerSelfAdvertiser() throws InterruptedException {

        IPayloadProcessor payloadProcessor = new PayloadProcessor();

        IHyperGateServer server01 = ServerFactory.builder()
                .setPayloadWorker(payloadProcessor)
                .setName(SERVICE_HYPER_GATE_NAME)
                .setInstanceName(SERVER_INSTANCE_NAME_1)
                .setZookeeperHosts(ZOOKEEPER_CONNECTION_STRING)
                .setServiceBasePath(SERVICE_BASE_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .setType(ServerFactory.Builder.Type.ZOOKEEPER_ADVERTISER)
                .build();

        IHyperGateServer server02 = ServerFactory.builder()
                .setPayloadWorker(payloadProcessor)
                .setName(SERVICE_HYPER_GATE_NAME)
                .setInstanceName(SERVER_INSTANCE_NAME_2)
                .setZookeeperHosts(ZOOKEEPER_CONNECTION_STRING)
                .setServiceBasePath(SERVICE_BASE_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .setType(ServerFactory.Builder.Type.ZOOKEEPER_ADVERTISER)
                .build();

        server01.start();
        server02.start();

        Thread.sleep(1000 * 2);

        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_CONNECTION_STRING, new ExponentialBackoffRetry(500, 3));
            client.start();

            // /baloo/services/HYPERGATE.ADDRESS/INSTANCE.001
            Stat stat = client.checkExists().forPath(SERVICE_BASE_PATH + "/" + SERVICE_HYPER_GATE_NAME + "/" + SERVER_INSTANCE_NAME_1);
            LOG.info(" INSTANCE.001 -- " + stat);
            Assert.assertNotNull(stat);

            // /baloo/services/HYPERGATE.ADDRESS/INSTANCE.002
            stat = client.checkExists().forPath(SERVICE_BASE_PATH + "/" + SERVICE_HYPER_GATE_NAME + "/" + SERVER_INSTANCE_NAME_2);
            LOG.info(" INSTANCE.002 -- " + stat);
            Assert.assertNotNull(stat);

            client.close();

            LOG.info("Server successfully Advertised");
        } catch (Exception e) {
            LOG.warn("NODE EXIST", e);
        }

        Thread.sleep(1000 * 2);

        server01.shutdown();
        server02.shutdown();
    }

    /**
     * Adverise servers by hand using - discoverer.advertise(new ServerMetadataBuilder().build(serverOne), SERVICE_FULL_PATH);
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void testHandAdvertise() throws InterruptedException, IOException {

        ServiceDiscoverer discoverer = new ServiceDiscoverer(ZOOKEEPER_CONNECTION_STRING, SERVICE_FULL_PATH);

        //IHyperGateServer serverOne = new HyperGateServer(SERVICE_HYPER_GATE_NAME, null, false);

        IPayloadProcessor payloadProcessor = new PayloadProcessor();

        IHyperGateServer serverOne = ServerFactory.builder()
                .setPayloadWorker(payloadProcessor)
                .setName(SERVER_INSTANCE_NAME_1)
                .setZookeeperHosts(ZOOKEEPER_CONNECTION_STRING)
                .setServiceBasePath(SERVICE_FULL_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .build();

        IHyperGateServer serverTwo = ServerFactory.builder()
                .setPayloadWorker(payloadProcessor)
                .setName(SERVER_INSTANCE_NAME_2)
                .setZookeeperHosts(SERVICE_HYPER_GATE_NAME)
                .setServiceBasePath(SERVICE_FULL_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .build();

        discoverer.advertise(new ServerMetadataBuilder().build(serverOne), SERVICE_FULL_PATH);
        discoverer.advertise(new ServerMetadataBuilder().build(serverTwo), SERVICE_FULL_PATH);

        Thread.sleep(2000);

        List<ServerMetadata> metadataList = discoverer.discover(SERVICE_HYPER_GATE_NAME);
        LOG.info("metadataList == " + metadataList);


        Thread.sleep(2000);
        discoverer.close();

        serverOne.shutdown();
        serverTwo.shutdown();

    }

}
