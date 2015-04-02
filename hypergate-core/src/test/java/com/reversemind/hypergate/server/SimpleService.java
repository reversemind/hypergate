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

package com.reversemind.hypergate.server;

import com.reversemind.hypergate.shared.ISimpleService;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Fake SimpleService implementation on server side
 */
public class SimpleService implements ISimpleService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SimpleService.class);

    @Override
    public String getSimpleValue(String parameter) {
        LOG.info("get from remote client parameter:" + parameter);
        LOG.info("Sending from server response value:" + RETURN_VALUE + parameter + " at " + new Date().getTime() + " ms");
        return RETURN_VALUE + parameter;
    }
}
