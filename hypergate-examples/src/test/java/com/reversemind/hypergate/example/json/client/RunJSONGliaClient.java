package com.reversemind.hypergate.example.json.client;

import com.reversemind.hypergate.client.HyperGateClient;
import com.reversemind.hypergate.proxy.ProxyFactory;
import com.reversemind.hypergate.example.json.Settings;
import com.reversemind.hypergate.example.json.shared.IDoSomething;
import com.reversemind.hypergate.example.json.shared.JSONBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 *
 * Copyright (c) 2013-2014 Eugene Kalinin
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
public class RunJSONGliaClient {

    private static final Logger LOG = LoggerFactory.getLogger(RunJSONGliaClient.class);

    public static void main(String... args) throws Exception {

        //
        HyperGateClient hyperGateClient = new HyperGateClient(Settings.SERVER_HOST, Settings.SERVER_PORT);
        hyperGateClient.start();

        // create proxy for remote service
        IDoSomething doSomething = (IDoSomething) ProxyFactory.getInstance().newProxyInstance(hyperGateClient, IDoSomething.class);

        // call remote server
        String jsonString = doSomething.doExtraThing(JSONBuilder.buildJSONQuery("Chicago"));
        LOG.debug("Server response: " + jsonString);


        LOG.debug("\n\nMake a second interface:");
        // call remote server
        jsonString = doSomething.doExtraThing(JSONBuilder.buildJSONQuery("Chicago"), "SIMPLE STRING");

        LOG.debug("Server response: " + jsonString);
//
//        // jut test yourself for little highload
//        for(int i=0;i<1;i++){
//            jsonString = doSomething.doExtraThing(JSONBuilder.buildJSONQuery("Chicago" + i));
//        }

        // let's parse a JSON string from server
        Map<String, Object> serverResponseMap = JSONBuilder.build(jsonString);

        // get status from response
        String serverResponseStatus = (String) serverResponseMap.get(Settings.SEARCH_STATUS);
        if (serverResponseStatus.equals(Settings.SEARCH_STATUS_OK)) {
            LOG.debug("Yep!");

            Set<String> keys = serverResponseMap.keySet();
            for (String key : keys) {
                LOG.debug(key + ":" + serverResponseMap.get(key));
            }
        }

        //Thread.sleep(3000);
        hyperGateClient.shutdown();
    }
}
