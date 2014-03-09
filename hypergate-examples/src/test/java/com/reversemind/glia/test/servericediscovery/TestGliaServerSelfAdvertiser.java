package com.reversemind.glia.test.servericediscovery;

import com.reversemind.glia.server.GliaPayloadProcessor;
import com.reversemind.glia.server.GliaServerFactory;
import com.reversemind.glia.server.IGliaPayloadProcessor;
import com.reversemind.glia.server.IGliaServer;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Serializable;

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
public class TestGliaServerSelfAdvertiser implements Serializable {

    @Ignore
    @Test
    public void testGliaServerSelfAdvertiser() throws InterruptedException {

        final String ZOOKEEPER_CONNECTION = "localhost:2181";
        final String SERVICE_BASE_PATH = "/baloo/services";
        final String SERVICE_NAME = "GLIA.ADDRESS";

        IGliaPayloadProcessor gliaPayloadProcessor = new GliaPayloadProcessor();

        IGliaServer server01 = GliaServerFactory.builder()
                .setPayloadWorker(gliaPayloadProcessor)
                .setName(SERVICE_NAME)
                .setInstanceName("INSTANCE.001")
                .setZookeeperHosts(ZOOKEEPER_CONNECTION)
                .setServiceBasePath(SERVICE_BASE_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .build();

        IGliaServer server02 = GliaServerFactory.builder()
                .setPayloadWorker(gliaPayloadProcessor)
                .setName(SERVICE_NAME)
                .setInstanceName("INSTANCE.002")
                .setZookeeperHosts(ZOOKEEPER_CONNECTION)
                .setServiceBasePath(SERVICE_BASE_PATH)
                .setAutoSelectPort(true)
                .setKeepClientAlive(false)
                .build();

        server01.start();
        server02.start();

        for (int i = 0; i < 100; i++) {
            Thread.sleep(1000);
            //server01.updateMetrics();
            server01.getMetrics().addRequest(10);
        }

        Thread.sleep(1000 * 60 * 1000);

        server01.shutdown();
        server02.shutdown();
    }

}
