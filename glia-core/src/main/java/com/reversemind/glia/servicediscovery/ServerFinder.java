package com.reversemind.glia.servicediscovery;

import com.google.common.base.Throwables;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.x.discovery.JsonServiceInstance;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import com.reversemind.glia.servicediscovery.serializer.InstanceSerializerFactory;
import com.reversemind.glia.servicediscovery.serializer.ServerMetadata;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
public class ServerFinder implements Serializable {

    private static Logger LOG = Logger.getLogger(ServerFinder.class);

    private ServiceDiscovery<ServerMetadata> discovery;
    private CuratorFramework curatorFramework;
    private String basePath;

    /**
     * @param curatorFramework
     * @param instanceSerializerFactory
     */
    public ServerFinder(CuratorFramework curatorFramework,
                        InstanceSerializerFactory instanceSerializerFactory,
                        String basePath) {

        discovery = ServiceDiscoveryBuilder.builder(ServerMetadata.class)
                .basePath(basePath)
                .client(curatorFramework)
                .serializer(instanceSerializerFactory
                        .getInstanceSerializer(new TypeReference<JsonServiceInstance<ServerMetadata>>() {
                        }))
                .build();

        this.curatorFramework = curatorFramework;
        this.basePath = basePath;
        try {
            discovery.start();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * @param serviceName
     * @return
     */
    public Collection<ServiceInstance<ServerMetadata>> getServers(String serviceName) {

        Collection<ServiceInstance<ServerMetadata>> instances;
        try {
            instances = discovery.queryForInstances(serviceName);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
        return instances;
    }

    /**
     * @param name
     * @throws Exception
     */
    private void p(String name) throws Exception {
        String path = ZKPaths.makePath(ZKPaths.makePath(this.basePath, name), null);
        List<String> files = curatorFramework.getChildren().forPath("/baloo/services");
        LOG.debug(files);
    }

}
