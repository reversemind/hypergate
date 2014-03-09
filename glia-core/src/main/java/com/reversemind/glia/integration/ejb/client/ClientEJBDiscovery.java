package com.reversemind.glia.integration.ejb.client;

import com.reversemind.glia.client.GliaClientServerDiscovery;

import javax.ejb.Stateless;
import java.io.Serializable;

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
 * <p/>
 * a. Remove pojo and jpa modules
 * Pojo extension
 * <extension module="org.jboss.as.osgi"/>
 * <!-- Remove this line extension module="org.jboss.as.pojo"/-->
 * <extension module="org.jboss.as.remoting"/>
 * And pojo domain
 * <subsystem xmlns="urn:jboss:domain:naming:1.0" />
 * <!--subsystem xmlns="urn:jboss:domain:pojo:1.0" /-->
 * <subsystem xmlns="urn:jboss:domain:osgi:1.0" activation="lazy">
 */
@Stateless
public class ClientEJBDiscovery extends AbstractClientEJB implements IClientEJB, Serializable {

    public String getGliaClientBeanName() {
        return "gliaClientServerDiscovery";
    }

    public Class getGliaClientBeanClass() {
        return GliaClientServerDiscovery.class;
    }
}
