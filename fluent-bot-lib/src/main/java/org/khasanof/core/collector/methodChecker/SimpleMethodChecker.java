package org.khasanof.core.collector.methodChecker;

import org.khasanof.core.enums.HandleClasses;
import org.khasanof.main.inferaces.sender.Sender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

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
