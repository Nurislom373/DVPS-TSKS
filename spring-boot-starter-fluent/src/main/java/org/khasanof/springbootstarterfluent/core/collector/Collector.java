package org.khasanof.springbootstarterfluent.core.collector;

import org.khasanof.springbootstarterfluent.core.enums.HandleType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 12:29
 * <br/>
 * Package: org.khasanof.springbootstarterfluent.core.collector
 */
public interface Collector {

    Map.Entry<Method, Object> getMethodValueAnn(Object value, Class<? extends Annotation> annotation);

    Map.Entry<Method, Object> getHandleAnyMethod(HandleType handleType);

    Map<Method, Object> getAllHandleAnyMethod(HandleType handleType);

    boolean hasHandle(Class<? extends Annotation> annotation);

}
