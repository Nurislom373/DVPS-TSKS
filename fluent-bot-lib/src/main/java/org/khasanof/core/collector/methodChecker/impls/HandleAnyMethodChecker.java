package org.khasanof.core.collector.methodChecker.impls;

import org.khasanof.core.collector.methodChecker.AbstractMethodChecker;
import org.khasanof.core.exceptions.InvalidParamsException;
import org.khasanof.main.annotation.methods.HandleAny;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.methodChecker.impls
 * @since 29.07.2023 15:55
 */
public class HandleAnyMethodChecker extends AbstractMethodChecker {

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
                        if (HandleAny.class.equals(annotation.annotationType())) {
                            matchCount.getAndIncrement();
                        }
                    });
            annotationValid = matchCount.get() == 1;
        }

        int parameterCount = method.getParameterCount();

        if (!(parameterCount == 2 || parameterCount == 0)) {
            throw new InvalidParamsException("Invalid parameter handleAny!");
        }

        if (parameterCount != 0) {
           parameterValid = paramsTypeCheckV2(method.getParameterTypes(), MAIN_PARAMS);
            if (!parameterValid) {
                throw new RuntimeException("There is an error in the method parameters with handle annotations!");
            }
        }

        return annotationValid;
    }

    @Override
    public Class<? extends Annotation> getType() {
        return HandleAny.class;
    }

    @Override
    public boolean hasSuperAnnotation() {
        return false;
    }

    @Override
    public boolean hasAny() {
        return true;
    }
}
