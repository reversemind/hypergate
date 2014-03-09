package com.reversemind.glia.proxy;

import com.reversemind.glia.client.IGliaClient;

import java.lang.reflect.InvocationHandler;

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
public class ProxyHandler extends AbstractProxyHandler implements InvocationHandler {

    private IGliaClient gliaClient;
    private Class interfaceClass;

    public ProxyHandler(IGliaClient gliaClient, Class interfaceClass) {
        this.gliaClient = gliaClient;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public IGliaClient getGliaClient() throws Exception {
        return this.gliaClient;
    }

    @Override
    public Class getInterfaceClass() {
        return this.interfaceClass;
    }

    @Override
    public void returnClient() {
    }

    @Override
    public void returnClient(IGliaClient gliaClient) throws Exception {
    }
}
