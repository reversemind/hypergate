package com.reversemind.glia.test.pojo.client;

import com.reversemind.glia.client.GliaClient;
import com.reversemind.glia.proxy.ProxyFactory;
import com.reversemind.glia.test.pojo.shared.ISimplePojo;
import com.reversemind.glia.test.pojo.shared.Settings;
import com.reversemind.glia.test.pojo.shared.SimpleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RunClientException {

    private final static Logger LOG = LoggerFactory.getLogger(RunClientException.class);

    public static void main(String... args) throws Exception {
        LOG.info("Run EXCEPTION client");

        GliaClient client = new GliaClient(Settings.SERVER_HOST, Settings.SERVER_PORT);
        client.start();

        ISimplePojo simplePojoProxy = (ISimplePojo) ProxyFactory.getInstance().newProxyInstance(client, ISimplePojo.class);

        LOG.info("\n\n=======================");

        try {
            String simple = simplePojoProxy.createException("Simple");
        } catch (SimpleException ex) {
            LOG.error("I've got an SimpleException:", ex);
        }

        client.shutdown();
    }
}
