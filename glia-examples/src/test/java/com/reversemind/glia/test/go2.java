package com.reversemind.glia.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

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
public class go2 implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(go2.class);

    public static void main(String... args) {
        String gg = "{&quot;max_id&quot;:&quot;283150187722076159&quot;,&quot;has_more_items&quot;:true,&quot;items_html&quot;:&quot; \u003cli class=\\&quot;js-stream-item stream-item stream-item expanding-stream-item\\&quot; data-item-id=\\&quot;285605679744569344\\&quot; id=\\&quot;stream-item-tweet-285605679744569344\\&quot; data-item-type=\\&quot;tweet\\&quot;\\u003e\\n \\n\\n\\n \\n\\n\\n \\u003cdiv class=\\&quot;tweet original-tweet js-stream-tweet js-actionable-tweet js-profile-popup-actionable js-original-tweet \\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\&quot; \\ndata-feedback-key=\\&quot;stream_status_285605679744569344\\&quot;\\ndata-tweet-id=\\&quot;285605679744569344\\&quot;";


        String values = gg.replaceAll("\\&quot;", "'").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&").replaceAll("&apos;", "'")
                //.replaceAll("&quot;","'")
                .replaceAll("\\n{1,}", " ").
                        replaceAll("\\'", "'")
                .replaceAll("\u003c", "<")
                .replaceAll("\u003e", ">");
        LOG.debug(values);
    }

}
