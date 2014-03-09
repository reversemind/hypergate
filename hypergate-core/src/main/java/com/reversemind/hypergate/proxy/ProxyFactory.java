package com.reversemind.hypergate.proxy;

import com.reversemind.hypergate.client.IHyperGateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

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
public class ProxyFactory implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(ProxyFactory.class);

    private static final ProxyFactory proxyFactory = new ProxyFactory();
    private Map<Class, ClassLoader> classLoaderMap;

    private ProxyFactory() {
        this.classLoaderMap = new HashMap<Class, ClassLoader>(1);
    }

    public static ProxyFactory getInstance() {
        return proxyFactory;
    }

    public Object newProxyInstance(IHyperGateClient client, Class interfaceClass) {

        ClassLoader classLoader = this.classLoaderMap.get(interfaceClass);
        if (classLoader == null) {
            classLoader = interfaceClass.getClassLoader();
            synchronized (this.classLoaderMap) {
                this.classLoaderMap.put(interfaceClass, classLoader);
            }
        }

        LOG.debug("PROXY FACTORY HyperGateClient:" + client);
        LOG.debug("PROXY FACTORY classLoader:" + classLoader);

        return Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, new ProxyHandler(client, interfaceClass));
    }
}
