package com.reversemind.hypergate.integration.pojo.client;

import com.reversemind.hypergate.client.ClientPool;
import com.reversemind.hypergate.client.ClientPoolFactory;
import com.reversemind.hypergate.proxy.ProxyFactoryPool;
import com.reversemind.hypergate.proxy.ProxySendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import java.io.Serializable;

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
// TODO remove it in common part for EJB & POJO
public abstract class AbstractClientPOJO implements IClient, Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractClientPOJO.class);

    protected ProxyFactoryPool proxyFactoryPool = null;
    private static ClientPool clientPool;

    public abstract String getClientBeanName();

    public abstract Class getClientBeanClass();

    /**
     * Need init during the start
     *
     */

    public void init() {
        this.localInit();
    }

    public void destroy() {
        // if need to destroy pool
        if(clientPool != null){
            synchronized (clientPool){
                clientPool.forceClearClose();
                clientPool.close();
            }
        }
    }

    /**
     * Default value is "META-INF/hypergate-client-context.xml"
     *
     * @return
     */
    @Override
    public String getContextXML() {
        return "META-INF/hypergate-client-context.xml";
    }

    private void initPool() {
        if (clientPool == null) {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.getContextXML());
            int poolSize = applicationContext.getBean("poolSize", java.lang.Integer.class);
            LOG.info("Pool start size:" + poolSize);
            ClientPoolFactory clientPoolFactory = new ClientPoolFactory(this.getContextXML(), this.getClientBeanName(), this.getClientBeanClass());
            clientPool = new ClientPool(clientPoolFactory, poolSize);
            LOG.info("Client pool now is RUNNING");
        }
        LOG.info("Client pool already initialized");
    }

    protected void localInit() {
        this.initPool();
        this.proxyFactoryPool = ProxyFactoryPool.getInstance();
    }

    protected void clientFullReconnect() throws Exception {
        long beginTime = System.currentTimeMillis();

        clientPool.forceClearClose();
        clientPool.clear();
        clientPool.close();
        clientPool = null;
        Thread.sleep(100);

        this.initPool();
        LOG.info("Reconnected for time:" + (System.currentTimeMillis() - beginTime) + " ms");
    }

    @Override
    public <T> T getProxy(Class<T> interfaceClass) throws Exception {

        if (clientPool == null) {
            this.clientFullReconnect();
            if (clientPool == null) {
                throw new Exception("HyperGate client is not running");
            }
        }

        if (this.proxyFactoryPool == null) {
            this.clientFullReconnect();
            if (this.proxyFactoryPool == null) {
                throw new Exception("Could not get proxyFactory for " + interfaceClass);
            }
        }

        T object = null;

        try {

            LOG.info("Going to create new newProxyInstance from proxyFactory" + this.proxyFactoryPool);
            LOG.info("Client pool is:" + clientPool);

            //object = (T)this.proxyFactory.newProxyInstance(interfaceClass);
            object = (T) this.proxyFactoryPool.newProxyInstance(this.clientPool, interfaceClass);

        } catch (Throwable th) {
            // com.reversemind.hypergate.proxy.ProxySendException: =HyperGate= Could not to send data into server: - let's reconnect
            Throwable throwableLocal = th.getCause();

            LOG.warn("some troubles with sending data to the server let's reconnect");

            if (throwableLocal.getClass().equals(ProxySendException.class)) {
                LOG.info("detected ProxySendException:" + throwableLocal.getMessage());
                this.clientFullReconnect();
                //object = (T)this.proxyFactory.newProxyInstance(interfaceClass);
                LOG.info("Client pool is:" + clientPool);
                object = (T) this.proxyFactoryPool.newProxyInstance(this.clientPool, interfaceClass);
            }

            if (throwableLocal.getCause().getClass().equals(Exception.class)) {
                throw new Exception("Could not to get proxy or send data to server");
            }

        }
        return object;
    }


}
