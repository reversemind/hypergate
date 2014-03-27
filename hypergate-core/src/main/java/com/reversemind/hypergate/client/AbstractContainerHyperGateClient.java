package com.reversemind.hypergate.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 */
public abstract class AbstractContainerHyperGateClient {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractContainerHyperGateClient.class);

    protected static final String CLIENT_SIMPLE_BUILDER_NAME = "hyperGateClient";
    protected static final String CLIENT_DISCOVERY_BUILDER_NAME = "hyperGateClientServerDiscovery";

    protected static final Class CLASS_HYPERGATE_CLIENT = HyperGateClient.class;
    protected static final Class CLASS_HYPERGATE_CLIENT_SERVER_DISCOVERY = HyperGateClientServerDiscovery.class;

    protected static final String CLIENT_DEFAULT_CONTEXT_NAME = "META-INF/hypergate-client-context.xml";

    private IHyperGateClient hyperGateClient;

    public IHyperGateClient getHyperGateClient() {
        return this.hyperGateClient;
    }

    public abstract String getClientBuilderName();

    public abstract Class getClientBeanClass();

    /**
     * "META-INF/hypergate-client-context.xml";
     *
     * @return - should be something like "META-INF/hypergate-client-context.xml"
     */
    public abstract String getContextXML();

    @PostConstruct
    public void init() throws Exception {
        LOG.debug("this.getContextXML():" + this.getContextXML());
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.getContextXML());

        // TODO NEED BUILDER
        this.hyperGateClient = (IHyperGateClient) applicationContext.getBean(this.getClientBuilderName(), this.getClientBeanClass());

        if (this.hyperGateClient == null) {
            throw new RuntimeException("Could not build hyperGateClient");
        }

        this.hyperGateClient.start();
    }

    @PreDestroy
    public void destroy() {
        if (this.hyperGateClient != null) {
            this.hyperGateClient.shutdown();
            LOG.warn("HyperGate client:" + this.hyperGateClient.getName() + " destroyed");
            this.hyperGateClient = null;
        }
    }
}
