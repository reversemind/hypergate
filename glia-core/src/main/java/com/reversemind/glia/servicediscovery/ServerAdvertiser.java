package com.reversemind.glia.servicediscovery;

import com.google.common.base.Throwables;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.JsonServiceInstance;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import com.reversemind.glia.servicediscovery.serializer.InstanceSerializerFactory;
import com.reversemind.glia.servicediscovery.serializer.ServerMetadata;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

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
public class ServerAdvertiser implements Serializable {

    private static Logger LOG = Logger.getLogger(ServerAdvertiser.class);

    private CuratorFramework curatorFramework;
    private InstanceSerializer<ServerMetadata> jacksonInstanceSerializer;
    private ServerMetadata serverMetadata;
    private String basePath;

    public ServerAdvertiser(CuratorFramework curatorFramework,
                            InstanceSerializerFactory instanceSerializerFactory,
                            ServerMetadata serverMetadata,
                            String basePath) {

        this.curatorFramework = curatorFramework;
        this.jacksonInstanceSerializer = instanceSerializerFactory.getInstanceSerializer(
                new TypeReference<JsonServiceInstance<ServerMetadata>>() {
                }
        );
        this.serverMetadata = serverMetadata;
        this.basePath = basePath;
    }

    public void advertiseAvailability() {
        try {
            ServiceDiscovery<ServerMetadata> discovery = this.getDiscovery();
            discovery.start();

            ServiceInstance serviceInstance = this.getInstance();
//            LOG.info("Service:" + serviceInstance + " is available");
            discovery.registerService(serviceInstance);

            // TODO ??????
            //discovery.close();
        } catch (Exception ex) {
            // look through it again
            ex.printStackTrace();
            throw Throwables.propagate(ex);
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private ServiceInstance<ServerMetadata> getInstance() throws Exception {

        ServerMetadata metadata = new ServerMetadata(
                this.serverMetadata.getName(),
                this.serverMetadata.getInstance(),
                this.serverMetadata.getHost(),
                this.serverMetadata.getPort(),
                this.serverMetadata.getMetrics()
        );

        return ServiceInstance.<ServerMetadata>builder()
                .name(this.serverMetadata.getName())
                .address(this.serverMetadata.getHost())
                .port(this.serverMetadata.getPort())
                .id(this.serverMetadata.getInstance())
                .payload(metadata)
                .build();
    }

    private ServiceDiscovery<ServerMetadata> getDiscovery() {
        return ServiceDiscoveryBuilder.builder(ServerMetadata.class)
                .basePath(this.basePath)
                .client(curatorFramework)
                .serializer(jacksonInstanceSerializer)
                .build();
    }

    public void deAdvertiseAvailability() {
        try {
            ServiceDiscovery<ServerMetadata> discovery = this.getDiscovery();
            discovery.start();
            discovery.unregisterService(getInstance());
            //discovery.close();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public void close() throws IOException {
        getDiscovery().close();
    }

}
