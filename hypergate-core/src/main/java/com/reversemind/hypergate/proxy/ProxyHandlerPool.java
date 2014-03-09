package com.reversemind.hypergate.proxy;

import com.reversemind.hypergate.client.ClientPool;
import com.reversemind.hypergate.client.ClientPoolFactory;
import com.reversemind.hypergate.client.IHyperGateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;

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
public class ProxyHandlerPool extends AbstractProxyHandler implements InvocationHandler {

    private final static Logger LOG = LoggerFactory.getLogger(ProxyHandlerPool.class);

    private ClientPool clientPool;
    private Class interfaceClass;
    private IHyperGateClient client = null;

    public ProxyHandlerPool(ClientPool clientPool, Class interfaceClass) {
        this.clientPool = clientPool;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public IHyperGateClient getClient() throws Exception {
        if (this.client == null) {
            try {
                synchronized (this.clientPool) {
                    this.client = this.clientPool.borrowObject();
                }
            } catch (Exception ex) {
                LOG.error("Could not get a hyperGateClient from Pool #1 - try again", ex);

                try {
                    Thread.sleep(300);
                    synchronized (this.clientPool) {
                        this.client = this.clientPool.borrowObject();
                    }
                } catch (Exception ex2) {
                    LOG.error("Could not get a hyperGateClient from Pool #2 - Try to reload pool", ex2);

                    try {
                        ClientPoolFactory clientPoolFactory = this.clientPool.getClientPoolFactory();
                        this.clientPool.clear();
                        this.clientPool.close();
                        this.clientPool = null;
                        this.clientPool = new ClientPool(clientPoolFactory);

                        synchronized (this.clientPool) {
                            this.client = this.clientPool.borrowObject();
                        }
                    } catch (Exception ex3) {
                        LOG.error("Could not get a client after reloaded pool #3", ex3);
                    }
                }
            }
        }
        LOG.warn("Pool METRICS:" + this.clientPool.printPoolMetrics());
        return this.client;
    }

    @Override
    public Class getInterfaceClass() {
        return this.interfaceClass;
    }

    @Override
    public void returnClient() throws Exception {
        if (this.client != null) {
            synchronized (this.client) {
                try {
                    this.clientPool.returnObject(this.client);
                } catch (Exception ex) {
                    LOG.error("Could not return HyperGateClient into Pool", ex);
                }
                this.client = null;
            }
        }
    }

    @Override
    public void returnClient(IHyperGateClient hyperGateClient) throws Exception {
        if (hyperGateClient != null) {
            synchronized (hyperGateClient) {
                try {
                    this.clientPool.returnObject(hyperGateClient);
                } catch (Exception ex) {
                    LOG.error("Could not return HyperGateClient into Pool #2", ex);
                }
                hyperGateClient = null;
            }
        }
    }

}
