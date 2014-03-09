package com.reversemind.glia.other.jndi;

import cluster.IAddressSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class SpringRemoteEjb implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SpringRemoteEjb.class);

    public static void main(String... args) {

        // Still some trouble with Spring + EJB

        // DOC - https://docs.jboss.org/author/display/AS72/Remote+EJB+invocations+via+JNDI+-+EJB+client+API+or+remote-naming+project
        // DOC - https://docs.jboss.org/author/display/AS72/EJB+invocations+from+a+remote+client+using+JNDI
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-jndi.xml");


        final IAddressSearch addressSearch = (IAddressSearch) context.getBean("beanAddressSearch");

        LOG.debug("" + addressSearch.doSearch("Moscow"));

        //No Spring just POJO

    }

}
