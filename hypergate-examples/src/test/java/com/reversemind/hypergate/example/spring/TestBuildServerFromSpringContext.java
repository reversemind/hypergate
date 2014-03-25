package com.reversemind.hypergate.example.spring;

import com.reversemind.hypergate.server.IHyperGateServer;
import com.reversemind.hypergate.server.IPayloadProcessor;
import com.reversemind.hypergate.server.ServerFactory;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.*;

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
 *
 * Create HyperGateServer from Spring Context
 *
 */
public class TestBuildServerFromSpringContext {

    private static final Logger LOG = LoggerFactory.getLogger(TestBuildServerFromSpringContext.class);

    @Test
    public void testSpringContext() throws InterruptedException {

        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/hypergate-server-context.xml");

        IPayloadProcessor payloadProcessor = (IPayloadProcessor) context.getBean("serverPayloadProcessor");

        Map<Class, Class> map = payloadProcessor.getPojoMap();
        Set<Class> set = map.keySet();
        for (Class clazz : set) {
            LOG.debug(clazz.getCanonicalName() + "|" + map.get(clazz).getCanonicalName());
        }

        ServerFactory.Builder serverBuilder = (ServerFactory.Builder) context.getBean("simpleHyperGateServerBuilder");

        assertNotNull(serverBuilder);
        LOG.info("serverBuilder: " + serverBuilder);


        IHyperGateServer hyperGateServer = serverBuilder.build();
        assertNotNull(hyperGateServer);

        hyperGateServer.start();
        Thread.sleep(1000);
        hyperGateServer.shutdown();

        assertTrue(!hyperGateServer.isRunning());
    }

}
