package com.reversemind.hypergate.client;

//import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class ClientPoolFactory extends BasePooledObjectFactory<IHyperGateClient> {

    private final static Logger LOG = LoggerFactory.getLogger(ClientPoolFactory.class);

    private String contextXML;
    private String beanName;
    private Class<? extends IHyperGateClient> clientClazz;
    private int poolSize;

    public ClientPoolFactory(String contextXML, String beanName, Class<? extends IHyperGateClient> clientClazz) {
        this.contextXML = contextXML;
        this.beanName = beanName;
        this.clientClazz = clientClazz;
    }

    @Override
    public IHyperGateClient create() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.contextXML);
        IHyperGateClient client = applicationContext.getBean(this.beanName, this.clientClazz);
        if (client == null) {
            throw new RuntimeException("Could not create client for beanName:" + beanName + " and Class:" + this.clientClazz + " and contextName:" + this.contextXML);
        }
        client.start();
        return client;
    }

    @Override
    public PooledObject<IHyperGateClient> wrap(IHyperGateClient obj) {
        DefaultPooledObject<IHyperGateClient> defaultPooledObject = new DefaultPooledObject<IHyperGateClient>(obj);
        return defaultPooledObject;
    }

    @Override
    public void destroyObject(PooledObject<IHyperGateClient> p) throws Exception  {
        if(p != null){
            IHyperGateClient hyperGateClient = p.getObject();
            this.destroyObject(hyperGateClient);
        }
    }

    public void destroyObject(IHyperGateClient client) throws Exception {
        LOG.warn("Going to destroy client:" + client);
        if(client != null) {
            int count = 10;
            while (client.isOccupied() == true || count-- > 0) {
                Thread.sleep(100);
                LOG.warn("Waiting for HyperGate:" + client.getName() + " times:" + count + " from 10");
            }
            client.shutdown();
            client = null;
        }
    }

}
