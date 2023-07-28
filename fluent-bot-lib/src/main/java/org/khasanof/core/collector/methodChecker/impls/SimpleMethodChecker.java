package org.khasanof.core.collector.methodChecker.impls;

import org.khasanof.core.collector.methodChecker.AbstractMethodChecker;
import org.khasanof.core.exceptions.InvalidParamsException;
import org.khasanof.core.utils.ReflectionUtils;
import org.khasanof.main.annotation.methods.HandleAny;
import org.khasanof.main.annotation.process.ProcessUpdate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 23:24
 * <br/>
 * Package: org.khasanof.core.collector.methodChecker
 */
public class SimpleMethodChecker extends AbstractMethodChecker {

    private final Set<Class<?>> classes = ReflectionUtils.getSubTypesSuperAnnotation(ProcessUpdate.class);

    @Override
    public boolean valid(Method method) {
        boolean annotationValid, parameterValid;
        int length = method.getAnnotations().length;

        if (length == 0) {
            return false;
        } else {
            AtomicInteger matchCount = new AtomicInteger();
            Arrays.stream(method.getAnnotations())
                    .forEach(annotation -> {
                        if (classes.contains(annotation.annotationType())) {
                            matchCount.getAndIncrement();
                        }
                    });
            annotationValid = matchCount.get() == 1;
        }

        int parameterCount = method.getParameterCount();

        if (isHandleAny(method.getAnnotations())) {
            if (parameterCount > 2) {
                throw new InvalidParamsException("Invalid parameter handleAny!");
            }
            if (parameterCount > 0) {
                parameterValid = paramsTypeCheck(method.getParameterTypes());
                if (!parameterValid) {
                    throw new RuntimeException("There is an error in the method parameters with handle annotations!");
                }
            }
        } else {
            if (parameterCount == 0 || parameterCount > 2) {
                if (!annotationValid) {
                    return false;
                }
            } else {
                parameterValid = paramsTypeCheck(method.getParameterTypes());
                if (!parameterValid) {
                    throw new RuntimeException("There is an error in the method parameters with handle annotations!");
                }
            }
        }

        return annotationValid;
    }

    private boolean isHandleAny(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .anyMatch(annotation -> annotation.annotationType().equals(HandleAny.class));
    }

    @Override
    public Class<? extends Annotation> getType() {
        return ProcessUpdate.class;
    }

    @Override
    public boolean hasSuperAnnotation() {
        return true;
    }

    @Override
    public boolean hasAny() {
        return false;
    }
}
