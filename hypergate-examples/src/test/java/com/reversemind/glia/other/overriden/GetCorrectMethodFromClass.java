package com.reversemind.glia.other.overriden;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
public class GetCorrectMethodFromClass {

    private static final Logger LOG = LoggerFactory.getLogger(GetCorrectMethodFromClass.class);

    private static Map<String, Class> typeMap = new HashMap<String, Class>();

    {
        typeMap.put(int.class.getCanonicalName(), Integer.class);
        typeMap.put(long.class.getCanonicalName(), Long.class);
        typeMap.put(double.class.getCanonicalName(), Double.class);
        typeMap.put(float.class.getCanonicalName(), Float.class);
        typeMap.put(boolean.class.getCanonicalName(), Boolean.class);
        typeMap.put(char.class.getCanonicalName(), Character.class);
        typeMap.put(byte.class.getCanonicalName(), Byte.class);
        typeMap.put(short.class.getCanonicalName(), Short.class);
    }

    @Test
    public void testGetCorrectMethod() {

        SimpleOverridenMethods simple = new SimpleOverridenMethods();

        String methodNameNeedToFind = "getSimple";
        Object[] arguments = new Object[2];
        arguments[0] = 1;
        arguments[1] = null;//"1";


        Method foundedMethod = null;

        Method[] methods = simple.getClass().getMethods();

        String compareTypeName = "";


        // Let's check that not all arguments are null
        int argCount = 0;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] == null) {
                argCount++;
            }
        }

        boolean argumentsAreNull = false;

        if (argCount == arguments.length) {
            LOG.debug("Not all arguments are null");
            argumentsAreNull = true;
        }

        for (Method method : methods) {

            if (method.getName().equals(methodNameNeedToFind)) {
                LOG.debug(method.getName() + " args:" + method.getParameterTypes().length);

                if (arguments.length == method.getParameterTypes().length) {

                    if (arguments.length == 0) {
                        foundedMethod = method;
                        break;
                    }

                    if (argumentsAreNull) {
                        foundedMethod = method;
                        break;
                    }

                    if (method.getParameterTypes().length > 0) {
                        int count = arguments.length;
                        Class[] cl = method.getParameterTypes();
                        for (int i = 0; i < arguments.length; i++) {

                            LOG.debug("cl[i].getCanonicalName():" + cl[i].getCanonicalName());
                            compareTypeName = cl[i].getCanonicalName();
                            if (typeMap.containsKey(cl[i].getCanonicalName())) {
                                compareTypeName = typeMap.get(cl[i].getCanonicalName()).getCanonicalName();
                            }

                            LOG.debug("arguments[i]:" + arguments[i]);
                            if (arguments[i] != null) {
                                LOG.debug("arguments[i].getClass():" + arguments[i].getClass());
                                LOG.debug("arguments[i].getClass().getCanonicalName():" + arguments[i].getClass().getCanonicalName());
                            }

                            if (arguments[i] == null) {
                                count--;
                            } else if (compareTypeName != null && arguments[i] != null && compareTypeName.equals(arguments[i].getClass().getCanonicalName())) {
                                count--;
                            }
                        }
                        if (count == 0) {
                            foundedMethod = method;
                        }
                    }
                }
            }

        }

        if (foundedMethod != null)
            LOG.debug("Founded method:" + foundedMethod + " arguments:" + foundedMethod.getParameterTypes());
    }
}
