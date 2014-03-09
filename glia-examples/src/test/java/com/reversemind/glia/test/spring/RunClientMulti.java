package com.reversemind.glia.test.spring;

import com.reversemind.glia.client.GliaClient;
import com.reversemind.glia.proxy.ProxyFactory;
import com.reversemind.glia.test.pojo.shared.ISimplePojo;
import com.reversemind.glia.test.pojo.shared.PAddressNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

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
public class RunClientMulti implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(RunClientMulti.class);

    static GliaClient client;

    public static void main(String... args) throws Exception {
        LOG.debug("Run client");

        int serverPort = 7000;
        String serverHost = "localhost";

        client = new GliaClient(serverHost, serverPort);
        client.start();

        Thread[] threadArray = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threadArray[i] = new Thread() {
                long vl = System.currentTimeMillis();

                public void run() {

                    for (int i = 0; i < 1; i++) {
                        ISimplePojo simplePojoProxy = (ISimplePojo) ProxyFactory.getInstance().newProxyInstance(client, ISimplePojo.class);

                        List<PAddressNode> list = simplePojoProxy.searchAddress(this.getName());

                        if (list != null && list.size() > 0) {
                            for (PAddressNode addressNode : list) {
                                LOG.debug("" + this.getName() + "-node:" + addressNode);
                            }
                        }
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }

                }
            };
            //Thread.sleep(1);
        }

        for (int i = 0; i < 10; i++) {
            threadArray[i].start();
        }

    }

}
