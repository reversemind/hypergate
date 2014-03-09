package com.reversemind.glia.servicediscovery;

import com.google.common.base.Throwables;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;
import org.apache.curator.x.discovery.ServiceInstance;
import com.reversemind.glia.server.Metrics;
import com.reversemind.glia.servicediscovery.serializer.InstanceSerializerFactory;
import com.reversemind.glia.servicediscovery.serializer.ServerMetadata;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Copyright (c) 2013 Eugene Kalinin
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
public class ServiceDiscoverer implements Serializable, Closeable {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ServiceDiscoverer.class);

    private static final String ZOOKEEPER_CONNECTION_STRING_TEST = "127.0.0.1:2181";
    private static final String BASE_PATH_TEST = "/baloo/services";

    private CuratorFramework curatorFramework;
    private List<ServerAdvertiser> listServerAdvertiser;

    private String zookeeperConnectionString;
    private String basePath;

    private int privateCounter = 0;

    public ServiceDiscoverer(String zookeeperConnectionString, String basePath) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(30000)
                .retryPolicy(new RetryNTimes(10, 500))
                .connectString(zookeeperConnectionString)
                .build();
        curatorFramework.start();

        this.listServerAdvertiser = new ArrayList<ServerAdvertiser>();

        this.zookeeperConnectionString = zookeeperConnectionString;
        this.basePath = basePath;
    }

    public List<ServerMetadata> discover(String serviceName) {
        try {
            new EnsurePath(this.basePath).ensure(curatorFramework.getZookeeperClient());
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }

        ServerFinder serverFinder = new ServerFinder(curatorFramework, this.getInstanceSerializerFactory(), this.basePath);

        List<ServerMetadata> metadataList = new ArrayList<ServerMetadata>();
        for (ServiceInstance<ServerMetadata> instance : serverFinder.getServers(serviceName)) {
            ServerMetadata serverMetadata = instance.getPayload();

            LOG.warn("found a server with parameters:" + serverMetadata);
            metadataList.add(serverMetadata);
        }

        Collections.sort(metadataList, new Comparator<ServerMetadata>() {
            @Override
            public int compare(ServerMetadata o1, ServerMetadata o2) {
                return (int) (o2.getMetrics().getStartDate().getTime() - o1.getMetrics().getStartDate().getTime());
            }
        });

        LOG.warn("Sorted by startDate:");
        for (ServerMetadata metadata : metadataList) {
            LOG.warn("server:" + metadata);
        }

        return metadataList;
    }

    public void advertise(ServerMetadata serverMetadata, String basePath) {
        ServerAdvertiser serverAdvertiser = new ServerAdvertiser(curatorFramework, this.getInstanceSerializerFactory(), serverMetadata, basePath);
        serverAdvertiser.advertiseAvailability();
        this.listServerAdvertiser.add(serverAdvertiser);

        if (this.privateCounter++ > 10) {
            LOG.debug(this.privateCounter + " = advertised... a " + serverMetadata);
            this.privateCounter = 0;
        }

    }

    private InstanceSerializerFactory getInstanceSerializerFactory() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new InstanceSerializerFactory(objectMapper.reader(), objectMapper.writer());
    }

    @Override
    public void close() throws IOException {
        if (this.listServerAdvertiser != null && this.listServerAdvertiser.size() > 0) {
            for (int i = 0; i < this.listServerAdvertiser.size(); i++) {
                synchronized (this.listServerAdvertiser.get(i)) {
                    this.listServerAdvertiser.get(i).deAdvertiseAvailability();
                    this.listServerAdvertiser.get(i).close();
                }
            }
        }
        this.curatorFramework.close();
    }

    public static void main(String... args) throws Exception {

        final String BASE_PATH = "/baloo/services";
        final String ZOOKEEPER_CONNECTION_STRING = "127.0.0.1:2181";

        ServiceDiscoverer discoverer = new ServiceDiscoverer(ZOOKEEPER_CONNECTION_STRING, BASE_PATH);

        ServerMetadata serverMetadata = new ServerMetadata(
                "GLIA_SERVER",
                "INSTANCE 001",
                "localhost",
                7000,
                new Metrics()
        );

        discoverer.advertise(serverMetadata, BASE_PATH);

        discoverer.advertise(new ServerMetadata(
                "GLIA_SERVER",
                "INSTANCE 002",
                "localhost",
                7001,
                new Metrics()
        ), BASE_PATH);

        Thread.sleep(1000);

        List<ServerMetadata> metadataList = discoverer.discover("GLIA_SERVER");

        discoverer.close();
    }
}
