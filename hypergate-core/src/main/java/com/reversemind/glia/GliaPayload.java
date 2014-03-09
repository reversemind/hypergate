package com.reversemind.glia;

import java.io.Serializable;
import java.util.Arrays;

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
 */
public class GliaPayload implements Serializable {

    private Object resultResponse;
    private String methodName;
    private Object[] arguments;
    private Class interfaceClass;

    private long clientTimestamp;
    private long serverTimestamp;

    private Throwable throwable;

    private GliaPayloadStatus status;

    public GliaPayload() {
        this.resultResponse = null;
        this.methodName = null;
        this.arguments = null;
        this.interfaceClass = null;
        this.clientTimestamp = System.currentTimeMillis();
        this.serverTimestamp = System.currentTimeMillis();
        this.status = GliaPayloadStatus.ERROR_UNKNOWN;
        this.throwable = null;
    }

    public Object getResultResponse() {
        return resultResponse;
    }

    public void setResultResponse(Object resultResponse) {
        this.resultResponse = resultResponse;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public long getClientTimestamp() {
        return clientTimestamp;
    }

    public void setClientTimestamp(long clientTimestamp) {
        this.clientTimestamp = clientTimestamp;
    }

    public long getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(long serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    public GliaPayloadStatus getStatus() {
        return status;
    }

    public void setStatus(GliaPayloadStatus status) {
        this.status = status;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "GliaPayload{" +
                "resultResponse=" + resultResponse +
                ", methodName='" + methodName + '\'' +
                ", arguments=" + (arguments == null ? null : Arrays.asList(arguments)) +
                ", interfaceClass=" + interfaceClass +
                ", clientTimestamp=" + clientTimestamp +
                ", serverTimestamp=" + serverTimestamp +
                ", status=" + status +
                ", throwable=" + throwable +
                '}';
    }
}
