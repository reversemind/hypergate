package com.reversemind.glia.server;

import com.esotericsoftware.kryo.Kryo;
import com.reversemind.glia.GliaPayload;
import com.reversemind.glia.serialization.*;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.Executors;

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
public abstract class GliaServer implements IGliaServer, Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(GliaServer.class);

    private String name;
    private String instanceName;
    private String host = "localhost";

    private boolean running = false;

    private ServerBootstrap serverBootstrap;
    private SimpleChannelUpstreamHandler handler;
    protected Metrics metrics;

    private int port;
    private boolean keepClientAlive = false;

    private GliaPayload gliaPayload;
    private IGliaPayloadProcessor gliaPayloadWorker;

    private final Kryo kryo = new KryoSettings().getKryo();
    private KryoDeserializer kryoDeserializer;

    /**
     * @param builder
     */
    public GliaServer(GliaServerFactory.Builder builder) {
        if (builder == null) {
            throw new RuntimeException("Builder is empty");
        }

        // parameter setAutoSelectPort has more priority than assigned setPort value
        if (builder.isAutoSelectPort()) {
            this.port = this.detectFreePort();
        } else if (builder.port() < 0) {
            this.port = this.detectFreePort();
        } else {
            this.port = builder.port();
        }

        if (builder.getPayloadWorker() == null) {
            throw new RuntimeException("Assign a setPayloadWorker to server!");
        }
        this.gliaPayloadWorker = builder.getPayloadWorker();

        // drop connection from client or not
        LOG.warn(" =GLIA= set value for KeepClientAlive from builder:" + builder.isKeepClientAlive());
        this.keepClientAlive = builder.isKeepClientAlive();

        this.name = StringUtils.isEmpty(builder.getName()) ? UUID.randomUUID().toString() : builder.getName();
        this.instanceName = StringUtils.isEmpty(builder.getInstanceName()) ? UUID.randomUUID().toString() : builder.getInstanceName();

        // TODO need to detect HOST NAME
        this.setHost(this.getIpAddress());

        this.metrics = new Metrics();

        this.kryoDeserializer = new KryoDeserializer(this.kryo);
    }

    private String getIpAddress() {
        Set<String> ipSet = new TreeSet<String>();
        Enumeration<NetworkInterface> n = null;
        try {
            n = NetworkInterface.getNetworkInterfaces();

            for (; n.hasMoreElements(); ) {
                NetworkInterface e = n.nextElement();
                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements(); ) {
                    InetAddress inetAddress = a.nextElement();
                    if (inetAddress.getHostAddress() != null) {
                        ipSet.add(inetAddress.getHostAddress());
                    }
                }
            }

            if (ipSet != null && ipSet.size() > 0) {
                for (String ip : ipSet) {
                    if (!ip.equals("127.0.0.1") & !ip.equalsIgnoreCase("localhost")) {
                        return ip;
                    }
                }
            }

        } catch (SocketException se) {
            // TODO need correct logging
            se.printStackTrace();
        }

        return "localhost";
    }

    /**
     * Detect free setPort on System
     *
     * @return
     */
    private int detectFreePort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            if (serverSocket.getLocalPort() == -1) {
                System.exit(-100);
                throw new RuntimeException("\n\nCould not start GliaServer there are no any free port in the system available");
            }

            int detectedPortNumber = serverSocket.getLocalPort();

            serverSocket.close();
            int count = 0;
            while (!serverSocket.isClosed()) {
                if (count++ > 10) {
                    throw new RuntimeException("Could not start GliaServer");
                }
                try {
                    Thread.sleep(100);
                    LOG.info("Waiting for closing auto discovered socket try number#" + count);
                } catch (InterruptedException e) {
                    System.exit(-100);
                    throw new RuntimeException("Could not start GliaServer");
                }
            }
            serverSocket = null;

            return detectedPortNumber;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not start GliaServer 'cause no any available free port in system");
    }

    /**
     * Get server setName
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Instance setName mostly prefer to use a UUID
     *
     * @return
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * Get setPort number of GliaServer
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Get server metrics
     *
     * @return
     */
    public Metrics getMetrics() {
        return this.metrics;
    }

    /**
     *
     */
    public void start() {
        // Configure the server.
        this.serverBootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        //this.handler = new GliaServerHandler(gliaPayloadWorker, metrics, keepClientAlive);
        this.handler = new GliaServerHandler(gliaPayloadWorker, metrics, keepClientAlive, this.kryoDeserializer);

        // Set up the pipeline factory
        // TODO add Kryo serializer
        this.serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new KryoObjectEncoder(),
                        new KryoObjectDecoder(
                                ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                        handler);
            }
        });

        // Bind and start to accept incoming connections.
        this.serverBootstrap.bind(new InetSocketAddress(port));

        // TODO use CORRECT LOGGGING
        LOG.debug(this.toString());
        LOG.warn("\n\nServer started\n\n");

        // TODO need to detect HOST NAME
        this.setHost(this.getIpAddress());
        this.running = true;
    }

    public boolean isRunning() {
        return this.running;
    }

    /**
     * Shutdown server
     */
    public void shutdown() {
        if (this.serverBootstrap != null) {
            this.serverBootstrap.releaseExternalResources();
            this.serverBootstrap.shutdown();

            this.port = -1;
            this.running = false;

            this.name = null;
            this.instanceName = null;
            this.host = null;
        }
    }

    private void setHost(String hostName) {
        // TODO remove from test implementation code
        this.host = hostName;
    }

    public String getHost() {
        // TODO remove from test implementation code
        return this.host;
    }

    @Override
    public String toString() {
        return "\n\n\n" +
                " GliaServer " +
                "\n-------------------" +
                "\n setName:" + this.name +
                "\n instance:" + this.instanceName +
                "\n port:" + this.port +
                "\n host:" + this.host +
                "\n metrics:" + this.metrics +
                "  \n\n\n";
    }

}
