package com.reversemind.hypergate.zookeeper;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.LocaleServiceProviderPool;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 *
 * Copyright (c) 2013-2014 Eugene Kalinin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class EmbeddedZookeeper extends ZooKeeperServerMain implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(EmbeddedZookeeper.class);

    private static EmbeddedZookeeper zookeeperServer;
    private static ServerConfig serverConfig;

    public static String EMBEDDED_ZOOKEEPER_PORT = "12181";
    public static String EMBEDDED_ZOOKEEPER_DIRECTORY = System.getProperty("java.io.tmpdir");

    /**
     * Just need port number usually it's 2181
     * And temp directory
     *
     * String port = "2181";
     * String dataDirectory = System.getProperty("java.io.tmpdir");
     *
     * EmbeddedZookeeper.start(new String[]{port, dataDirectory});
     *
     * @param configPath
     */
    public static void start(String[] configPath) {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        serverConfig = new ServerConfig();
        serverConfig.parse(configPath);
        zookeeperServer = new EmbeddedZookeeper();
        (new Thread(zookeeperServer)).start();
    }

    /**
     * Default Zookeeper directory - is /java.io.tmpdir/{new Date().getTime()}
     */
    public static void start() {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        serverConfig = new ServerConfig();
        serverConfig.parse(new String[]{EMBEDDED_ZOOKEEPER_PORT, EMBEDDED_ZOOKEEPER_DIRECTORY + File.separator + "" + new Date().getTime() + "--" + UUID.randomUUID().toString(),"2000", "100"});
        zookeeperServer = new EmbeddedZookeeper();
        (new Thread(zookeeperServer)).start();
    }

    /**
     *
     */
    public static void stop() {
        if(zookeeperServer != null){
            zookeeperServer.shutdown();
        }
    }

    @Override
    public void run() {
        try {
            zookeeperServer.runFromConfig(serverConfig);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
