package ejb.client;

import com.reversemind.glia.integration.ejb.client.ClientEJBDiscovery;

import javax.annotation.PostConstruct;
import javax.ejb.*;
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
//@Singleton
//@Startup
@Stateless
@Local(ClientSimple.class)
public class ClientSimple extends ClientEJBDiscovery implements Serializable {

    /**
     * Should be started even after a zookeeper and server
     */
//    @PostConstruct
//    public void init(){
//        // Wait a little -
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        super.init();
//    }

//    @Lock(LockType.READ)
    @Override
    public <T> T getProxy(Class<T> interfaceClass) throws Exception {
        return super.getProxy(interfaceClass);
    }

}
