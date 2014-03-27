package com.reversemind.hypergate.server;

import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Don't forget that this SimpleServer will be started in console application so need to call init() and destroy()
 */
public class SimpleServer extends AbstractContainerHyperGateServer implements Serializable {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SimpleServer.class);

    @Override
    public String getServerBuilderName() {
        return SERVER_SIMPLE_BUILDER_NAME;
    }

    @Override
    public String getContextXML() {
        return SERVER_DEFAULT_CONTEXT_NAME;
    }
}
