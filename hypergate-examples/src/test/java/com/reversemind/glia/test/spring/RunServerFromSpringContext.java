package com.reversemind.glia.test.spring;

import com.reversemind.hypergate.server.HyperGateServer;
import com.reversemind.hypergate.server.IPayloadProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

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
public class RunServerFromSpringContext implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(RunServerFromSpringContext.class);

    public static void main(String... args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/glia-server-context.xml");

        IPayloadProcessor payloadProcessor = (IPayloadProcessor) context.getBean("serverPayloadProcessor");

        Map<Class, Class> map = payloadProcessor.getPojoMap();
        Set<Class> set = map.keySet();
        for (Class clazz : set) {
            LOG.debug(clazz.getCanonicalName() + "|" + map.get(clazz).getCanonicalName());
        }

        HyperGateServer server = (HyperGateServer) context.getBean("gliaServer");
        if (server != null) {
            LOG.debug("!!!!");
        }

        server.start();
    }

}