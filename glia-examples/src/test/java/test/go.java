package test;

import com.reversemind.glia.server.GliaServerFactory;
import com.reversemind.glia.server.IGliaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;

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
public class go implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(go.class);

    public static void main(String... args) {
        System.setProperty("curator-log-events", "true");

        IGliaServer server;

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/glia-server-context.xml");
        GliaServerFactory.Builder builderAdvertiser = applicationContext.getBean("serverBuilder", GliaServerFactory.Builder.class);

        LOG.debug("--------------------------------------------------------");
        LOG.debug("Builder properties:");
        LOG.debug("Name:" + builderAdvertiser.getName());
        LOG.debug("Instance Name:" + builderAdvertiser.getInstanceName());
        LOG.debug("port:" + builderAdvertiser.getPort());
        LOG.debug("isAutoSelectPort:" + builderAdvertiser.isAutoSelectPort());

        LOG.debug("Type:" + builderAdvertiser.getType());

        LOG.debug("Zookeeper connection string:" + builderAdvertiser.getZookeeperHosts());
        LOG.debug("Zookeeper base path:" + builderAdvertiser.getServiceBasePath());


        server = builderAdvertiser.build();

        LOG.debug("\n\n");
        LOG.debug("--------------------------------------------------------");
        LOG.debug("After server initialization - properties");
        LOG.debug("\n");
        LOG.debug("Server properties:");
        LOG.debug("......");
        LOG.debug("Name:" + server.getName());
        LOG.debug("Instance Name:" + server.getInstanceName());
        LOG.debug("port:" + server.getPort());

        server.start();

        LOG.debug("Server started");
    }

}
