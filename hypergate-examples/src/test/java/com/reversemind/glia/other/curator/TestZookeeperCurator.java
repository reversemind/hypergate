package com.reversemind.glia.other.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;
import org.apache.zookeeper.CreateMode;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class TestZookeeperCurator {

    private static final Logger LOG = LoggerFactory.getLogger(TestZookeeperCurator.class);

    @Ignore
    @Test
    public void testChangeDataInNode() throws Exception {
        String connectionString = "localhost:2181";
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(500, 3));
        client.start();

        String path = "/baloo/services/ADDRESS/INSTANCE.001";

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        InstanceSerializerFactory factory = new InstanceSerializerFactory(objectMapper.reader(), objectMapper.writer());

        byte[] payload = "simple string".getBytes();
        client.setData().forPath(path, payload);
    }

    @Ignore
    @Test
    public void testZookeeperCurator() throws Exception {

//        String connectionString = "127.0.0.1:2128";
//
//        CuratorFramework client = null;
//
//            client = CuratorFrameworkFactory.builder()
//                    .connectionTimeoutMs(1000)
//                    .retryPolicy(new RetryNTimes(10, 500))
//                    .connectString(connectionString)
//                    .build();
//
//        client.start();

        // these are reasonable arguments for the ExponentialBackoffRetry. The first
        // retry will wait 1 second - the second will wait up to 2 seconds - the
        // third will wait up to 4 seconds.

        String connectionString = "localhost:2181";
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(500, 3));
        client.start();

        LOG.debug(client.getNamespace());

        //String path = "/zookeeper/testNode_EPH_" + System.currentTimeMillis();
        //String path = "/testNode_EPH_" + System.currentTimeMillis();

        String path = "/baloo/server/address/GLIA_SERVER_";
        String value = "simple data";

        client.create().withMode(CreateMode.EPHEMERAL).forPath(path, value.getBytes());
        //client.create().withMode(CreateMode.PERSISTENT).forPath(path, value.getBytes());

        byte[] some = client.getData().forPath(path);
        LOG.debug("Get back is:" + new String(some));

        // Let's check that EPHEMERAL will be deleted at the end of client session
        Thread.sleep(600000);
        client.close();
    }


    private void createHierarchy(CuratorFramework client, String paths, String pathSeparator) throws Exception {

        String[] elements = paths.split(pathSeparator);
        if (elements.length > 1) {
            String path = "";
            if (elements[0].length() > 0) {
                path += "/" + elements[0];
            }
            for (int i = 1; i < elements.length; i++) {
                path += "/" + elements[i];
                try {
                    client.create().forPath(path);
                } catch (Exception ex) {
                    LOG.error("Node '" + path + "' exist", ex);
                }
            }

        }

    }

    /**
     * @throws Exception
     */
    @Ignore
    @Test
    public void testCreateSubNodes() throws Exception {

        String connectionString = "localhost:2181";
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(500, 3));
        client.start();

        String path = "/baloo/application/address/server/GLIA_SERVER";
        //String path = "/baloo";
        client.create().forPath(path);


        Thread.sleep(100);
        client.close();

    }

    /**
     *
     */
    @Ignore
    @Test
    public void testCreateHierarchyPath() throws Exception {

        String hierarchyPath = "/baloo/application/address/server/GLIA_SERVER/NODE2";

        String connectionString = "localhost:2181";
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(500, 3));
        client.start();

        LOG.debug("" + client.checkExists().forPath(hierarchyPath));

        this.createHierarchy(client, hierarchyPath, "/");

        LOG.debug("" + client.checkExists().forPath(hierarchyPath));

    }

    /**
     * Just use other technique for zookeeper connection
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testPure() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(1000)
                .retryPolicy(new RetryNTimes(10, 500))
                .connectString(ZookeeperConfiguration.ZOOKEEPER_CONNECTION)
                .build();

        curatorFramework.start();

        String hierarchyPath = "/baloo/application/address/server/GLIA_SERVER/NODE2";
        LOG.debug("" + curatorFramework.checkExists().forPath(hierarchyPath));


    }


    @Ignore
    @Test
    public void testEnsure() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(1000)
                .retryPolicy(new RetryNTimes(10, 500))
                .connectString(ZookeeperConfiguration.ZOOKEEPER_CONNECTION)
                .build();

        curatorFramework.start();

        new EnsurePath(ZookeeperConfiguration.BASE_PATH).ensure(curatorFramework.getZookeeperClient());

    }

}
