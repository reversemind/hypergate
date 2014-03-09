package com.reversemind.glia.test.json.server;

import com.reversemind.glia.test.json.Settings;
import com.reversemind.glia.test.json.shared.IDoSomething;
import com.reversemind.glia.test.json.shared.JSONBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
 *
 * POJO on server side to process remote client request through a IDoSomething interface call
 */
public class ServerPojo implements IDoSomething {

    private static final Logger LOG = LoggerFactory.getLogger(ServerPojo.class);

    private static final Map<String, Object> ADDRESS_MAP = new HashMap<String, Object>() {
        {
            put("id#1", "St Louis, MO, USA");
            put("id#2", "Cansas Drive, Hopkinsville, KY, United States");
            put("id#3", "Chicago Ridge, IL, United States");
        }
    };

    /**
     * JSON string should be something like this {"searchAddress":"Chicago"}
     *
     * @param jsonString
     * @return
     */
    @Override
    public String doExtraThing(String jsonString) {
        try {

            LOG.debug("JSON string is:" + jsonString);
            Map<String, Object> queryMap = JSONBuilder.build(jsonString);

            LOG.debug("Going to search for address:" + queryMap.get(Settings.ADDRESS_SEARCH));

            Map<String, Object> responseMap = ADDRESS_MAP;
            responseMap.put("#id4", queryMap.get(Settings.ADDRESS_SEARCH));

            responseMap.put(Settings.SEARCH_STATUS, Settings.SEARCH_STATUS_OK);

            return JSONBuilder.build(responseMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get back an Error
        return "{" + Settings.SEARCH_STATUS + ":" + Settings.SEARCH_STATUS_ERROR + "}";
    }

    @Override
    public String doExtraThing(String jsonString, String otherParameter) {
        LOG.debug("Just for testing overrided functions - I've got a second parameter:" + otherParameter);
        return this.doExtraThing(jsonString);
    }

}
