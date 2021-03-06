/**
 * Copyright (c) 2013-2015 Eugene Kalinin
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
 *
 */

package com.reversemind.hypergate.proxy;

import com.reversemind.hypergate.Payload;
import com.reversemind.hypergate.client.IHyperGateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 */
public abstract class AbstractProxyHandler implements InvocationHandler {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractProxyHandler.class);

    private Payload makePayload() {
        return new Payload();
    }

    public abstract IHyperGateClient getClient() throws Exception;

    public abstract Class getInterfaceClass();

    public abstract void returnClient() throws Exception;

    public abstract void returnClient(IHyperGateClient hyperGateClient) throws Exception;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        IHyperGateClient _hyperGateClient = null;

        if(this.getClient() == null){
            LOG.debug(" this.getClient() is NULL - " + Thread.currentThread().getName());
            this.returnClient();
            throw new RuntimeException("Client is null");
        }

        synchronized (this.getClient()) {
            _hyperGateClient = this.getClient();
        }

        if (_hyperGateClient == null) {
            LOG.debug(" _hyperGateClient is NULL - " + Thread.currentThread().getName());
            this.returnClient();
            throw new RuntimeException("Client is null");
        }

        synchronized (_hyperGateClient) {
            LOG.debug("\n\n\n" + "!!!!!!!!!!!!!!!!\n" + "Invoke REMOTE METHOD\n\n\n");

            LOG.debug("Method:" + method.getName());
            if (args != null && args.length > 0) {
                for (Object obj : args) {
                    if (obj != null)
                        LOG.debug("arguments: " + obj.getClass().getCanonicalName() + " value:" + obj);
                }
            }

            if (method.getName().equalsIgnoreCase("toString")) {
                return "You are calling for toString method - it's not a good idea :)";
            }

            Payload payload = this.makePayload();

            payload.setClientTimestamp(System.currentTimeMillis());
            payload.setMethodName(method.getName());
            payload.setArguments(args);
            payload.setInterfaceClass(this.getInterfaceClass());

            LOG.debug(" Payload created on client:" + payload);
            LOG.debug(" hyperGateClient:" + _hyperGateClient);
            if (_hyperGateClient != null) {
                LOG.warn(" HyeprGate is running hyperGateClient:" + _hyperGateClient.isRunning());
            }

            // TODO need to refactor this catcher
            try {
                _hyperGateClient.send(payload);
            } catch (IOException ex) {
                LOG.error("hyperGateClient.send(payload);", ex);
                LOG.error("HyperGateClient going to restart a client and send again data");
                _hyperGateClient.restart();

                try {
                    _hyperGateClient.send(payload);
                } catch (IOException ex2) {
                    LOG.error("After second send - exception", ex2);
                    this.returnClient(_hyperGateClient);
                    throw new ProxySendException("Could not to send data into server - let's reconnect");
                }
            }

            long bT = System.currentTimeMillis();
            Payload fromServer = _hyperGateClient.getPayload();
            this.returnClient(_hyperGateClient);

            if(fromServer == null){
                throw new NullPointerException("Get NPE from remote server");
            }

            if (fromServer != null && fromServer.getThrowable() != null) {
                // TODO What if impossible to load a specific Class
                if (fromServer.getThrowable().getCause() == null) {
                    Constructor constructor = fromServer.getThrowable().getClass().getConstructor(new Class[]{String.class});
                    String[] exceptionMessage = {fromServer.getThrowable().getMessage()};
                    throw (Throwable) constructor.newInstance(exceptionMessage);
                } else {
                    Constructor constructor = fromServer.getThrowable().getCause().getClass().getConstructor(new Class[]{String.class});
                    String[] exceptionMessage = {fromServer.getThrowable().getCause().getMessage()};
                    throw (Throwable) constructor.newInstance(exceptionMessage);
                }
            }

            LOG.debug("==2.5 Get back from server for:" + (System.currentTimeMillis() - bT) + " ms");

            if (fromServer != null && fromServer.getStatus() != null) {
                return fromServer.getResultResponse();
            }

        } //synchronized

        this.returnClient(_hyperGateClient);

        return null;
    }

}
