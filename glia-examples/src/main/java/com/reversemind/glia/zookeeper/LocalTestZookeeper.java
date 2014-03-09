package com.reversemind.glia.zookeeper;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;

import java.io.IOException;

/**
 *
 * Copyright (c) 2013 Eugene Kalinin
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
public class LocalTestZookeeper extends ZooKeeperServerMain implements Runnable {

    private static LocalTestZookeeper zookeeperServer;
    private static ServerConfig serverConfig;

    /**
     * Just need port number usually it's 2181
     * And temp directory
     *
     * @param configPath
     */
    public static void start(String[] configPath) {
        serverConfig = new ServerConfig();
        serverConfig.parse(configPath);
        zookeeperServer = new LocalTestZookeeper();
        (new Thread(zookeeperServer)).start();
    }

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
