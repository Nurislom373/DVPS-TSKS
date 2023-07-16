package org.khasanof.core.collector.methodChecker;

import org.khasanof.core.enums.HandleClasses;
import org.khasanof.main.annotation.exception.HandleException;
import org.khasanof.main.annotation.extra.HandleState;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

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

    private final ExceptionMethodChecker exceptionMethodChecker = new ExceptionMethodChecker();
    private final StateMethodChecker stateMethodChecker = new StateMethodChecker();

    @Override
    public boolean valid(Method method) {
        boolean annotationValid, parameterValid;
        int length = method.getAnnotations().length;
        if (length == 0) {
            return false;
        } else {
            annotationValid = Arrays.stream(method.getAnnotations())
                    .anyMatch(annotation -> HandleClasses.getAllAnnotations()
                            .contains(annotation.annotationType()));
        }
        int parameterCount = method.getParameterCount();
        if (isHandle(method, HandleException.class)) {
            return exceptionMethodChecker.valid(method);
        } else if (isHandle(method, HandleState.class)) {
            return stateMethodChecker.valid(method);
        } else {
            if (parameterCount == 0 || parameterCount > 2) {
                if (!annotationValid) {
                    return false;
                } else {
                    throw new RuntimeException("There is an error in the method parameters with handle annotations!");
                }
            } else {
                parameterValid = paramsTypeCheck(method.getParameterTypes());
                if (!parameterValid) {
                    throw new RuntimeException("There is an error in the method parameters with handle annotations!");
                }
            }
            return annotationValid;
        }
    }

    private boolean isHandle(Method method, Class<? extends Annotation> clazz) {
        return method.isAnnotationPresent(clazz);
    }


}
