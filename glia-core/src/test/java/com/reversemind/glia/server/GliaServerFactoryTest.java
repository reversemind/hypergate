package com.reversemind.glia.server;

import com.reversemind.glia.GliaPayload;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.*;

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
public class GliaServerFactoryTest {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GliaServerFactoryTest.class);

    /**
     * Need a setPayloadWorker
     */
    @Test
    public void testBuilderNoPayloadWorker() {
        IGliaServer gliaServer = null;
        try {
            gliaServer = GliaServerFactory.builder().build();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
        assertNull(gliaServer);
    }

    /**
     * AutoSelectPort is true and setPort is 8000 - should be selected auto setPort number
     * <p></p>
     * 'Cause priority to .setAutoSelectPort(true)
     */
    @Test
    public void testAutoSelectPortNumber() {
        IGliaServer gliaServer = GliaServerFactory.builder()
                .setAutoSelectPort(true)
                .setPort(8000)
                .setPayloadWorker(new IGliaPayloadProcessor() {
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
                    public GliaPayload process(Object gliaPayloadObject) {
                        return null;
                    }
                }).build();

        assertNotNull(gliaServer);
        // 'cause .setAutoSelectPort(true)
        assertNotEquals(8000, gliaServer.getPort());


        if (gliaServer != null) {
            gliaServer.shutdown();
        }
        assertNotNull(gliaServer);


        gliaServer = GliaServerFactory.builder()
                .setAutoSelectPort(true)
                .setPayloadWorker(new IGliaPayloadProcessor() {
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
                    public GliaPayload process(Object gliaPayloadObject) {
                        return null;
                    }
                }).build();

        assertNotNull(gliaServer);

        LOG.info("port number:" + gliaServer.getPort());

        if (gliaServer != null) {
            gliaServer.shutdown();
        }
        assertNotNull(gliaServer);
    }

    @Test
    public void testAssignPortNumber() {

        IGliaServer gliaServer = GliaServerFactory.builder()
                .setPayloadWorker(new IGliaPayloadProcessor() {
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
                    public GliaPayload process(Object gliaPayloadObject) {
                        return null;
                    }
                })
                .setAutoSelectPort(false)
                .setPort(8000)
                .build();

        LOG.info("port number:" + gliaServer.getPort());
        assertEquals(8000, gliaServer.getPort());

        if (gliaServer != null) {
            gliaServer.shutdown();
        }
        assertNotNull(gliaServer);
    }

    @Test
    public void testSetNames() {

        final String NAME = "GLIA_SERVER_NAME";
        final String NAME_INSTANCE = "GLIA_SERVER_INSTANCE_NAME";

        IGliaServer gliaServer = GliaServerFactory.builder()
                .setPayloadWorker(new IGliaPayloadProcessor() {
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
                    public GliaPayload process(Object gliaPayloadObject) {
                        return null;
                    }
                })
                .setAutoSelectPort(true)
                .setName(NAME)
                .setInstanceName(NAME_INSTANCE)
                .build();

        assertEquals(NAME, gliaServer.getName());
        assertEquals(NAME_INSTANCE, gliaServer.getInstanceName());

        if (gliaServer != null) {
            gliaServer.shutdown();
        }
        assertNotNull(gliaServer);
    }

    /**
     * // TODO need Netflix Curator - fake zookeeper instance
     */
    @Ignore
    @Test
    public void testAutoDiscovery() {

        final String ZOOKEEPER_HOST = "localhost";
        final int ZOOKEEPER_PORT = 2181;

    }

}
