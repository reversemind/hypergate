/**
 * Copyright (c) 2013-2015 Eugene Kalinin
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
 *
 */

package com.reversemind.hypergate.client;

import com.reversemind.hypergate.servicediscovery.IServerSelectorStrategy;
import com.reversemind.hypergate.servicediscovery.ServiceDiscoverer;
import com.reversemind.hypergate.servicediscovery.serializer.ServerMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

public class HyperGateClientServerDiscovery extends HyperGateClient implements IHyperGateClient, Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(HyperGateClientServerDiscovery.class);

    private String zookeeperHosts;
    private String serviceBasePath;
    private String serviceName;
    private IServerSelectorStrategy serverSelectorStrategy;

    private ServiceDiscoverer serviceDiscoverer;

    /**
     * @param zookeeperHosts
     * @param serviceBasePath
     * @param serviceName
     * @param clientTimeOut
     * @param serverSelectorStrategy
     */
    public HyperGateClientServerDiscovery(String zookeeperHosts,
                                          String serviceBasePath,
                                          String serviceName,
                                          long clientTimeOut,
                                          IServerSelectorStrategy serverSelectorStrategy) {
        this.zookeeperHosts = zookeeperHosts;
        this.serviceBasePath = serviceBasePath;
        this.serviceName = serviceName;
        this.serverSelectorStrategy = serverSelectorStrategy;
        this.setClientTimeOut(clientTimeOut);
        this.serviceDiscoverer = new ServiceDiscoverer(this.zookeeperHosts, this.serviceBasePath);
    }

    @Override
    public void start() throws Exception {
        ServerMetadata serverMetadata = this.serverSelectorStrategy.selectServer(this.serviceDiscoverer.discover(this.serviceName));
        if (serverMetadata != null && !StringUtils.isEmpty(serverMetadata.getHost()) && serverMetadata.getPort() > 0) {
            LOG.info("found server:" + serverMetadata);
            this.port = serverMetadata.getPort();
            this.host = serverMetadata.getHost();
            super.start();
            return;
        }
        throw new Exception("Could not find any available server for the ServiceName:" + this.serviceName + " on path:" + this.serviceBasePath);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if (this.serviceDiscoverer != null) {
            try {
                this.serviceDiscoverer.close();
            } catch (IOException e) {
                LOG.error("Shutting down HyperGate client server discovery - ", e);
            }
        }
    }

    @Override
    public void restart() throws Exception {
        this.shutdown();
//        this.zookeeperHosts = zookeeperHosts;
//        this.serviceBasePath = serviceBasePath;
//        this.serviceName = serviceName;
//        this.serverSelectorStrategy = serverSelectorStrategy;
        this.serviceDiscoverer = new ServiceDiscoverer(this.zookeeperHosts, this.serviceBasePath);
        this.start();
    }

    @Override
    public void restart(String serverHost, int serverPort, long clientTimeOut) throws Exception {
        this.shutdown();
        this.setClientTimeOut(clientTimeOut);
        this.serviceDiscoverer = new ServiceDiscoverer(this.zookeeperHosts, this.serviceBasePath);
        this.start();
    }

}
