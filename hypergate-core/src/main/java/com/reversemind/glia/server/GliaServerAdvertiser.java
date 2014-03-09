package com.reversemind.glia.server;

import com.reversemind.glia.servicediscovery.ServiceDiscoverer;
import com.reversemind.glia.servicediscovery.serializer.ServerMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

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
public class GliaServerAdvertiser extends GliaServer implements Serializable {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GliaServerAdvertiser.class);

    private String zookeeperHosts;
    private String serviceBasePath;
    private ServiceDiscoverer serviceDiscoverer;
    private ServerMetadata metadata;

    private MetricsUpdateTask metricsUpdateTask;
    private Timer timer;

    private boolean useMetrics = false;
    private long periodPublishMetrics = 1000; // ms

    /**
     * @param builder
     */
    public GliaServerAdvertiser(GliaServerFactory.Builder builder) {
        super(builder);

        if (StringUtils.isEmpty(builder.getZookeeperHosts())) {
            throw new RuntimeException("Need zookeeper connection string");
        }
        this.zookeeperHosts = builder.getZookeeperHosts();

        if (StringUtils.isEmpty(builder.getServiceBasePath())) {
            throw new RuntimeException("Need zookeeper base path string");
        }
        this.serviceBasePath = builder.getServiceBasePath();

        this.metadata = new ServerMetadata(
                this.getName(),
                this.getInstanceName(),
                this.getHost(),
                this.getPort(),
                this.metrics
        );

        this.useMetrics = builder.isUseMetrics();
        if (builder.isUseMetrics()) {

            this.periodPublishMetrics = builder.getPeriodPublishMetrics();

            LOG.debug("builder.getPeriodPublishMetrics() == " + builder.getPeriodPublishMetrics());
            if (builder.getPeriodPublishMetrics() < 0) {
                this.periodPublishMetrics = 1000; //ms
            }

            LOG.debug("this.periodPublishMetrics == " + this.periodPublishMetrics);

            this.metricsUpdateTask = new MetricsUpdateTask();
            this.timer = new Timer();
            this.timer.schedule(this.metricsUpdateTask, this.periodPublishMetrics, this.periodPublishMetrics);
        }

    }

    /**
     *
     */
    @Override
    public void start() {
        super.start();
        this.serviceDiscoverer = new ServiceDiscoverer(this.zookeeperHosts, this.serviceBasePath);
        this.serviceDiscoverer.advertise(this.metadata, this.serviceBasePath);
    }

    /**
     * Update different metrics
     */
    public void updateMetrics() {
        this.metadata = new ServerMetadata(
                this.getName(),
                this.getInstanceName(),
                this.getHost(),
                this.getPort(),
                this.metrics
        );
        this.serviceDiscoverer.advertise(this.metadata, this.serviceBasePath);
    }

    /**
     *
     */
    @Override
    public void shutdown() {
        super.shutdown();
        if (this.serviceDiscoverer != null) {
            synchronized (this.serviceDiscoverer) {
                try {
                    this.serviceDiscoverer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return
     */
    public ServerMetadata getMetadata() {
        return this.metadata;
    }

    /**
     *
     */
    private class MetricsUpdateTask extends TimerTask {
        @Override
        public void run() {
            updateMetrics();
        }
    }
}
