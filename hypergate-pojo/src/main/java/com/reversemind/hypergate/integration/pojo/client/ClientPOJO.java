package com.reversemind.hypergate.integration.pojo.client;

import com.reversemind.hypergate.client.AbstractContainerHyperGateClient;
import com.reversemind.hypergate.client.HyperGateClient;

import java.io.Serializable;

/**
 * Copyright (c) 2013-2014 Eugene Kalinin
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
public class ClientPOJO extends AbstractClientPOJO implements IClient, Serializable {

    @Override
    public String getClientBeanName() {
        return AbstractContainerHyperGateClient.CLIENT_SIMPLE_BUILDER_NAME;
    }

    @Override
    public Class getClientBeanClass() {
        return AbstractContainerHyperGateClient.CLASS_HYPERGATE_CLIENT;
    }
}
