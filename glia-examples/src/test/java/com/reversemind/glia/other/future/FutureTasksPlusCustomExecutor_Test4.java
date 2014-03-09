package com.reversemind.glia.other.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.*;

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
public class FutureTasksPlusCustomExecutor_Test4 implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(FutureTasksPlusCustomExecutor_Test4.class);

    public static void main(String... args) {

        try {

            ExecutorService executor = new ThreadPoolExecutor(0, 1,
                    1L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());

            FutureTask<String> future = new FutureTask<String>(
                    new Callable<String>() {
                        public String call() {
                            long bT = System.currentTimeMillis();
                            try {

                                Thread.sleep(10000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return " delta:" + (System.currentTimeMillis() - bT);
                        }
                    });

            executor.submit(future);
            executor.shutdown();

            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOG.debug("Time is UP!!!!!!");
            }


            if (future.isCancelled()) {
                LOG.debug("FUTURE is Canceled");
            }

            if (future.isDone()) {
                LOG.debug("GEt some values from Future:" + future.get());
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
