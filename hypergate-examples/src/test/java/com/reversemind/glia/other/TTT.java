package com.reversemind.glia.other;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
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
public class TTT {

    private static final Logger LOG = LoggerFactory.getLogger(TTT.class);

    @Test
    public void testSorting() {

        LOG.debug("extracted number2:" + extractNumber2("вл.15 стр.3FIAS "));

        String number = "";
        String house = null;

        if (number == null) number = "";
        if (house == null) house = "";

        LOG.debug("equals number:" + number + " house:" + house + "|" + !(number != null ? !number.equals(house) : house != null));
        LOG.debug("Number empty:" + something(number, house));
    }

    private boolean something(String s1, String s2) {
        return StringUtils.isEmpty(s1);
    }

    private int extractNumber2(String string) {
        int value = 0;
        LOG.debug(string.replaceAll("[^0-9]", ""));
        try {
            value = Integer.parseInt(string.replaceAll("[^\\d]", ""));
        } catch (Exception ex) {
        } finally {
            return value;
        }
    }
}
