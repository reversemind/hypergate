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
 */

package com.reversemind.hypergate.server;

import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Eugene Kalinin
 */
public class ServerFactory implements Serializable {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ServerFactory.class);

    private ServerFactory() {
    }

    /**
     * Return a new builder that builds a HyperGateServer
     *
     * @return new Builder();
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     *
     */
    public static class Builder {

        private String name = null;
        private String instanceName = null;
        private int port = -1;
        private boolean dropClientConnection = false;
        private IPayloadProcessor payloadWorker = null;

        private boolean autoSelectPort = true;
        private boolean keepClientAlive = false;

        private Type type = Type.SIMPLE;

        // for Zookeeper specific
        private String zookeeperHosts;
        private String serviceBasePath;

        private boolean useMetrics = false;
        private long periodPublishMetrics = 1000; // ms

        /**
         * Actually this is Constructor should be a private but for Spring Context it's opened
         */
        public Builder() {
        }

        /**
         * Using type SIMPLE, ZOOKEEPER_ADVERTISER to build server
         * <p/>
         * for SIMPLE HyperGateServerSimple
         * for ZOOKEEPER_ADVERTISER HyperGateServerAdvertiser
         *
         * @return
         */
        public IHyperGateServer build() {
            if (this.payloadWorker == null) {
                throw new RuntimeException("Assign a setPayloadWorker to server!");
            }
            switch (this.type) {
                case SIMPLE:
                    return new HyperGateServerSimple(this);
                case ZOOKEEPER_ADVERTISER:
                    return new HyperGateServerAdvertiser(this);
                default:
                    return new HyperGateServerSimple(this);
            }
        }

        public Builder setPayloadWorker(IPayloadProcessor payloadWorker) {
            this.payloadWorker = payloadWorker;
            return this;
        }

        /**
         * Server setName should be without any space - if you will use zookeeper
         *
         * @param name
         * @return
         */
        public Builder setName(String name) {
            // TODO check is the setName contains any space
            this.name = name;
            return this;
        }

        /**
         * Instance setName should be without any space - if you will use zookeeper
         * <p></p>
         * but prefer to use empty - constructor will assign a UUID for instance setName
         *
         * @param instanceName
         * @return
         */
        public Builder setInstanceName(String instanceName) {
            this.instanceName = instanceName;
            return this;
        }

        /**
         * Right now (v1.4) only SIMPLE and ZOOKEEPER_ADVERTISER(server will share selft info end metrics inside zookeeper)
         *
         * @param type
         * @return
         */
        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Example: localhost:2181
         *
         * @param zookeeperHosts
         * @return
         */
        public Builder setZookeeperHosts(String zookeeperHosts) {
            this.zookeeperHosts = zookeeperHosts;
            return this;
        }

        /**
         * Base path relatively from this server will publish info
         * <p></p>
         * Example: /baloo/services
         *
         * @param serviceBasePath
         * @return
         */
        public Builder setServiceBasePath(String serviceBasePath) {
            this.serviceBasePath = serviceBasePath;
            return this;
        }

        /**
         * if setAutoSelectPort is true than assigned setPort number will be ignored
         *
         * @param port - setPort number from 0-65k
         * @return
         */
        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * Force to server select and assign any available setPort in OS
         *
         * @param autoSelectPort
         * @return
         */
        public Builder setAutoSelectPort(boolean autoSelectPort) {
            this.autoSelectPort = autoSelectPort;
            return this;
        }

        /**
         * Close or not after response a client connection
         *
         * @param keepClientAlive
         * @return
         */
        public Builder setKeepClientAlive(boolean keepClientAlive) {
            LOG.debug(" =HyperGate= goint to set keep client alive:" + keepClientAlive);
            this.keepClientAlive = keepClientAlive;
            return this;
        }

        public Builder setPeriodPublishMetrics(long periodPublishMetrics) {
            this.periodPublishMetrics = periodPublishMetrics;
            return this;
        }

        public Builder setUseMetrics(boolean useMetrics) {
            this.useMetrics = useMetrics;
            return this;
        }

        public long getPeriodPublishMetrics() {
            return periodPublishMetrics;
        }

        public boolean isUseMetrics() {
            return useMetrics;
        }

        public int getPort() {
            return port;
        }

        public boolean isKeepClientAlive() {
            return keepClientAlive;
        }

        public String getName() {
            return name;
        }

        public String getInstanceName() {
            return instanceName;
        }

        public int port() {
            return port;
        }

        public boolean isDropClientConnection() {
            return dropClientConnection;
        }

        public IPayloadProcessor getPayloadWorker() {
            return payloadWorker;
        }

        /**
         * Server setType
         *
         * @return
         */
        public Type getType() {
            return type;
        }

        public String getServiceBasePath() {
            return this.serviceBasePath;
        }

        /**
         * If setAutoSelectPort is true - setPort number will be ignored
         *
         * @return
         */
        public boolean isAutoSelectPort() {
            return autoSelectPort;
        }

        public String getZookeeperHosts() {
            return this.zookeeperHosts;
        }

        public enum Type {
            SIMPLE, ZOOKEEPER_ADVERTISER
        }
    }

}
