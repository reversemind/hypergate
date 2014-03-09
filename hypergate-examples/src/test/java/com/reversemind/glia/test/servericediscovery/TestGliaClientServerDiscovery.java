package com.reversemind.glia.test.servericediscovery;

import com.reversemind.hypergate.client.HyperGateClientServerDiscovery;
import com.reversemind.hypergate.servicediscovery.ServerSelectorSimpleStrategy;
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
public class TestGliaClientServerDiscovery implements Serializable {

    @Ignore
    @Test
    public void testGliaClientSelfDiscovery() throws Exception {

        final String ZOOKEEPER_CONNECTION = "localhost:2181";
        final String SERVICE_BASE_PATH = "/baloo/services";
        final String SERVICE_NAME = "ADDRESS";

        final long clientTimeOut = 30000;
        HyperGateClientServerDiscovery client = new HyperGateClientServerDiscovery(ZOOKEEPER_CONNECTION, SERVICE_BASE_PATH, SERVICE_NAME, clientTimeOut, new ServerSelectorSimpleStrategy());

        client.start();

        Thread.sleep(1000 * 30);

        client.shutdown();
    }

}