package com.reversemind.glia.other.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.*;

/**
 *
 * Copyright (c) 2013-2014 Eugene Kalinin
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
public class FutureTask_Test2 implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(FutureTask_Test2.class);

    public static final long TIME_OUT_FOR_PAYLOAD = 2000; // sec

    public static void main(String... args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        FutureTask<String> future = new FutureTask<String>(
                new Callable<String>() {
                    public String call() {
                        long bT = System.currentTimeMillis();
                        try {

                            Thread.sleep(3000);

                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        return " delta:" + (System.currentTimeMillis() - bT);
                    }
                });


        executor.execute(future);

        String vvv = "EMPTY";
        try {
            vvv = future.get(4, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            LOG.error("TimeoutException == HERE", e);
            future.cancel(true);
        }

        LOG.debug("VVV:" + vvv);

        long timBegin = System.currentTimeMillis();
        try {
            while (!future.isDone()) {
                if ((System.currentTimeMillis() - timBegin) >= TIME_OUT_FOR_PAYLOAD) {
                    future.cancel(true);
                    executor.shutdown();
                    LOG.debug("DELTA TIME IS:" + (System.currentTimeMillis() - timBegin));
                    throw new InterruptedException("TIME IS UP for GETTING PAYLOAD");
                }
            }
        } catch (InterruptedException ex) {
            LOG.error("", ex);
        }


        if (future.isCancelled()) {
            LOG.debug("Task was canceled");
            executor.shutdown();
        }

        if (future.isDone()) {
            LOG.debug("Get from source:" + future.get());
            executor.shutdown();
        }

    }

}
