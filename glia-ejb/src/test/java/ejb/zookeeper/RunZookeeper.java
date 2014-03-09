package ejb.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RunZookeeper {

    private static final Logger LOG = LoggerFactory.getLogger(RunZookeeper.class);

    public static void start(){
        String port = "2181";
        String dataDirectory = System.getProperty("java.io.tmpdir");

        LocalZookeeper.start(new String[]{port, dataDirectory});
        LOG.info(dataDirectory);
    }

    public static void stop(){
        LocalZookeeper.stop();
    }

    public static void main(String... args) throws InterruptedException {
        String port = "2181";
        String dataDirectory = System.getProperty("java.io.tmpdir");

        LocalZookeeper.start(new String[]{port, dataDirectory});
        LOG.info(dataDirectory);

        Thread.sleep(60000);
        LocalZookeeper.stop();
    }

}
