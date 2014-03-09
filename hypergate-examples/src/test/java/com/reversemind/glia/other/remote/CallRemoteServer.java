package com.reversemind.glia.other.remote;

import cluster.AddressSearchResult;
import cluster.IAddressSearch;
import com.reversemind.hypergate.client.HyperGateClient;
import com.reversemind.hypergate.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
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
public class CallRemoteServer implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CallRemoteServer.class);

    public static void main(String... args) throws Exception {

        LOG.debug("Run client");

        int serverPort = 7000;
        String serverHost = "localhost";

        HyperGateClient client = new HyperGateClient(serverHost, serverPort);
        client.start();

        IAddressSearch addressSearch = (IAddressSearch) ProxyFactory.getInstance().newProxyInstance(client, IAddressSearch.class);

        List<AddressSearchResult> list = addressSearch.doSearch("Чонгарский");
        if (list != null && list.size() > 0) {
            for (AddressSearchResult result : list) {
                LOG.debug("--" + result);
            }
        }

    }

}