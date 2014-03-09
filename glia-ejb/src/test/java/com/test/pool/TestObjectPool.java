package com.test.pool;

import com.reversemind.glia.client.GliaClientServerDiscovery;
import com.reversemind.glia.client.IGliaClient;
import org.apache.commons.pool.impl.GenericObjectPool;

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
public class TestObjectPool {

    public static void main(String... args) throws Exception {
        ClientFactory clientFactory = new ClientFactory("META-INF/glia-client-context.xml","gliaClientServerDiscovery", GliaClientServerDiscovery.class);
        GenericObjectPool<IGliaClient> pool = new GenericObjectPool<IGliaClient>(clientFactory, 5);

        IGliaClient gliaClient = pool.borrowObject();
    }

}
