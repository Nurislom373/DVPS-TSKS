package org.khasanof.springbootstarterfluent.core.collector.methodChecker;

import org.khasanof.springbootstarterfluent.core.model.properties.MainParamProperties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 23:23
 * <br/>
 * Package: org.khasanof.core.collector.methodChecker
 */
public abstract class AbstractMethodChecker implements AbstractMethodType {

    protected final Class<?>[] MAIN_PARAMS = MainParamProperties.MAIN_PARAMS_ARRAY;

    public abstract boolean valid(Method method);

    protected boolean paramsTypeCheckV2(Class<?>[] methodParams, Class<?>[] matchParams) {
        return Arrays.stream(matchParams)
                .allMatch(param -> Arrays.asList(methodParams).contains(param));
    }

    protected boolean paramsTypeCheckV3(Class<?>[] methodParams, Class<?>[] matchParams) {
        return Arrays.stream(matchParams)
                .allMatch(param -> Arrays.stream(methodParams)
                        .anyMatch(methodParam -> param.equals(methodParam) || param.isAssignableFrom(methodParam)));
    }

    protected boolean hasAnnotation(Method method, Class<? extends Annotation> annotation) {
        return method.isAnnotationPresent(annotation);
    }

    protected <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

}
