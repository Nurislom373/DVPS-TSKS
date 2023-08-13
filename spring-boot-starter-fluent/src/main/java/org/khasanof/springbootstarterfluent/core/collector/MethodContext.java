package org.khasanof.springbootstarterfluent.core.collector;

import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector
 * @since 8/8/2023 10:24 PM
 */
public interface MethodContext {

    Map<Method, Object> getMethodsWithAnnotation(Class<? extends Annotation> annotation);

    Map<Method, Object> getMethodsWithHandleClass(HandleClasses classes);

    boolean containsKey(HandleClasses key);

    boolean containsKey(Class<? extends Annotation> annotation);

}