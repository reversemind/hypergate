package com.test.pool;

import com.reversemind.hypergate.client.IHyperGateClient;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * Copyright (c) 2013 Eugene Kalinin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ClientFactory extends BasePoolableObjectFactory<IHyperGateClient> {

    private String contextXML;
    private String beanName;
    private Class<? extends IHyperGateClient> clientClazz;

    /**
     *
     * @param contextXML
     * @param clientClazz
     * @param beanName
     */
    public ClientFactory(String contextXML, String beanName, Class<? extends IHyperGateClient> clientClazz) {
        this.contextXML = contextXML;
        this.beanName = beanName;
        this.clientClazz = clientClazz;
    }

    @Override
    public IHyperGateClient makeObject() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.contextXML);
        IHyperGateClient client = applicationContext.getBean(this.beanName, this.clientClazz);
        client.start();
        return client;
    }

    public void destroyObject(IHyperGateClient client) throws Exception  {
        if(client != null){
            client.shutdown();
            client = null;
        }
    }

    public void activateObject(IHyperGateClient client) throws Exception {

    }

    public void passivateObject(IHyperGateClient client) throws Exception {

    }

}
