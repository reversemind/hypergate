package com.reversemind.glia.server;

import com.reversemind.glia.GliaPayload;
import com.reversemind.glia.GliaPayloadBuilder;
import com.reversemind.glia.GliaPayloadStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Copyright (c) 2013 Eugene Kalinin
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
 */
public class GliaPayloadProcessor implements IGliaPayloadProcessor, Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(GliaPayloadProcessor.class);

    private static Map<Class, Class> mapPOJORegisteredInterfaces = new HashMap<Class, Class>();
    private static Map<Class, String> mapEJBRegisteredInterfaces = new HashMap<Class, String>();

    private static Properties jndiEnvironment;
    private static Hashtable jndiPropertiesMap;
    private static Context jndiContext = null;

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
        typeMap.put(List.class.getCanonicalName(), List.class);
        typeMap.put(ArrayList.class.getCanonicalName(), ArrayList.class);
    }

    @Override
    public Map<Class, Class> getPojoMap() {
        return mapPOJORegisteredInterfaces;
    }

    @Override
    public void setPojoMap(Map<Class, Class> map) {
        if (map != null && map.size() > 0) {
            Set<Class> set = map.keySet();
            for (Class interfaceClass : set) {
                synchronized (mapPOJORegisteredInterfaces) {
                    mapPOJORegisteredInterfaces.put(interfaceClass, map.get(interfaceClass));
                }
            }
        }
    }

    @Override
    public void setEjbMap(Map<Class, String> map) {
        if (map != null && map.size() > 0) {
            Set<Class> set = map.keySet();
            for (Class interfaceClass : set) {
                synchronized (mapEJBRegisteredInterfaces) {
                    mapEJBRegisteredInterfaces.put(interfaceClass, map.get(interfaceClass));
                }
            }
        }
    }

    @Override
    public void registerPOJO(Class interfaceClass, Class pojoClass) {
        synchronized (mapPOJORegisteredInterfaces) {
            mapPOJORegisteredInterfaces.put(interfaceClass, pojoClass);
        }
    }

    private void initJndiContext() {
        jndiPropertiesMap = this.getJndiProperties(jndiEnvironment);
        try {
            jndiContext = new InitialContext(jndiPropertiesMap);
            // buildingDAO = InitialContext.doLookup("java:global/ttk-house/ttk-house-ejb-2.0-SNAPSHOT/BuildingDAO!ru.ttk.baloo.house.data.service.building.IBuildingDAO");
        } catch (NamingException e) {
            LOG.error("NamingException ", e);
        }
    }

    /**
     * Set the JNDI environment to use for JNDI lookups.
     *
     * @param jndiEnvironment
     */
    public void setJndiEnvironment(Properties jndiEnvironment) {
        this.jndiEnvironment = jndiEnvironment;
    }

    /**
     * Return the JNDI environment to use for JNDI lookups.
     */
    public Properties getJndiEnvironment() {
        return this.jndiEnvironment;
    }

    @Override
    public GliaPayload process(Object gliaPayloadObject) {

        if (gliaPayloadObject == null) {
            LOG.info("ERROR: " + GliaPayloadStatus.ERROR_CLIENT_PAYLOAD);
            return GliaPayloadBuilder.buildErrorPayload(GliaPayloadStatus.ERROR_CLIENT_PAYLOAD);
        }

        if (!(gliaPayloadObject instanceof GliaPayload)) {
            LOG.info("ERROR: " + GliaPayloadStatus.ERROR_CLIENT_PAYLOAD);
            return GliaPayloadBuilder.buildErrorPayload(GliaPayloadStatus.ERROR_CLIENT_PAYLOAD);
        }

        GliaPayload gliaPayload = ((GliaPayload) gliaPayloadObject);


        LOG.debug("Get from client:" + gliaPayload);

        Class interfaceClass = gliaPayload.getInterfaceClass();
        Class pojoClass = this.findPOJOClass(interfaceClass);

        // POJO

        if (pojoClass != null) {
            return this.invokeMethod(gliaPayload, pojoClass, gliaPayload.getMethodName(), gliaPayload.getArguments());
        }

        // EJB
        String jndiName = this.findEjbClass(interfaceClass);

        if (!StringUtils.isEmpty(jndiName)) {
            try {
                // JBoss AS 7 JNDI name
                // buildingDAO = InitialContext.doLookup("java:global/ttk-house/ttk-house-ejb-2.0-SNAPSHOT/BuildingDAO!ru.ttk.baloo.house.data.service.building.IBuildingDAO");
                Object remoteObject = InitialContext.doLookup(jndiName);
                return this.invokeEjbMethod(gliaPayload, remoteObject, gliaPayload.getMethodName(), gliaPayload.getArguments());
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        return GliaPayloadBuilder.buildErrorPayload(GliaPayloadStatus.ERROR_UNKNOWN);
    }

    private GliaPayload invokeMethod(GliaPayload gliaPayload, Class pojoOrEjbClass, String methodName, Object[] arguments) {

        Throwable throwable = null;
        Method selectedMethod = this.findMethod(pojoOrEjbClass, methodName, arguments);

        if (selectedMethod == null) {
            LOG.info("ERROR :" + GliaPayloadStatus.ERROR_PAYLOAD_UNKNOWN_METHOD);
            return GliaPayloadBuilder.buildErrorPayload(GliaPayloadStatus.ERROR_PAYLOAD_UNKNOWN_METHOD);
        }

        try {

            Object result = selectedMethod.invoke(pojoOrEjbClass.newInstance(), arguments);
            gliaPayload.setResultResponse(result);
            gliaPayload.setStatus(GliaPayloadStatus.OK);
            gliaPayload.setServerTimestamp(System.currentTimeMillis());
            gliaPayload.setThrowable(null);

            return gliaPayload;
        } catch (Throwable th) {
            throwable = th;
            gliaPayload.setThrowable(th);
            LOG.error("ON SERVER Throwable:", th);
        }
//            // TODO make correct Exception processing
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }

        return GliaPayloadBuilder.buildErrorPayload(GliaPayloadStatus.ERROR_UNKNOWN, throwable);
    }

    private GliaPayload invokeEjbMethod(GliaPayload gliaPayload, Object instance, String methodName, Object[] arguments) {

        Throwable throwable = null;

        Method selectedMethod = this.findMethod(instance.getClass(), methodName, arguments);

        if (selectedMethod == null) {
            LOG.info("ERROR :" + GliaPayloadStatus.ERROR_PAYLOAD_UNKNOWN_METHOD);
            return GliaPayloadBuilder.buildErrorPayload(GliaPayloadStatus.ERROR_PAYLOAD_UNKNOWN_METHOD);
        }

        try {

            Object result = selectedMethod.invoke(instance, arguments);
            gliaPayload.setResultResponse(result);
            gliaPayload.setStatus(GliaPayloadStatus.OK);
            gliaPayload.setServerTimestamp(System.currentTimeMillis());
            gliaPayload.setThrowable(null);

            return gliaPayload;

        } catch (Throwable th) {
            throwable = th;
            gliaPayload.setThrowable(th);
            LOG.error("ON SERVER Throwable:", th);
        }
//            // TODO make correct Exception processing
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }

        return GliaPayloadBuilder.buildErrorPayload(GliaPayloadStatus.ERROR_UNKNOWN, throwable);
    }

    private Method findMethod(Class interfaceClass, String methodName, Object[] arguments) {
        Method selectedMethod = null;

        // Let's check that not all arguments are null
        int argumentsCount = 0;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] == null) {
                argumentsCount++;
            }
        }

        boolean argumentsAreNull = false;

        if (argumentsCount == arguments.length) {
            LOG.info("Not all arguments are null");
            argumentsAreNull = true;
        }

        Method[] pojoClassMethods = interfaceClass.getMethods();
        String compareTypeName = "";
        for (Method method : pojoClassMethods) {


            if (method.getName().equals(methodName)) {
                LOG.debug(method.getName() + " args:" + method.getParameterTypes().length);

                if (arguments.length == method.getParameterTypes().length) {

                    if (arguments.length == 0) {
                        selectedMethod = method;
                        break;
                    }

                    if (argumentsAreNull) {
                        selectedMethod = method;
                        break;
                    }

                    if (method.getParameterTypes().length > 0) {
                        int count = arguments.length;
                        Class[] cl = method.getParameterTypes();
                        for (int i = 0; i < arguments.length; i++) {

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
                            } else if (compareTypeName != null
                                    && arguments[i] != null
                                    && compareTypeName.equals(arguments[i].getClass().getCanonicalName())) {
                                count--;
                            } else if (compareTypeName != null
                                    && arguments[i] != null
                                    && arguments[i].getClass().getCanonicalName().equals(ArrayList.class.getCanonicalName())
                                    && compareTypeName.equals(List.class.getCanonicalName())) {
                                count--;
                            }

                        }

//                        Type[] tp = method.getGenericParameterTypes();
//                        for(int i=0; i< arguments.length; i++){
////                            LOG.debug("generic type = " + i + " -- " + ((ParameterizedTypeImpl) tp[i]).getRawType());
//                            LOG.debug("generic type = " + i + " -- " + tp[i]);
//                        }


                        if (count == 0) {
                            selectedMethod = method;
                            break;
                        }
                    }
                }
            }

//            if (method.getName().equals(methodName)) {
//                selectedMethod = method;
//                LOG.info("Find method:" + selectedMethod);
//                break;
//            }
        }

        return selectedMethod;
    }

    private Hashtable getJndiProperties(Properties jndi) {
        Set set = jndi.keySet();
        Hashtable localTable = new Hashtable();
        for (Object key : set) {
            localTable.put(key, jndi.get(key));
        }
        return localTable;
    }

    private Class findPOJOClass(Class interfaceClass) {
        Class pojoInterfaceClass = this.mapPOJORegisteredInterfaces.get(interfaceClass);
        return pojoInterfaceClass;
    }

    private String findEjbClass(Class interfaceClass) {
        return this.mapEJBRegisteredInterfaces.get(interfaceClass);
    }


}
