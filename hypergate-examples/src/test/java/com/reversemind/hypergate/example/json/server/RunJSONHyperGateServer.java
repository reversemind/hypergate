package com.reversemind.hypergate.example.json.server;

import com.reversemind.hypergate.server.IHyperGateServer;
import com.reversemind.hypergate.server.IPayloadProcessor;
import com.reversemind.hypergate.server.PayloadProcessor;
import com.reversemind.hypergate.server.ServerFactory;
import com.reversemind.hypergate.example.json.shared.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RunJSONHyperGateServer {

    private static final Logger LOG = LoggerFactory.getLogger(RunJSONHyperGateServer.class);

    public static void main(String... args) throws InterruptedException {

        IPayloadProcessor payloadProcessor = new PayloadProcessor();
        payloadProcessor.registerPOJO(IDoSomething.class, ServerPojo.class);

        IHyperGateServer hyperGateServer = ServerFactory.builder()
                .setPayloadWorker(payloadProcessor)
                .setName("HYPERGATE_JSON_SERVER")
                .setPort(Params.SERVER_PORT)
                .setKeepClientAlive(true)
                .setAutoSelectPort(false)
                .build();

        hyperGateServer.start();

        int count = 0;
        // just wait for a minute 5 sec * 120 = 10 minutes
        while (count++ < 120) {
            Thread.sleep(5000);
            LOG.debug("" + hyperGateServer.getMetrics());
        }

        hyperGateServer.shutdown();
    }
}
