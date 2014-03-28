package com.reversemind.hypergate.client;

import org.apache.commons.pool2.impl.DefaultPooledObjectInfo;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool2;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Set;

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
public class ClientPool extends GenericObjectPool2<IHyperGateClient> {

    private static int START_POOL_SIZE = 10;
    private ClientPoolFactory clientPoolFactory;

    GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();

    /**
     * Default values 10 clients
     *
     * @param clientPoolFactory
     */
    public ClientPool(ClientPoolFactory clientPoolFactory) {
        // int maxActive, byte WHEN_EXHAUSTED_GROW, long maxWait
        // super(clientPoolFactory, START_POOL_SIZE, GenericObjectPool2.WHEN_EXHAUSTED_GROW, 30 * 1000);
        super(clientPoolFactory);

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setJmxEnabled(true);
        genericObjectPoolConfig.setJmxNameBase("HyperGatePool");
        genericObjectPoolConfig.setJmxNamePrefix("HyperGatePoolPrefix");
        genericObjectPoolConfig.setBlockWhenExhausted(false);

        this.setConfig(genericObjectPoolConfig);
        this.clientPoolFactory = clientPoolFactory;
    }

    public ClientPool(ClientPoolFactory clientPoolFactory, int poolSize) {
        // int maxActive, byte WHEN_EXHAUSTED_GROW, long maxWait
//        super(clientPoolFactory, poolSize, GenericObjectPool2.WHEN_EXHAUSTED_GROW, 30 * 1000);
//        GenericObjectPoolConfig objectPoolConfig = new GenericObjectPoolConfig().setMaxTotal(poolSize);
        super(clientPoolFactory);

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setJmxEnabled(true);
        genericObjectPoolConfig.setJmxNameBase("HyperGatePool");
        genericObjectPoolConfig.setJmxNamePrefix("HyperGatePoolPrefix");
        genericObjectPoolConfig.setBlockWhenExhausted(false);
        genericObjectPoolConfig.setMaxTotal(poolSize);
        genericObjectPoolConfig.setMinIdle(0);
        genericObjectPoolConfig.setTestOnBorrow(true);
        genericObjectPoolConfig.setMaxWaitMillis(500);
        this.setConfig(genericObjectPoolConfig);

        this.clientPoolFactory = clientPoolFactory;
        START_POOL_SIZE = poolSize;
    }

    public ClientPoolFactory getClientPoolFactory() {
        return this.clientPoolFactory;
    }

    public String printPoolMetrics() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n");
        stringBuffer.append(" getMaxActive:").append(this.getMaxTotal()).append("\n");
        stringBuffer.append(" getMaxIdle:").append(this.getMaxIdle()).append("\n");
        stringBuffer.append(" getMaxWait:").append(this.getMaxWaitMillis()).append("\n");

        stringBuffer.append(" getNumActive:").append(this.getNumActive()).append("\n");
        stringBuffer.append(" getNumIdle:").append(this.getNumIdle()).append("\n");
        stringBuffer.append(" getNumTestsPerEvictionRun:").append(this.getNumTestsPerEvictionRun()).append("\n");
        return stringBuffer.toString();
    }

    /**
     * Force to close all Netty threads
     */
    public void forceClearClose(){
        Set<IHyperGateClient> clients = this.getAll().keySet();
        if (clients.size() > 0) {
            for (IHyperGateClient client : clients) {
                if (client != null) {
                    client.shutdown();
                }
            }
        }
    }

}
