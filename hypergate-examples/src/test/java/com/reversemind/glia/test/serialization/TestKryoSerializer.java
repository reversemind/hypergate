package com.reversemind.glia.test.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.reversemind.glia.GliaPayload;
import com.reversemind.glia.GliaPayloadStatus;
import com.reversemind.glia.serialization.KryoSerializer;
import com.reversemind.glia.serialization.KryoSettings;
import com.reversemind.glia.test.pojo.shared.PAddressNode;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

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
public class TestKryoSerializer {
    @Test
    public void testCompare() throws IOException {
        List<PAddressNode> list = new ArrayList<PAddressNode>();
        for (int i = 0; i < 100000; i++) {
//        for (int i = 0; i < 1; i++) {
            list.add(new PAddressNode("" + i, " city - " + i + "_" + i));
        }





        Class[] classes = {PAddressNode.class, Integer.class};

        final Kryo kryo = new Kryo();
        kryo.register(Integer.class, new DefaultSerializers.IntSerializer());
        kryo.register(Long.class, new DefaultSerializers.LongSerializer());
        kryo.register(String.class, new DefaultSerializers.StringSerializer());
        kryo.register(List.class);
//        kryo.register(Set.class);
//        kryo.register(Map.class);
//        kryo.register(HashMap.class);

        kryo.register(ArrayList.class,kryo.getSerializer(List.class));
        kryo.register(PAddressNode.class);
//        kryo.register(Throwable.class);
//        kryo.register(GliaPayloadStatus.class);
//        kryo.register(GliaPayload.class);



        KryoSerializer kryoSerializer = new KryoSerializer(kryo);
        long bT = System.currentTimeMillis();
        byte[] bytes = kryoSerializer.serialize(list);
        System.out.println("gogo -- " + bytes.length + " time:" + (System.currentTimeMillis()-bT));


        FileOutputStream fos = new FileOutputStream("/serializer.java");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        bT = System.currentTimeMillis();
        oos.writeObject(list);
        System.out.println( "TIME - " + " time:" + (System.currentTimeMillis()-bT));
        oos.flush();
        oos.close();



        fos = new FileOutputStream("/serializer.kryo");
        oos = new ObjectOutputStream(fos);


        oos.writeObject(bytes);


        oos.flush();
        oos.close();


    }
}
