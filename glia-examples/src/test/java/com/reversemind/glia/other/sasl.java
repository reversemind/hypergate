package com.reversemind.glia.other;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class sasl implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(sasl.class);

    public static void main(String... args) throws Exception {

        // https://issues.apache.org/jira/browse/ZOOKEEPER-1554
        System.setProperty("java.security.auth.login.config", "/opt/zookeeper/conf/jaas.conf");
        System.setProperty("curator-log-events", "true");

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework client = builder.connectString("localhost:2181").retryPolicy(new RetryOneTime(1000)).connectionTimeoutMs(10000).sessionTimeoutMs(100000).build();
        client.start();

        Thread.sleep(3000);

        LOG.debug("creating the node..");
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.CREATOR_ALL_ACL).forPath("/node01", "some text".getBytes());
        LOG.debug("node created");

        client.close();
    }

}
