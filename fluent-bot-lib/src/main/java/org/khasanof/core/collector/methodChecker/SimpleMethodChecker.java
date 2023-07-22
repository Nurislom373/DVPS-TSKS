package org.khasanof.core.collector.methodChecker;

import org.khasanof.core.enums.HandleClasses;
import org.khasanof.main.annotation.extra.TGPermission;

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

    private final SimpleInterMethodChecker interMethodChecker = new SimpleInterMethodChecker();

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

        return annotationValid;
    }

    @Override
    public Class<? extends Annotation> getType() {
        return TGPermission.class;
    }

    @Override
    public boolean hasSuperAnnotation() {
        return true;
    }
}
