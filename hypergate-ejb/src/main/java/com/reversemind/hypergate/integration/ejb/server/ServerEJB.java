package com.reversemind.hypergate.integration.ejb.server;

import com.reversemind.hypergate.server.AbstractContainerHyperGateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import java.io.Serializable;

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
@Singleton
public class ServerEJB extends AbstractContainerHyperGateServer implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(ServerEJB.class);

    @Override
    public String getServerBuilderName() {
        return SERVER_SIMPLE_BUILDER_NAME;
    }

    @Override
    public String getContextXML() {
        return SERVER_DEFAULT_CONTEXT_NAME;
    }

}
