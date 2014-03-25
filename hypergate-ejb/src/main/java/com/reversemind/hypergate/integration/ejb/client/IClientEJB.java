package com.reversemind.hypergate.integration.ejb.client;

import javax.ejb.Local;
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
@Local
public interface IClientEJB extends Serializable {

    public String getClientBeanName();

    public Class getClientBeanClass();

    public <T> Object getProxy(Class<T> interfaceClass) throws Exception;

    public String getContextXML();
}
