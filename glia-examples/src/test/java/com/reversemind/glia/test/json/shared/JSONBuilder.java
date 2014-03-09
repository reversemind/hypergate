package com.reversemind.glia.test.json.shared;

import com.reversemind.glia.test.json.Settings;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

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
 * Simple JSON builder for testing
 */
public class JSONBuilder {

    /**
     * @param addressObject - it's just a string - for example a city setName - Chicago
     * @return - JSON String {"searchAddress":"Chicago"}
     */
    public static String buildJSONQuery(String addressObject) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Settings.ADDRESS_SEARCH, addressObject);
        return build(map);
    }

    /**
     * Build JSON string from Java Map<String, Object>
     *
     * @param map
     * @return
     * @throws IOException
     */
    public static String build(Map<String, Object> map) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    /**
     * Build Java Map<String, Object> from JSON string
     *
     * @param JSONString
     * @return
     * @throws IOException
     */
    public static Map<String, Object> build(String JSONString) throws IOException {
        ObjectMapper mapperBack = new ObjectMapper();
        return mapperBack.readValue(JSONString, new TypeReference<Map<String, Object>>() {
        });
    }
}
