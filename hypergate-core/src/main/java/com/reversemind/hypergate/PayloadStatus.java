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

package com.reversemind.hypergate;

/**
 *
 */
public enum PayloadStatus {

    ERROR_UNKNOWN("ERROR_UNKNOWN", -1),                                                 //
    ERROR_CLIENT_PAYLOAD("ERROR_CLIENT_PAYLOAD", -100),                                 //
    ERROR_PAYLOAD_UNKNOWN_METHOD("ERROR_PAYLOAD_UNKNOWN_METHOD", -111),                 //
    ERROR_COULD_NOT_INIT_JNDI_CONTEXT("ERROR_COULD_NOT_INIT_JNDI_CONTEXT", -222),       //
    ERROR_SERVER_TIMEOUT("ERROR_SERVER_TIMEOUT", -333),                                 // 
    OK("OK", 0);

    private String message;
    private int code;

    PayloadStatus(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static PayloadStatus get(int code) {
        switch (code) {
            case 0:
                return OK;
            case -1:
                return ERROR_UNKNOWN;
            case -100:
                return ERROR_CLIENT_PAYLOAD;
            case -111:
                return ERROR_PAYLOAD_UNKNOWN_METHOD;
            case -222:
                return ERROR_COULD_NOT_INIT_JNDI_CONTEXT;
            case -333:
                return ERROR_SERVER_TIMEOUT;
            default:
                return ERROR_UNKNOWN;
        }
    }

    public static int get(PayloadStatus payloadStatus) {
        if (payloadStatus != null) {
            return payloadStatus.getCode();
        }
        return -1;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
