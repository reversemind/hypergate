package com.reversemind.glia.client;

import com.reversemind.glia.servicediscovery.serializer.ServerMetadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class ServerSelectorSimpleStrategy implements IServerSelectorStrategy, Serializable {

    public ServerMetadata selectServer(List<ServerMetadata> serverMetadataList) {
        if (serverMetadataList != null && serverMetadataList.size() > 0) {

            if (serverMetadataList.size() == 1) {
                return serverMetadataList.get(0);
            }

            Collections.sort(serverMetadataList, new Comparator<ServerMetadata>() {
                @Override
                public int compare(ServerMetadata o1, ServerMetadata o2) {
                    return (int) (o2.getMetrics().getStartDate().getTime() - o1.getMetrics().getStartDate().getTime());
                }
            });

            return serverMetadataList.get(0);
        }
        return null;
    }
}
