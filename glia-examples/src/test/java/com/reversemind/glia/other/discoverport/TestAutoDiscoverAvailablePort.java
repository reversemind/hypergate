package com.reversemind.glia.other.discoverport;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

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
public class TestAutoDiscoverAvailablePort {

    private static final Logger LOG = LoggerFactory.getLogger(TestAutoDiscoverAvailablePort.class);

    @Ignore
    @Test
    public void testFindFreePort() {
        try {

            ServerSocket serverSocket = new ServerSocket(0);
            LOG.debug("serverSocket.isBound():" + serverSocket.isBound());
            LOG.debug("port:" + serverSocket.getLocalPort());

            serverSocket.close();
            LOG.debug("serverSocket.isBound():" + serverSocket.isBound());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
