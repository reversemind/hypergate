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
 *
 */

package com.reversemind.hypergate.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SimpleClient extends AbstractContainerHyperGateClient{

    private final static Logger LOG = LoggerFactory.getLogger(SimpleClient.class);

    @Override
    public String getClientBuilderName() {
        return CLIENT_SIMPLE_BUILDER_NAME;
    }

    @Override
    public Class getClientBeanClass() {
        return CLASS_HYPERGATE_CLIENT;
    }

    @Override
    public String getContextXML() {
        return CLIENT_DEFAULT_CONTEXT_NAME;
    }
}
