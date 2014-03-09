package com.reversemind.glia.other.future;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
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
public class FutureTask_TimeOut_Example implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(FutureTask_TimeOut_Example.class);

    @Ignore
    @Test
    public void testFutureTaskTimeOutExample() {

        final long EXECUTOR_TIME_OUT = 1000;    // 1sec
        final long FUTURE_TASK_TIME_OUT = 2000; // 2 sec

        ExecutorService executor = new ThreadPoolExecutor(0, 1,
                EXECUTOR_TIME_OUT, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>());

        FutureTask<String> future = new FutureTask<String>(
                new Callable<String>() {
                    public String call() {
                        long bT = System.currentTimeMillis();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                        }
                        return " delta time:" + (System.currentTimeMillis() - bT);
                    }
                });

        executor.execute(future);

        String resultFromFutureTask = "EMPTY";
        try {
            resultFromFutureTask = future.get(FUTURE_TASK_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            LOG.error("TimeoutException == HERE", e);
            future.cancel(true);
        } catch (InterruptedException e) {
            LOG.error("InterruptedException == HERE", e);
        } catch (ExecutionException e) {
            LOG.error("ExecutionException == HERE", e);
        }

        LOG.debug("resultFromFutureTask:" + resultFromFutureTask);
        Assert.assertEquals("EMPTY", resultFromFutureTask);

        if (future.isCancelled()) {
            LOG.debug("Task was canceled");
            executor.shutdown();
        }

        if (future.isDone()) {
            executor.shutdown();
        }

        if (!executor.isShutdown()) {
            executor.shutdown();
        }

    }

}
