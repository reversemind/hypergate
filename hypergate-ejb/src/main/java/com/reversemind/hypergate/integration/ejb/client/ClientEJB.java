package com.reversemind.hypergate.integration.ejb.client;

import com.reversemind.hypergate.client.AbstractContainerHyperGateClient;
import com.reversemind.hypergate.client.HyperGateClient;

import javax.ejb.Stateless;
import java.io.Serializable;

/**
 * Copyright (c) 2013-2014 Eugene Kalinin
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
     * Name look at hypergate-client-context.xml
     * <p/>
     * <bean id="hyperGateClientServerDiscovery" class="com.reversemind.hypergate.client.HyperGateClientServerDiscovery" scope="prototype">
     * <constructor-arg index="0" value="${hypergate.client.zookeeper.connection}" />
     * <constructor-arg index="1" value="${hypergate.client.zookeeper.base.path}" />
     * <constructor-arg index="2" value="${hypergate.client.service.name}" />
     * <constructor-arg index="3" value="${hypergate.client.timeout}" />
     * <constructor-arg index="4" ref="selectorStrategy" />
     * </bean>
     * <p/>
     * <p/>
     * <bean id="hyperGateClient" class="com.reversemind.hypergate.client.HyperGateClient" scope="prototype">
     * <constructor-arg index="0" value="${hypergate.client.service.host}" />
     * <constructor-arg index="1" value="${hypergate.client.service.port}" />
     * </bean>
     *
     * @return
     */
    public String getClientBeanName() {
        return AbstractContainerHyperGateClient.CLIENT_SIMPLE_BUILDER_NAME;
    }

    public Class getClientBeanClass() {
        return AbstractContainerHyperGateClient.CLASS_HYPERGATE_CLIENT;
    }

}
