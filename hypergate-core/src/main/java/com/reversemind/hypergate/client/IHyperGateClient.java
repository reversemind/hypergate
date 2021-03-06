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

package com.reversemind.hypergate.client;

import com.reversemind.hypergate.Payload;

import java.io.IOException;
import java.io.Serializable;

public interface IHyperGateClient extends Serializable {

    public String getName();

    public void start() throws Exception;

    public void shutdown();

    public void restart() throws Exception;

    public void restart(String serverHost, int serverPort, long clientTimeOut) throws Exception;

    public boolean isRunning();

    public int getPort();

    public String getHost();

    public void send(Payload payloadSend) throws IOException;

    public Payload getPayload();

    public boolean isOccupied();
}
