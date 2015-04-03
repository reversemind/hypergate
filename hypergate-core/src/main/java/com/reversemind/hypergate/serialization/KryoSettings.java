/**
 * Copyright (c) 2013-2015 Eugene Kalinin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.reversemind.hypergate.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.reversemind.hypergate.Payload;
import com.reversemind.hypergate.PayloadStatus;

import java.util.*;

/**
 * @author Eugene Kalinin
 */
public class KryoSettings {

    public static final int DEFAULT_MAX_SIZE = 1048576;
    public static final int EXPERIMENT_MAX_SIZE = DEFAULT_MAX_SIZE * 10;

    public Kryo getKryo() {
        Kryo kryo = new Kryo();
        kryo.register(Integer.class);
        kryo.register(Long.class);
        kryo.register(String.class);
        kryo.register(List.class);
        kryo.register(Set.class);
        kryo.register(Map.class);
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);

        kryo.register(Throwable.class);
        kryo.register(PayloadStatus.class);
        kryo.register(Payload.class);
        return kryo;
    }
}
