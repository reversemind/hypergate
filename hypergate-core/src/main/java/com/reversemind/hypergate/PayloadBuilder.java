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

import java.io.Serializable;

/**
 *
 */
public class PayloadBuilder implements Serializable {

    /**
     * @param payloadStatus
     * @return
     */
    public static Payload buildErrorPayload(PayloadStatus payloadStatus) {
        Payload payload = new Payload();
        payload.setServerTimestamp(System.currentTimeMillis());
        payload.setStatus(payloadStatus);
        payload.setThrowable(null);
        return payload;
    }

    public static Payload buildErrorPayload(PayloadStatus payloadStatus, Throwable throwable) {
        Payload payload = new Payload();
        payload.setServerTimestamp(System.currentTimeMillis());
        payload.setStatus(payloadStatus);
        payload.setThrowable(throwable);
        return payload;
    }

}
