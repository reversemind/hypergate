package com.reversemind.glia.client;

import org.apache.commons.pool.impl.GenericObjectPool;

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
public class ClientPool extends GenericObjectPool<IGliaClient> {

    private static int START_POOL_SIZE = 10;
    private ClientPoolFactory clientPoolFactory;

    /**
     * Right now only max 10 clients
     *
     * @param clientPoolFactory
     */
    public ClientPool(ClientPoolFactory clientPoolFactory) {
        // int maxActive, byte WHEN_EXHAUSTED_GROW, long maxWait
        super(clientPoolFactory, START_POOL_SIZE, GenericObjectPool.WHEN_EXHAUSTED_GROW, 30 * 1000);
        this.clientPoolFactory = clientPoolFactory;
    }

    public ClientPool(ClientPoolFactory clientPoolFactory, int poolSize) {
        // int maxActive, byte WHEN_EXHAUSTED_GROW, long maxWait
        super(clientPoolFactory, poolSize, GenericObjectPool.WHEN_EXHAUSTED_GROW, 30 * 1000);
        this.clientPoolFactory = clientPoolFactory;
        START_POOL_SIZE = poolSize;
    }

    public ClientPoolFactory getClientPoolFactory() {
        return this.clientPoolFactory;
    }

    public String printPoolMetrics() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" getMaxActive:").append(this.getMaxActive()).append("\n");
        stringBuffer.append(" getMaxIdle:").append(this.getMaxIdle()).append("\n");
        stringBuffer.append(" getMaxWait:").append(this.getMaxWait()).append("\n");

        stringBuffer.append(" getNumActive:").append(this.getNumActive()).append("\n");
        stringBuffer.append(" getNumIdle:").append(this.getNumIdle()).append("\n");
        stringBuffer.append(" getNumTestsPerEvictionRun:").append(this.getNumTestsPerEvictionRun()).append("\n");
        return stringBuffer.toString();
    }
}
