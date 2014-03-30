package com.reversemind.hypergate.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (c) 2013-2014 Eugene Kalinin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class StartEmbeddedZookeeper {

    private final static Logger LOG = LoggerFactory.getLogger(StartEmbeddedZookeeper.class);

    public static final String ZOOKEEPER_HOST = "127.0.0.1";
    public static final String ZOOKEEPER_CONNECTION_STRING = ZOOKEEPER_HOST + ":" + EmbeddedZookeeper.EMBEDDED_ZOOKEEPER_PORT;

    public static final String SERVICE_BASE_PATH = "/baloo/services";
    public static final String SERVICE_HYPER_GATE_NAME = "HYPERGATE.ADDRESS";
    public static final String SERVICE_FULL_PATH = SERVICE_BASE_PATH + "/" + SERVICE_HYPER_GATE_NAME;

    public static final String SERVER_INSTANCE_NAME_1 = "INSTANCE.001";
    public static final String SERVER_INSTANCE_NAME_2 = "INSTANCE.002";

    /**
     *
     */
    @Before
    public void init() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        // need Zookeeper to advertise service
        EmbeddedZookeeper.start();

        // create default path - like /baloo/services
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_CONNECTION_STRING, new ExponentialBackoffRetry(500, 3));
            client.start();

            client.create().forPath("/baloo");
            client.create().forPath(SERVICE_BASE_PATH);

            client.close();
        } catch (Exception e) {
            LOG.warn("NODE EXIST", e);
        }
    }

    @After
    public void shutDown() throws InterruptedException {
        EmbeddedZookeeper.stop();
    }
}
