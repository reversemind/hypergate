package com.reversemind.glia.integration.ejb.client;

import com.reversemind.glia.client.ClientPool;
import com.reversemind.glia.client.ClientPoolFactory;
import com.reversemind.glia.proxy.ProxyFactoryPool;
import com.reversemind.glia.proxy.ProxySendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
public abstract class AbstractClientEJB implements IClientEJB, Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractClientEJB.class);

    protected ProxyFactoryPool proxyFactoryPool = null;
    private static ClientPool clientPool;

    @PostConstruct
    public void init() {
        this.localInit();
    }

    @PreDestroy
    public void destroy() {
        // if need to destroy pool
    }

    public abstract String getGliaClientBeanName();

    public abstract Class getGliaClientBeanClass();

    /**
     * Default value is "META-INF/glia-client-context.xml"
     *
     * @return
     */
    @Override
    public String getContextXML() {
        return "META-INF/glia-client-context.xml";
    }

    private void initPool() {
        if (clientPool == null) {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.getContextXML());
            int poolSize = applicationContext.getBean("poolSize", java.lang.Integer.class);
            LOG.info("Pool start size:" + poolSize);
            ClientPoolFactory clientPoolFactory = new ClientPoolFactory(this.getContextXML(), this.getGliaClientBeanName(), this.getGliaClientBeanClass());
            clientPool = new ClientPool(clientPoolFactory, poolSize);
            LOG.info("Client pool RUN !!!");
        }
        LOG.info("Client pool already initialized");
    }

    protected void localInit() {
        this.initPool();
        this.proxyFactoryPool = ProxyFactoryPool.getInstance();
    }

    protected void clientFullReconnect() throws Exception {
        long beginTime = System.currentTimeMillis();

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
                throw new Exception("Glia client is not running");
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
            // com.reversemind.glia.proxy.ProxySendException: =GLIA= Could not to send data into server: - let's reconnect
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
