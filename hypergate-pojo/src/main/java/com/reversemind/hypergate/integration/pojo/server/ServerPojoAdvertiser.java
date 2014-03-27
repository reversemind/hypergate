package com.reversemind.hypergate.integration.pojo.server;

import com.reversemind.hypergate.server.AbstractContainerHyperGateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 */
@Component
public class ServerPojoAdvertiser extends AbstractContainerHyperGateServer implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(ServerPojoAdvertiser.class);

    @Override
    public String getServerBuilderName() {
        return SERVER_ADVERTISER_BUILDER_NAME;
    }

    @Override
    public String getContextXML() {
        return SERVER_DEFAULT_CONTEXT_NAME;
    }
}
