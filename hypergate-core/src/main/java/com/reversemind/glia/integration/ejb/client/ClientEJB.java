package com.reversemind.glia.integration.ejb.client;

import com.reversemind.glia.client.GliaClient;

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
 *
 * @since 1.0
 */
@Stateless
public class ClientEJB extends AbstractClientEJB implements IClientEJB, Serializable {

    /**
     * Name look at glia-client-context.xml
     * <p/>
     * <bean id="gliaClientServerDiscovery" class="com.reversemind.glia.client.GliaClientServerDiscovery" scope="prototype">
     * <constructor-arg index="0" value="${glia.client.zookeeper.connection}" />
     * <constructor-arg index="1" value="${glia.client.zookeeper.base.path}" />
     * <constructor-arg index="2" value="${glia.client.service.name}" />
     * <constructor-arg index="3" value="${glia.client.timeout}" />
     * <constructor-arg index="4" ref="selectorStrategy" />
     * </bean>
     * <p/>
     * <p/>
     * <bean id="gliaClient" class="com.reversemind.glia.client.GliaClient" scope="prototype">
     * <constructor-arg index="0" value="${glia.client.service.host}" />
     * <constructor-arg index="1" value="${glia.client.service.port}" />
     * </bean>
     *
     * @return
     */
    public String getGliaClientBeanName() {
        return "gliaClient";
    }

    public Class getGliaClientBeanClass() {
        return GliaClient.class;
    }

}
