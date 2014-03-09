package com.test.timer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
public class TimerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TimerTest.class);

    @Test
    public void testTimer() throws InterruptedException {
        TimerTask timerTask = new MetricsUpdateTask();

        Timer timer;
        timer = new Timer();
        timer.schedule(timerTask, 10000, 10000);

        Thread.sleep(30000);
    }

    private class MetricsUpdateTask extends TimerTask {

        long count = 0;

        @Override
        public void run() {
            LOG.info("\n\n " + count++ + " - " + new Date());
        }
    }

}
