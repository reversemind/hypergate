package com.reversemind.hypergate.spring;

import com.reversemind.hypergate.client.AbstractContainerHyperGateClient;
import com.reversemind.hypergate.client.HyperGateClientServerDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class HyperGateClientSpringContextLoader implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(HyperGateClientSpringContextLoader.class);

    public static void main(String... args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/hypergate-client-context.xml");

        HyperGateClientServerDiscovery client = (HyperGateClientServerDiscovery) applicationContext.getBean(AbstractContainerHyperGateClient.CLIENT_DISCOVERY_BUILDER_NAME, AbstractContainerHyperGateClient.CLASS_HYPERGATE_CLIENT_SERVER_DISCOVERY);

        client.start();

        Thread.sleep(40000);

        client.shutdown();

    }

}
