package com.reversemind.glia.proxy;

import com.reversemind.glia.client.ClientPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

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
public class ProxyFactoryPool {

    private final static Logger LOG = LoggerFactory.getLogger(ProxyFactoryPool.class);
    private static final ProxyFactoryPool proxyFactoryPool = new ProxyFactoryPool();

    private ProxyFactoryPool() {
    }

    public static ProxyFactoryPool getInstance() {
        return proxyFactoryPool;
    }

    public Object newProxyInstance(ClientPool clientPool, Class interfaceClass) {
        // TODO make map for classLoader - key is a interfaceClass.name
        ClassLoader classLoader = interfaceClass.getClassLoader();
        LOG.debug("GLIA PROXY FACTORY clientPool" + clientPool);
        LOG.debug("GLIA PROXY FACTORY classLoader:" + classLoader);
        return Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, new ProxyHandlerPool(clientPool, interfaceClass));
    }

}
