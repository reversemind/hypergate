package com.reversemind.glia.test.json.server;

import com.reversemind.glia.server.GliaPayloadProcessor;
import com.reversemind.glia.server.GliaServerFactory;
import com.reversemind.glia.server.IGliaPayloadProcessor;
import com.reversemind.glia.server.IGliaServer;
import com.reversemind.glia.test.json.Settings;
import com.reversemind.glia.test.json.shared.IDoSomething;
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
public class RunJSONGliaServer {

    private static final Logger LOG = LoggerFactory.getLogger(RunJSONGliaServer.class);

    public static void main(String... args) throws InterruptedException {

        IGliaPayloadProcessor gliaPayloadProcessor = new GliaPayloadProcessor();
        gliaPayloadProcessor.registerPOJO(IDoSomething.class, ServerPojo.class);

        IGliaServer gliaServer = GliaServerFactory.builder()
                .setPayloadWorker(gliaPayloadProcessor)
                .setName("GLIA_JSON_SERVER")
                .setPort(Settings.SERVER_PORT)
                .setKeepClientAlive(true)
                .setAutoSelectPort(false)
                .build();

        gliaServer.start();

        int count = 0;
        // just wait for a minute 5 sec * 120 = 10 minutes
        while (count++ < 120) {
            Thread.sleep(5000);
            LOG.debug("" + gliaServer.getMetrics());
        }

        gliaServer.shutdown();
    }
}
