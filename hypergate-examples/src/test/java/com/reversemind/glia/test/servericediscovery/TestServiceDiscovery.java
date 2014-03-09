package com.reversemind.glia.test.servericediscovery;

import com.reversemind.hypergate.server.IHyperGateServer;
import com.reversemind.hypergate.server.IPayloadProcessor;
import com.reversemind.hypergate.server.PayloadProcessor;
import com.reversemind.hypergate.server.ServerFactory;
import com.reversemind.hypergate.servicediscovery.ServiceDiscoverer;
import com.reversemind.hypergate.servicediscovery.serializer.ServerMetadata;
import com.reversemind.hypergate.servicediscovery.serializer.ServerMetadataBuilder;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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
public class TestServiceDiscovery implements Serializable {

    @Ignore
    @Test
    public void testAdvertise() throws InterruptedException, IOException {

        final String SERVICE_NAME = "ADDRESS";
        final String ZOOKEEPER_CONNECTION_STRING = "localhost:2181";
        final String BASE_PATH = "/baloo/services/" + SERVICE_NAME;

        ServiceDiscoverer discoverer = new ServiceDiscoverer(ZOOKEEPER_CONNECTION_STRING, BASE_PATH);

        //IHyperGateServer serverOne = new HyperGateServer(SERVICE_NAME, null, false);

        IPayloadProcessor gliaPayloadProcessor = new PayloadProcessor();

        IHyperGateServer serverOne = ServerFactory.builder()
                .setPayloadWorker(gliaPayloadProcessor)
                .setName(SERVICE_NAME)
                .setZookeeperHosts(ZOOKEEPER_CONNECTION_STRING)
                .setServiceBasePath(BASE_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .build();

        IHyperGateServer serverTwo = ServerFactory.builder()
                .setPayloadWorker(gliaPayloadProcessor)
                .setName(SERVICE_NAME)
                .setZookeeperHosts(ZOOKEEPER_CONNECTION_STRING)
                .setServiceBasePath(BASE_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .build();

        discoverer.advertise(new ServerMetadataBuilder().build(serverOne), BASE_PATH);
        discoverer.advertise(new ServerMetadataBuilder().build(serverTwo), BASE_PATH);

        Thread.sleep(1000);

        List<ServerMetadata> metadataList = discoverer.discover(SERVICE_NAME);

        discoverer.close();

        serverOne.shutdown();
        serverTwo.shutdown();

    }

}
