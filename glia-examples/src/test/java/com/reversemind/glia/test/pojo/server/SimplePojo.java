package com.reversemind.glia.test.pojo.server;

import com.reversemind.glia.test.pojo.shared.ISimplePojo;
import com.reversemind.glia.test.pojo.shared.PAddressNode;
import com.reversemind.glia.test.pojo.shared.SimpleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class SimplePojo implements ISimplePojo, Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(SimplePojo.class);

    @Override
    public List<PAddressNode> searchAddress(String query) {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            LOG.error("InterruptedException -- ", e);
        }

        List<PAddressNode> list = new ArrayList<PAddressNode>();
        for (int i = 0; i < 100000; i++) {
//        for (int i = 0; i < 1; i++) {
            list.add(new PAddressNode("" + i, " city - " + query + "_" + i));
        }
        return list;
    }

    @Override
    public List<PAddressNode> searchAddress(String vv, List<String> query) {
        LOG.info("LIST QUERY:" + query);
        LOG.info("Correctly detected method");

        return new ArrayList<PAddressNode>();
    }

    @Override
    public String createException(String query) throws SimpleException {
        throw new SimpleException("-Simple exception message-");
    }
}
