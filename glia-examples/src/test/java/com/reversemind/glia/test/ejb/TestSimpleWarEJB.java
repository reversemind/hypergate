package com.reversemind.glia.test.ejb;

import com.reversemind.glia.simple.SimpleEJB;
import com.reversemind.glia.simple.shared.ISimpleEJB;
import com.reversemind.glia.zookeeper.StartZookeeper;
import org.apache.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

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
@RunWith(Arquillian.class)
public class TestSimpleWarEJB {

    private final static Logger LOG = Logger.getLogger(TestSimpleWarEJB.class);

//    @Inject
//    GliaClient gliaClient;

    @Inject
    ISimpleEJB simpleEJB;

    @Deployment
    @TargetsContainer("jbossas-managed")
    public static Archive<?> createDeployment() {
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

        // Не стучимся на remote репы
        resolver.goOffline();

        WebArchive archive = ShrinkWrap.create(WebArchive.class, TestSimpleWarEJB.class.getName() + ".war")

                // .artifact("GROUPID:ARTIFACTID:TYPE:VERSION")
                .addAsLibraries(resolver
                        .artifact("org.springframework:spring-core:3.0.7.RELEASE")
                        .artifact("org.springframework:spring-context:3.0.7.RELEASE")

                        .artifact("postgresql:postgresql:9.1-901.jdbc4")

                        .artifact("org.apache.commons:commons-lang3:3.1")

                        .artifact("log4j:log4j:1.2.16")

                        .artifact("com.reversemind:glia-core:1.9.2-SNAPSHOT")

                        .artifact("net.sf.dozer:dozer:5.4.0")
                        .artifact("com.google.code.gson:gson:2.2.4")
                        .artifact("com.google.guava:guava:14.0.1")

                        .resolveAsFiles())

//                .addPackages(true, com.reversemind.glia.simple.GliaClient.class.getPackage())
//                .addPackages(true, StartZookeeper.class.getPackage())
//                .addPackages(true, GliaClient.class.getPackage())
                .addPackages(true, SimpleEJB.class.getPackage())

//                .addAsResource("META-INF/glia-interface-map.xml", "META-INF/glia-interface-map.xml")
//                .addAsResource("META-INF/glia-server-context.xml", "META-INF/glia-server-context.xml")
//                .addAsResource("META-INF/glia-server.properties", "META-INF/glia-server.properties")
//
//                .addAsResource("META-INF/glia-client-context.xml", "META-INF/glia-client-context.xml")
//                .addAsResource("META-INF/glia-client.properties", "META-INF/glia-client.properties")

                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        LOG.debug("archive:" + archive.toString(true));
        return archive;
    }

    //    @Ignore
    @Test
    public void testSomething() throws InterruptedException {

//        gliaClient
        StartZookeeper.start();

        Thread.sleep(10000);

        StartZookeeper.stop();

    }

    //    @Ignore
    @Test
    public void testSimple() throws Exception {
//        LOG.info(":Simple test");
//
//        ISimpleEJB simpleEJB = gliaClient.getProxy(ISimpleEJB.class);
    }

    @Test
    public void simpleTest() {
        LOG.debug("VALUE:=" + simpleEJB.getResult("sdfsdf"));
    }

}
