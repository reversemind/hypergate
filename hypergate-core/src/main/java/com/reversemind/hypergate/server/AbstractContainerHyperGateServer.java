package com.reversemind.hypergate.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;

/**
 * AbstractContainerHyperGateServer - an abstract HyperGateServer for building inside container EJB or Spring
 *
 */
public abstract class AbstractContainerHyperGateServer implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractContainerHyperGateServer.class);

    protected static final String SERVER_SIMPLE_BUILDER_NAME = "serverBuilder";
    protected static final String SERVER_ADVERTISER_BUILDER_NAME = "serverBuilderAdvertiser";

    protected static final String SERVER_DEFAULT_CONTEXT_NAME = "META-INF/hypergate-server-context.xml";

    private IHyperGateServer server;

    public IHyperGateServer getServer() {
        return server;
    }

    /**
     * serverBuilder == for simple server NO Zookeeper
     * serverBuilderAdvertiser == for server Advertized via Zookeeper
     *
     * see hypergate-server-context.xml - or what context assigned via this.getContextXML()
     *
     * @return
     */
    public abstract String getServerBuilderName();

    /**
     * "META-INF/hypergate-server-context.xml";
     *
     * @return - should be something like "META-INF/hypergate-server-context.xml"
     */
    public abstract String getContextXML();

    @PostConstruct
    public void init() {

        //https://issues.apache.org/jira/browse/ZOOKEEPER-1554
        // System.setProperty("java.security.auth.login.config","/opt/zookeeper/conf/jaas.conf");
        // System.setProperty("java.security.auth.login.config","/opt/zookeeper/conf");
        System.setProperty("curator-log-events", "true");

        // TODO wrap it in Exception handler
        LOG.debug("this.getContextXML():" + this.getContextXML());
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(this.getContextXML());

        // "serverBuilderAdvertiser" - for server Advertized via Zookeeper - see hypergate-server-context.xml
        // "serverBuilder" - for server - Simple - see hypergate-server-context.xml
        ServerFactory.Builder builder = applicationContext.getBean(this.getServerBuilderName(), ServerFactory.Builder.class);

        LOG.info("--------------------------------------------------------");
        LOG.info("Builder properties:");
        LOG.info("Name:" + builder.getName());
        LOG.info("Instance Name:" + builder.getInstanceName());
        LOG.info("port:" + builder.getPort());
        LOG.info("isAutoSelectPort:" + builder.isAutoSelectPort());
        LOG.info("Keep client alive:" + builder.isKeepClientAlive());
        LOG.info("Type:" + builder.getType());

        if(builder.getType().equals(ServerFactory.Builder.Type.ZOOKEEPER_ADVERTISER)){
            LOG.info("Zookeeper connection string:" + builder.getZookeeperHosts());
            LOG.info("Zookeeper base path:" + builder.getServiceBasePath());
        }

        this.server = builder.build();

        LOG.info("\n\n");
        LOG.info("--------------------------------------------------------");
        LOG.info("After server initialization - properties");
        LOG.info("\n");
        LOG.info("Server properties:");
        LOG.info("......");
        LOG.info("Name:" + this.server.getName());
        LOG.info("Instance Name:" + this.server.getInstanceName());
        LOG.info("port:" + this.server.getPort());

        this.server.start();

        LOG.warn("Server started");
    }

    @PreDestroy
    public void destroy() {
        if (this.server != null) {
            //server SHUTDOWN
            this.server.shutdown();
            LOG.warn("SERVER SHUTDOWN");
        }
    }

//    public String getContextXML() {
//        return "META-INF/hypergate-server-context.xml";
//    }

}
