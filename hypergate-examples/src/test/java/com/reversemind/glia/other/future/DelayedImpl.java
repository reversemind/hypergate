package com.reversemind.glia.other.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Delayed;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
public class DelayedImpl<T> implements Delayed {

    private static final Logger LOG = LoggerFactory.getLogger(DelayedImpl.class);

    private Future<T> task;
    private final long maxExecTimeMinutes = 1;//MAX_THREAD_LIFE_MINUTES;
    private final long startInMillis = System.currentTimeMillis();

    private DelayedImpl(Future<T> task) {
        this.task = task;
    }

    public long getDelay(TimeUnit unit) {
        return unit.convert((startInMillis + maxExecTimeMinutes * 60 * 1000) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public int compareTo(Delayed o) {
        Long thisDelay = getDelay(TimeUnit.MILLISECONDS);
        Long thatDelay = o.getDelay(TimeUnit.MILLISECONDS);
        return thisDelay.compareTo(thatDelay);
    }

    public Future<T> getTask() {
        return task;
    }
}
