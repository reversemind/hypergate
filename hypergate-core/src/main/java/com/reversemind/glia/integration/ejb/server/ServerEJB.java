package com.reversemind.glia.integration.ejb.server;

import com.reversemind.glia.server.GliaServerFactory;
import com.reversemind.glia.server.IGliaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import java.io.Serializable;

/**
 * Copyright (c) 2013 Eugene Kalinin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Singleton
public class ServerEJB implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(ServerEJB.class);

    private IGliaServer server;

    @PostConstruct
    public void init() {

        //https://issues.apache.org/jira/browse/ZOOKEEPER-1554
        //System.setProperty("java.security.auth.login.config","/opt/zookeeper/conf/jaas.conf");
//        System.setProperty("java.security.auth.login.config","/opt/zookeeper/conf");
        System.setProperty("curator-log-events", "true");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.getContextXML());
        GliaServerFactory.Builder builderAdvertiser = applicationContext.getBean("serverBuilder", GliaServerFactory.Builder.class);

        LOG.info("--------------------------------------------------------");
        LOG.info("Builder properties:");
        LOG.info("Name:" + builderAdvertiser.getName());
        LOG.info("Instance Name:" + builderAdvertiser.getInstanceName());
        LOG.info("port:" + builderAdvertiser.getPort());
        LOG.info("isAutoSelectPort:" + builderAdvertiser.isAutoSelectPort());

        LOG.info("Type:" + builderAdvertiser.getType());

        LOG.info("Zookeeper connection string:" + builderAdvertiser.getZookeeperHosts());
        LOG.info("Zookeeper base path:" + builderAdvertiser.getServiceBasePath());


        this.server = builderAdvertiser.build();

        LOG.info("\n\n");
        LOG.info("--------------------------------------------------------");
        LOG.info("After server initialization - properties");
        LOG.info("\n");
        LOG.info("Server properties:");
        LOG.info("......");
        LOG.info("Name:" + this.server.getName());
        LOG.info("Instance Name:" + this.server.getInstanceName());
        LOG.info("port:" + this.server.getPort());

        this.server.start();

        LOG.info("Server started");

    }

    @PreDestroy
    public void destroy() {
        if (this.server != null) {
            //server SHUTDOWN
            this.server.shutdown();
            LOG.info("SERVER SHUTDOWN");
        }
    }

    public String getContextXML() {
        return "META-INF/glia-server-context.xml";
    }
}
