package com.reversemind.glia.test.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

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
 * Test JSON generation with
 */
public class TestJSON {

    private static final Logger LOG = LoggerFactory.getLogger(TestJSON.class);

    /**
     * Generate JSON string from Java Map and read a JSON string back into Map
     *
     * @throws IOException
     */

    @Ignore
    @Test
    public void testJSON() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> addressMap = new HashMap<String, Object>();

        addressMap.put("id#1", "St Louis, MO, USA");
        addressMap.put("id#2", "Cansas Drive, Hopkinsville, KY, United States");
        addressMap.put("id#3", "Chicago Ridge, IL, United States");

        List<Object> list = new ArrayList<Object>();
        list.add("001");
        list.add("002");
        list.add("003");

        addressMap.put("versions", list);

        String jsonString = mapper.writeValueAsString(addressMap);
        LOG.debug("" + jsonString);


        // Read JSON string back
        ObjectMapper mapperBack = new ObjectMapper();
        Map<String, Object> addressMapBack = mapperBack.readValue(jsonString, new TypeReference<Map<String, Object>>() {
        });

        Set<String> keys = addressMapBack.keySet();
        for (String key : keys) {
            LOG.debug(key + ":" + addressMapBack.get(key));
        }

    }

}
