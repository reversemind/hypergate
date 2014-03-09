package com.reversemind.glia.client;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class ClientPoolFactory extends BasePoolableObjectFactory<IGliaClient> {

    private String contextXML;
    private String beanName;
    private Class<? extends IGliaClient> clientClazz;
    private int poolSize;

    public ClientPoolFactory(String contextXML, String beanName, Class<? extends IGliaClient> clientClazz) {
        this.contextXML = contextXML;
        this.beanName = beanName;
        this.clientClazz = clientClazz;
    }

    @Override
    public IGliaClient makeObject() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.contextXML);
        IGliaClient client = applicationContext.getBean(this.beanName, this.clientClazz);
        if (client == null) {
            throw new RuntimeException("Could not create gliaClient for beanName:" + beanName + " and Class:" + this.clientClazz + " and contextName:" + this.contextXML);
        }
        client.start();
        return client;
    }

//    public void destroyObject(IGliaClient client) throws Exception {
//        LOG.warn("Going to destroy client:" + client);
//        if(client != null && client.isOccupied() == false){
//            client.shutdown();
//            client = null;
//            LOG.warn("Client destroyed");
//        }
//    }

    public void activateObject(IGliaClient client) throws Exception {

    }

    public void passivateObject(IGliaClient client) throws Exception {

    }

}
