package com.reversemind.glia.other.overriden;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Copyright (c) 2013 Eugene Kalinin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SimpleOverridenMethods {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleOverridenMethods.class);

    public String getSimple(String string) {
        LOG.debug("Argument #1:" + string);
        return "argument #1:" + string;
    }

    public String getSimple(String string, String otherString) {
        LOG.debug("Argument #1:" + string);
        LOG.debug("Argument #2:" + otherString);
        return "argument #1:" + string + " argument #2:" + otherString;
    }

    public String getSimple(int string, String otherString) {
        LOG.debug("Argument #1:" + string);
        LOG.debug("Argument #2:" + otherString);
        return "argument #1:" + string + " argument #2:" + otherString;
    }

}
