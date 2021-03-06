package com.reversemind.glia.other.spring;

import com.reversemind.hypergate.Payload;
import com.reversemind.hypergate.server.ServerFactory;
import com.reversemind.hypergate.server.IPayloadProcessor;
import com.reversemind.hypergate.server.IHyperGateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;
import java.util.Map;

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
public class GliaServerSpringContextLoader implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(GliaServerSpringContextLoader.class);

    public static void main(String... args) throws InterruptedException {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/hypergate-server-context.xml");

        ServerFactory.Builder builderAdvertiser = applicationContext.getBean("serverBuilderAdvertiser", ServerFactory.Builder.class);

        LOG.debug("--------------------------------------------------------");
        LOG.debug("Builder properties:");
        LOG.debug("Name:" + builderAdvertiser.getName());
        LOG.debug("Instance Name:" + builderAdvertiser.getInstanceName());
        LOG.debug("port:" + builderAdvertiser.getPort());
        LOG.debug("isAutoSelectPort:" + builderAdvertiser.isAutoSelectPort());

        LOG.debug("Type:" + builderAdvertiser.getType());

        LOG.debug("Zookeeper connection string:" + builderAdvertiser.getZookeeperHosts());
        LOG.debug("Zookeeper base path:" + builderAdvertiser.getServiceBasePath());


        IHyperGateServer server = builderAdvertiser.build();

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

        Thread.sleep(60000);

        server.shutdown();


        ServerFactory.Builder builderSimple = (ServerFactory.Builder) applicationContext.getBean("serverBuilderSimple");
        LOG.debug("" + builderSimple.port());

        IHyperGateServer serverSimple = builderSimple
                .setAutoSelectPort(true)
                .setName("N A M E")
                .setPort(8000)
                .setPayloadWorker(new IPayloadProcessor() {
                    @Override
                    public Map<Class, Class> getPojoMap() {
                        return null;
                    }

                    @Override
                    public void setPojoMap(Map<Class, Class> map) {
                    }

                    @Override
                    public void setEjbMap(Map<Class, String> map) {
                    }

                    @Override
                    public void registerPOJO(Class interfaceClass, Class pojoClass) {
                    }

                    @Override
                    public Payload process(Object payloadObject) {
                        return null;
                    }
                }).build();


        LOG.debug("\n\n");
        LOG.debug("--------------------------------------------------------");
        LOG.debug("Simple Glia server");
        LOG.debug("\n");
        LOG.debug("Server properties:");
        LOG.debug("......");
        LOG.debug("Name:" + serverSimple.getName());
        LOG.debug("Instance Name:" + serverSimple.getInstanceName());
        LOG.debug("port:" + serverSimple.getPort());

    }

}
