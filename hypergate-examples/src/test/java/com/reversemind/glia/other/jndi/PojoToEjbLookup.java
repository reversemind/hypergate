package com.reversemind.glia.other.jndi;

import cluster.AddressSearchResult;
import cluster.IAddressSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

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
public class PojoToEjbLookup implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(PojoToEjbLookup.class);

    /**
     * // DOC - https://docs.jboss.org/author/display/AS72/Remote+EJB+invocations+via+JNDI+-+EJB+client+API+or+remote-naming+project
     * // DOC - https://docs.jboss.org/author/display/AS72/EJB+invocations+from+a+remote+client+using+JNDI
     *
     * @param args
     * @throws NamingException
     */

    public static void main(String... args) throws NamingException {

        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.PROVIDER_URL, "remote://localhost:4447");                             // PROVIDER_URL = java.naming.provider.url
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");                            // URL_PKG_PREFIXES = java.naming.factory.url.pkgs
        jndiProperties.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");

        // !!!!! it's property important for JBoss 7.1.1
        jndiProperties.put("jboss.naming.client.ejb.context", true);

//        jndiProperties.put("remote.connections", "default");
//        jndiProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
//        jndiProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");

//
//        jndiProperties.put(Context.SECURITY_PRINCIPAL, "kalinin@transtk.ru");
//        jndiProperties.put(Context.SECURITY_CREDENTIALS, "Rtls1q2w3e4r3e");


        final Context context = new InitialContext(jndiProperties);


//        java:global/building/building-ejb-0.5-SNAPSHOT/SimpleRemoteEJB!ru.ttk.test.interfaces.ISimpleRemoteEJB
//        java:app/building-ejb-0.5-SNAPSHOT/SimpleRemoteEJB!ru.ttk.test.interfaces.ISimpleRemoteEJB
//        java:module/SimpleRemoteEJB!ru.ttk.test.interfaces.ISimpleRemoteEJB
//        java:jboss/exported/building/building-ejb-0.5-SNAPSHOT/SimpleRemoteEJB!ru.ttk.test.interfaces.ISimpleRemoteEJB
//        java:global/building/building-ejb-0.5-SNAPSHOT/SimpleRemoteEJB
//        java:app/building-ejb-0.5-SNAPSHOT/SimpleRemoteEJB
//        java:module/SimpleRemoteEJB
//
//        String path = "ejb:building/building-ejb-0.5-SNAPSHOT/SimpleRemoteEJB!ru.ttk.test.interfaces.ISimpleRemoteEJB";

        //
        //String path = "java:global/address/address-ejb-1.0-SNAPSHOT/AddressSearch!cluster.IAddressSearch";
        String path = "ejb:address/address-ejb-1.0-SNAPSHOT/AddressSearch!cluster.IAddressSearch";
        //String path = "ejb:/address-ejb-1.0-SNAPSHOT//AddressSearch!cluster.IAddressSearch";
        final IAddressSearch addressSearch = (IAddressSearch) context.lookup(path);

        List<AddressSearchResult> list = addressSearch.doSearch("Чонгарский");
        if (list != null && list.size() > 0) {
            for (AddressSearchResult result : list) {
                LOG.debug("--" + result);
            }
        }

    }

}
