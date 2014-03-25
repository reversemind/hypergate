package com.reversemind.hypergate.proxy;

import com.reversemind.hypergate.client.IHyperGateClient;

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
public class ProxyHandler extends AbstractProxyHandler implements InvocationHandler {

    private IHyperGateClient client;
    private Class interfaceClass;

    public ProxyHandler(IHyperGateClient client, Class interfaceClass) {
        this.client = client;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public IHyperGateClient getClient() throws Exception {
        return this.client;
    }

    @Override
    public Class getInterfaceClass() {
        return this.interfaceClass;
    }

    @Override
    public void returnClient() {
    }

    @Override
    public void returnClient(IHyperGateClient hyperGateClient) throws Exception {
    }
}
