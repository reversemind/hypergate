package com.reversemind.glia.test;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;

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
public class go3 implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(go3.class);

    public static void save(String fileName, String string) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
        bw.write(string + "\n");
        bw.flush();
        bw.close();


    }

    public static void main(String... args) throws Exception {

        System.setProperty("http.proxyHost", "10.105.0.217");
        System.setProperty("http.proxyPort", "3128");

        System.setProperty("https.proxyHost", "10.105.0.217");
        System.setProperty("https.proxyPort", "3128");


        HttpHost proxy = new HttpHost("10.105.0.217", 3128);
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpGet request = new HttpGet("https://twitter.com/i/profiles/show/splix/timeline/with_replies?include_available_features=1&include_entities=1&max_id=285605679744569344");
        HttpResponse response = client.execute(request);

// Get the response
        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

        String sl = "";
        String line = "";
        while ((line = rd.readLine()) != null) {
            LOG.debug(line);
            sl += line;
        }

        sl = new String(sl.getBytes(), "UTF-8");
        String sss = sl
                .replaceAll("\\{1,}", "\\")
                .replaceAll("\\\"", "'")
                .replaceAll("\\&quot;", "'")
                .replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&").replaceAll("&apos;", "'")
                .replaceAll("\u003c", "<")
                .replaceAll("\u003e", ">")
                .replaceAll("\n", " ")
                .replaceAll("\\/", "/")
                .replaceAll("\\'", "'");

        String sss2 = sss.replaceAll("\\'", "'");
        LOG.debug(sss);


        save("/opt/_del/go_sl.txt", sl);
        save("/opt/_del/go_sss.txt", sss);
        save("/opt/_del/go_line.txt", line);
        save("/opt/_del/go_sss2.txt", sss2);


        LOG.debug("\n\n\n\n\n");
        LOG.debug(sss);
        LOG.debug("\n\n\n\n\n");
        LOG.debug(URLDecoder.decode(sl, "UTF-8"));
        LOG.debug(URLDecoder.decode("\u0438\u043d\u043e\u0433\u0434\u0430", "UTF-8"));

        LOG.debug(URLDecoder.decode("\n            \u003c/span\u003e\n            \u003cb\u003e\n ", "UTF-8"));

    }

}
