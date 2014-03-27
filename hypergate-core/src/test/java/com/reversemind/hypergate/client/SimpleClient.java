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
