/**
 * Copyright (c) 2013-2015 Eugene Kalinin
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

package com.reversemind.hypergate.server;

import com.reversemind.hypergate.Payload;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class ServerFactoryTest {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ServerFactoryTest.class);

    /**
     * Need a setPayloadWorker
     */
    @Test
    public void testBuilderNoPayloadWorker() {
        IHyperGateServer server = null;
        try {
            server = ServerFactory.builder().build();
            assertNotNull(server);
        } catch (RuntimeException ex) {
            LOG.error("Runtime error:", ex);
        }
        assertNull(server);
    }

    /**
     * AutoSelectPort is true and setPort is 8000 - should be selected auto setPort number
     * <p></p>
     * 'Cause priority to .setAutoSelectPort(true)
     */
    @Test
    public void testAutoSelectPortNumber() {
        IHyperGateServer server = ServerFactory.builder()
                .setAutoSelectPort(true)
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

        assertNotNull(server);
        // 'cause .setAutoSelectPort(true)
        assertNotEquals(8000, server.getPort());

        if (server != null) {
            server.shutdown();
        }
        assertNotNull(server);

        server = ServerFactory.builder()
                .setAutoSelectPort(true)
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

        assertNotNull(server);

        LOG.info("port number:" + server.getPort());

        if (server != null) {
            server.shutdown();
        }
        assertNotNull(server);
    }

    /**
     *
     */
    @Test
    public void testAssignPortNumber() {

        IHyperGateServer server = ServerFactory.builder()
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
                })
                .setAutoSelectPort(false)
                .setPort(8000)
                .build();

        LOG.info("port number:" + server.getPort());
        assertEquals(8000, server.getPort());

        server.shutdown();
        assertNotNull(server);
    }

    @Test
    public void testSetNames() {

        final String NAME = "HYPERGATE_SERVER_NAME";
        final String NAME_INSTANCE = "HYPERGATE_SERVER_INSTANCE_NAME";

        IHyperGateServer hyperGateServer = ServerFactory.builder()
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
                })
                .setAutoSelectPort(true)
                .setName(NAME)
                .setInstanceName(NAME_INSTANCE)
                .build();

        assertEquals(NAME, hyperGateServer.getName());
        assertEquals(NAME_INSTANCE, hyperGateServer.getInstanceName());

        if (hyperGateServer != null) {
            hyperGateServer.shutdown();
        }
        assertNotNull(hyperGateServer);
    }

}
