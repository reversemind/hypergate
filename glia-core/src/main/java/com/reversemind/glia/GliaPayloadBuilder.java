package com.reversemind.glia;

import java.io.Serializable;

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
public class GliaPayloadBuilder implements Serializable {

    /**
     * @param gliaPayloadStatus
     * @return
     */
    public static GliaPayload buildErrorPayload(GliaPayloadStatus gliaPayloadStatus) {
        GliaPayload gliaPayload = new GliaPayload();
        gliaPayload.setServerTimestamp(System.currentTimeMillis());
        gliaPayload.setStatus(gliaPayloadStatus);
        gliaPayload.setThrowable(null);
        return gliaPayload;
    }

    public static GliaPayload buildErrorPayload(GliaPayloadStatus gliaPayloadStatus, Throwable throwable) {
        GliaPayload gliaPayload = new GliaPayload();
        gliaPayload.setServerTimestamp(System.currentTimeMillis());
        gliaPayload.setStatus(gliaPayloadStatus);
        gliaPayload.setThrowable(throwable);
        return gliaPayload;
    }

}
