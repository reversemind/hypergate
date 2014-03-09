package com.reversemind.glia.server;

import com.reversemind.glia.GliaPayload;

import java.io.Serializable;
import java.util.Map;

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
public interface IGliaPayloadProcessor extends Serializable {

    public Map<Class, Class> getPojoMap();

    public void setPojoMap(Map<Class, Class> map);

    public void setEjbMap(Map<Class, String> map);

    public void registerPOJO(Class interfaceClass, Class pojoClass);

    /**
     * Should be thread safe
     *
     * @param gliaPayloadObject
     * @return
     */
    public GliaPayload process(Object gliaPayloadObject);
}
