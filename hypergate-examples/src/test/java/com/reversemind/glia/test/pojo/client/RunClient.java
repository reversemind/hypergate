package com.reversemind.glia.test.pojo.client;

import com.reversemind.glia.client.GliaClient;
import com.reversemind.glia.proxy.ProxyFactory;
import com.reversemind.glia.test.pojo.shared.ISimplePojo;
import com.reversemind.glia.test.pojo.shared.PAddressNode;
import com.reversemind.glia.test.pojo.shared.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
public class RunClient {

    private final static Logger LOG = LoggerFactory.getLogger(RunClient.class);

    public static void main(String... args) throws Exception {

        LOG.info("Run Client");

        GliaClient client = new GliaClient(Settings.SERVER_HOST, Settings.SERVER_PORT);
        client.start();

        ISimplePojo simplePojoProxy = (ISimplePojo) ProxyFactory.getInstance().newProxyInstance(client, ISimplePojo.class);

//        simplePojoProxy.searchAddress("123");


        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("12");
        list.add("123");
        list.add("1234");

        List<PAddressNode> result = simplePojoProxy.searchAddress("12", list);

        LOG.info("gogo - " + result);

        Thread.sleep(2000);
        client.shutdown();



//        LOG.info("Run Client");
//
//        GliaClient client = new GliaClient(Settings.SERVER_HOST, Settings.SERVER_PORT);
//        client.start();
//
//        ISimplePojo simplePojoProxy = (ISimplePojo) ProxyFactory.getInstance().newProxyInstance(client, ISimplePojo.class);
//
//        List<PAddressNode> list = simplePojoProxy.searchAddress("Moscow");
//
//        if (list != null && list.size() > 0) {
//            int count = 0;
//            for (PAddressNode addressNode : list) {
//                if(count++ % 10000 == 0){
//                    LOG.info("node:" + addressNode);
//                }
//            }
//        }
//        client.shutdown();
//
//        Thread.sleep(2000);
//
//        LOG.info("Restart client");
//        client.restart();
//
//        Thread.sleep(100);
//
//        list = simplePojoProxy.searchAddress("Moscow");
//
//        if (list != null && list.size() > 0) {
//            int count = 0;
//            for (PAddressNode addressNode : list) {
//                if(count++ % 10000 == 0){
//                    LOG.info("node:" + addressNode);
//                }
//            }
//        }
//
//        client.shutdown();

    }

}
