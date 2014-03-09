package com.test.simple.pool;

import com.reversemind.glia.client.GliaClientServerDiscovery;
import com.reversemind.glia.client.IGliaClient;
import com.test.pool.ClientFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

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
public class TestSimpleObjectPool {

    private static final Logger LOG = LoggerFactory.getLogger(TestSimpleObjectPool.class);

    @Test
    public void testSimplePool() throws Exception {

        SimpleFactory simpleFactory = new SimpleFactory();
        GenericObjectPool<String> pool = new GenericObjectPool<String>(simpleFactory, 10, GenericObjectPool.WHEN_EXHAUSTED_GROW, 2 * 1000);

        String[] strs = new String[10];
        for (int i = 0; i < 10; i++) {
            strs[i] = pool.borrowObject();
        }

        for (String string : strs) {
            LOG.debug("|" + string);
        }

        Thread.sleep(5 * 1000);

        String extra = null;
        try {
            extra = pool.borrowObject();
        } catch (NoSuchElementException nex) {
            nex.printStackTrace();
            LOG.debug("!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.debug("!!!!");
        }


        LOG.debug("Get extra from pool:" + extra);
    }

}
