package com.test.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
public class ExecutorsTest {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutorsTest.class);

    public static void main(String... args) throws ExecutionException, InterruptedException {

        StringCallable sc1 = new StringCallable("123");
        StringCallable sc2 = new StringCallable("456");
        StringCallable sc3 = new StringCallable("678");
        StringCallable sc4 = new StringCallable("90");


        List<StringCallable> callableList = new ArrayList<StringCallable>();
        for (int i = 0; i < 5; i++) {
            callableList.add(new StringCallable(i + "" + i + "" + i));
        }


        List<FutureTask<String>> futureTaskList = new ArrayList<FutureTask<String>>();
        for (StringCallable stringCallable : callableList) {
            futureTaskList.add(new FutureTask<String>(stringCallable));
        }


        long beginTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(futureTaskList.size());
        for (FutureTask<String> futureTask : futureTaskList) {
            executor.execute(futureTask);
        }


        boolean ready = false;

        while (!ready) {

            int count = 0;
            for (FutureTask<String> futureTask : futureTaskList) {
                if (futureTask.isDone()) {
                    LOG.debug("Value:" + futureTask.get());
                    count++;
                }
            }

            if (count == futureTaskList.size()) {
                ready = true;
            }

        }
        LOG.debug("All DONE for:" + (System.currentTimeMillis() - beginTime));

        executor.shutdown();
    }

    /**
     *
     */


}
