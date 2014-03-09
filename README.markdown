# Glia - is a nervous system of you cluster

* Glia is distributed system for cluster node communication.
* Glia is simple, compact, fast, scalable, highly available, supports POJO and EJB model.
* Glia could be run as a standalone console application or inside any container (Servlet, EJB, Spring & etc.)
* Glia could be utilized by any JVM based language (Groovy, Scala, Clojure, JRuby, Jython & etc.)


Wiki definition of the word glia.

Glia (Greek γλία, γλοία "glue"; pronounced in English as either /ˈɡliːə/ or /ˈɡlaɪə/), are non-neuronal cells that maintain homeostasis, form myelin, and provide support and protection for neurons in the brain, and for neurons in other parts of the nervous system such as in the autonomic nervous system

Glia using [Netty](http://netty.io/), [Kryo](https://code.google.com/p/kryo/), [Curator](https://github.com/Netflix/curator), [Zookeeper](http://zookeeper.apache.org/)


#### Artifacts

Right now only in local TTK Nexus

groupId: com.reversemind

artifactId: glia-core

version: 1.9.2-SNAPSHOT

<dependency>
  <groupId>com.reversemind</groupId>
  <artifactId>glia-core</artifactId>
  <version>1.9.2-SNAPSHOT</version>
</dependency>

Stable is  1.9.2-SNAPSHOT - version


#### Getting started


#### Documentation
Documentation and tutorials on the [Glia Wiki](https://github.com/dendrite/viscosity/wiki/Glia-Wiki)

#### Logging
 Glia uses [SLF4J](http://www.slf4j.org/) for logging. SLF4J is a facade over logging that allows you to plug in any (or no) logging framework.

#### License

The use and distribution terms for this software are covered by the Apache License, Version 2.0 (http://opensource.org/licenses/Apache-2.0) which can be found in the file LICENSE.html at the root of this distribution. By using this software in any fashion, you are agreeing to be bound by the terms of this license. You must not remove this notice, or any other, from this software.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

#### TODO

* What about methods with collections types

* What about transactions??? Atmicos + Spring - it's an interesting solution

Java Open Transaction Manager (JOTM) - http://jotm.ow2.org/xwiki/bin/view/Main/WebHome
JBoss TS - http://www.jboss.org/jbosstm
Bitronix Transaction Manager (BTM) - http://docs.codehaus.org/display/BTM/Home
Atomikos - http://www.atomikos.com/


* ProxyHandler - What if impossible to load a specific Class
* look through is glia-server-context.xml - exists?? - just wrap it into exception!

* [DONE] Need GliaServerEJBContainer Arquillian JUnit Test

* [DONE] Create restart method for GliaServer & GliaClient
* [DONE] Add to Client a timeout as a parameter


* [DONE] Need to propagate from server to client an exception
* [DONE] Some troubles with overriden methods

* Build Client from a Builder pattern
* Publish in zookeeper a list (names - canonical java names) of interfaces inside a GliaServer
* [DONE] Make a typezation without cast IAskServer askServer = (IAskServer) gliaClientProxy.getProxy(IAskServer.class);

* [DONE] What about a reconnect - tested under EJB
* [DONE] If connection was lost from client - need to reconnection another instance - tested under EJB


!!!!!!!
Some troubles with Sigar under JBoss 7.1.1.Final


* [DONE] Extra troubles zookeeper under JBoss - it's a real bug - !(resume - it's not a bug it's feature and works great with some log extra info)
https://issues.apache.org/jira/browse/ZOOKEEPER-1696
https://issues.apache.org/jira/browse/ZOOKEEPER-1554


* GliaServerEJBContainer - right now tested only for JBoss 7.1.1.Final - test dependency for different application servers (GlassFish, JBoss)


* Need Netflix Curator fake zookeeper instance - for Testing
* What about Snappy-java
* Remove Sigar into new abstraction - 'cause GliaServer is very affectable

* Detect server host name - tested in - but some troubles with selecting correct values if a lot of net interfaces is up
* ?? Add to GliaClientDiscoverer - select server each time before send method

* Additional options for client speed optimisation - http://docs.jboss.org/netty/3.2/api/org/jboss/netty/bootstrap/ClientBootstrap.html
* In ProxyFactory - use non-static GliaClient
* Replace in ProxyFactory Interface class into String name of that interface class
* Add a control to server through zookeeper
* How to get from server number of client connected??

* Need look through all cases - but first situation looks pretty good
* CASE: if Server will send but Client is down?
* Need to make a stress loading from a lot of clients


* [DONE] - Make as parameter a timeout for Metrics publication
* [DONE] - but some shift to pure JavaBean in builder pattern - Need to fix Spring context loader
* [DONE] - Make a GliaServer & GliaClient Builder or Factory - 'cause number of constructors grows very rapidly
* [DONE] - Add CPU load to the server metrics
* [DONE] - Add to GliaClient or create a GliaClientDiscoverer - just automatically to find a necessary server for communication
* [DONE] - Update metrics dynamically in zookeeper
* [DONE] - Add to ServerMetadata a Metrics

* [DONE] - Add metrics to the server like a heartbeats & so on...

* [DONE] - Autodiscover server in zookeeper
* [DONE] - Glia server correct shutdown

* [DONE] Client correct wait - for a period - something like Future
* [DONE] for example a JSON sending - * 4 - Also made some pure POJO example integration





Fetures for JBoss 7.1.1 deployment

-- if You get something like that during deployment on Jboss 7.1.1.Final
 Service jboss.pojo."org.jboss.netty.internal.LoggerConfigurator".DESCRIBED is already registered

Just comment POJO modules in standalone.xml
https://community.jboss.org/wiki/JBPM-530FinalManualDeploymentGuideForBeginner#a_Remove_pojo_and_jpa_modules

a. Remove pojo and jpa modules
Pojo extension
<extension module="org.jboss.as.osgi"/>
<!-- Remove this line extension module="org.jboss.as.pojo"/-->
<extension module="org.jboss.as.remoting"/>
And pojo domain
<subsystem xmlns="urn:jboss:domain:naming:1.0" />
<!--subsystem xmlns="urn:jboss:domain:pojo:1.0" /-->
<subsystem xmlns="urn:jboss:domain:osgi:1.0" activation="lazy">




Look through - FutureTask

http://stackoverflow.com/questions/1247390/java-native-process-timeout/1249984#1249984

http://stackoverflow.com/questions/1234429/best-ways-to-handle-maximum-execution-time-for-threads-in-java

for jump start = http://habrahabr.ru/post/116363/





--------

Extension
<extension module="org.jboss.as.pojo"/>

And pojo domain
<subsystem xmlns="urn:jboss:domain:pojo:1.0"/>





---------

12:00:20,521 INFO  [stdout] (MSC service thread 1-6) 306 [MSC service thread 1-6] DEBUG org.apache.zookeeper.ClientCnxn  - zookeeper.disableAutoWatchReset is false
12:00:20,557 INFO  [stdout] (MSC service thread 1-6-SendThread(localhost:2181)) 339 [MSC service thread 1-6-SendThread(localhost:2181)] DEBUG org.apache.zookeeper.client.ZooKeeperSaslClient  - JAAS loginContext is: Client
12:00:20,569 INFO  [stdout] (MSC service thread 1-6-SendThread(localhost:2181)) 354 [MSC service thread 1-6-SendThread(localhost:2181)] WARN org.apache.zookeeper.client.ZooKeeperSaslClient  - Could not login: the client is being asked for a password, but the Zookeeper client code does not currently support obtaining a password from the user. Make sure that the client is configured to use a ticket cache (using the JAAS configuration setting 'useTicketCache=true)' and restart the client. If you still get this message after that, the TGT in the ticket cache has expired and must be manually refreshed. To do so, first determine if you are using a password or a keytab. If the former, run kinit in a Unix shell in the environment of the user who is running this Zookeeper client using the command 'kinit <princ>' (where <princ> is the name of the client's Kerberos principal). If the latter, do 'kinit -k -t <keytab> <princ>' (where <princ> is the name of the Kerberos principal, and <keytab> is the location of the keytab file). After manually refreshing your cache, restart this client. If you continue to see this message after manually refreshing your cache, ensure that your KDC host's clock is in sync with this host's clock.



------



13:33:39,968 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-5) JBAS015877: Stopped deployment address-ejb-1.0-SNAPSHOT.jar in 743ms
13:33:39,985 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) JBAS015877: Stopped deployment address-web-1.0-SNAPSHOT.war in 760ms
13:33:40,000 ERROR [stderr] (Timer-1) java.lang.IllegalStateException: instance must be started before calling this method
13:33:40,001 ERROR [stderr] (Timer-1)   at com.google.common.base.Preconditions.checkState(Preconditions.java:149)
13:33:40,001 ERROR [stderr] (Timer-1)   at com.netflix.curator.framework.imps.CuratorFrameworkImpl.create(CuratorFrameworkImpl.java:316)
13:33:40,001 ERROR [stderr] (Timer-1)   at com.netflix.curator.x.discovery.details.ServiceDiscoveryImpl.internalRegisterService(ServiceDiscoveryImpl.java:175)
13:33:40,002 ERROR [stderr] (Timer-1)   at com.netflix.curator.x.discovery.details.ServiceDiscoveryImpl.registerService(ServiceDiscoveryImpl.java:149)
13:33:40,002 ERROR [stderr] (Timer-1)   at com.reversemind.glia.servicediscovery.ServerAdvertiser.advertiseAvailability(ServerAdvertiser.java:55)
13:33:40,002 ERROR [stderr] (Timer-1)   at com.reversemind.glia.servicediscovery.ServiceDiscoverer.advertise(ServiceDiscoverer.java:78)
13:33:40,002 ERROR [stderr] (Timer-1)   at com.reversemind.glia.server.GliaServerAdvertiser.updateMetrics(GliaServerAdvertiser.java:93)
13:33:40,003 ERROR [stderr] (Timer-1)   at com.reversemind.glia.server.GliaServerAdvertiser$MetricsUpdateTask.run(GliaServerAdvertiser.java:127)
13:33:40,003 ERROR [stderr] (Timer-1)   at java.util.TimerThread.mainLoop(Timer.java:512)
13:33:40,003 ERROR [stderr] (Timer-1)   at java.util.TimerThread.run(Timer.java:462)
13:33:40,003 ERROR [stderr] (Timer-1) Exception in thread "Timer-1" java.lang.NoClassDefFoundError: com/google/common/base/Throwables
13:33:40,004 ERROR [stderr] (Timer-1)   at com.reversemind.glia.servicediscovery.ServerAdvertiser.advertiseAvailability(ServerAdvertiser.java:62)
13:33:40,004 ERROR [stderr] (Timer-1)   at com.reversemind.glia.servicediscovery.ServiceDiscoverer.advertise(ServiceDiscoverer.java:78)
13:33:40,004 ERROR [stderr] (Timer-1)   at com.reversemind.glia.server.GliaServerAdvertiser.updateMetrics(GliaServerAdvertiser.java:93)
13:33:40,004 ERROR [stderr] (Timer-1)   at com.reversemind.glia.server.GliaServerAdvertiser$MetricsUpdateTask.run(GliaServerAdvertiser.java:127)
13:33:40,005 ERROR [stderr] (Timer-1)   at java.util.TimerThread.mainLoop(Timer.java:512)
13:33:40,005 ERROR [stderr] (Timer-1)   at java.util.TimerThread.run(Timer.java:462)
13:33:40,005 ERROR [stderr] (Timer-1) Caused by: java.lang.ClassNotFoundException: com.google.common.base.Throwables from [Module "deployment.address.ear:main" from Service Module Loader]
13:33:40,005 ERROR [stderr] (Timer-1)   at org.jboss.modules.ModuleClassLoader.findClass(ModuleClassLoader.java:190)
13:33:40,005 ERROR [stderr] (Timer-1)   at org.jboss.modules.ConcurrentClassLoader.performLoadClassUnchecked(ConcurrentClassLoader.java:468)
13:33:40,006 ERROR [stderr] (Timer-1)   at org.jboss.modules.ConcurrentClassLoader.performLoadClassChecked(ConcurrentClassLoader.java:456)
13:33:40,006 ERROR [stderr] (Timer-1)   at org.jboss.modules.ConcurrentClassLoader.performLoadClassChecked(ConcurrentClassLoader.java:423)
13:33:40,006 ERROR [stderr] (Timer-1)   at org.jboss.modules.ConcurrentClassLoader.performLoadClass(ConcurrentClassLoader.java:398)
13:33:40,006 ERROR [stderr] (Timer-1)   at org.jboss.modules.ConcurrentClassLoader.loadClass(ConcurrentClassLoader.java:120)
13:33:40,007 ERROR [stderr] (Timer-1)   ... 6 more
