package ejb.server;

import com.reversemind.glia.integration.ejb.server.ServerEJBAdvertiser;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.Serializable;

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
@Startup
@Singleton
public class ServerSimple extends ServerEJBAdvertiser implements Serializable {

    @PostConstruct
    public void init(){
        try {
            // Cause zookeeper should be started earlier
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.init();
    }

    @Override
    public String getContextXML(){
        return "META-INF/glia-server-context.xml";
    }
}
