package com.reversemind.glia.test.pojo.server;

import com.reversemind.glia.server.GliaPayloadProcessor;
import com.reversemind.glia.server.GliaServerFactory;
import com.reversemind.glia.server.IGliaServer;
import com.reversemind.glia.test.pojo.shared.ISimplePojo;
import com.reversemind.glia.test.pojo.shared.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

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
public class RunServer implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(RunServer.class);

    public static void main(String... args) {

        GliaPayloadProcessor gliaPayloadProcessor = new GliaPayloadProcessor();
        gliaPayloadProcessor.registerPOJO(ISimplePojo.class, SimplePojo.class);

        IGliaServer server = GliaServerFactory.builder()
                .setPayloadWorker(gliaPayloadProcessor)
                .setPort(Settings.SERVER_PORT)
                .setAutoSelectPort(false)
                .setKeepClientAlive(false)
                .setUseMetrics(true)
                .build();

        LOG.info("Started on port:" + server.getPort());
        server.start();
    }

}
