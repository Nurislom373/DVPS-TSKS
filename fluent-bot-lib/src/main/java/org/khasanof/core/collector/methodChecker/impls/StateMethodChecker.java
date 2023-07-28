package org.khasanof.core.collector.methodChecker.impls;

import org.khasanof.core.collector.methodChecker.AbstractMethodChecker;
import org.khasanof.core.exceptions.InvalidParamsException;
import org.khasanof.core.utils.AnnotationUtils;
import org.khasanof.main.annotation.process.ProcessState;
import org.khasanof.main.inferaces.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.methodChecker
 * @since 09.07.2023 19:55
 */
// TODO this class is one of the state classes
public class StateMethodChecker {

    private final List<Class<?>> validTypes = List.of(State.class, Update.class, AbsSender.class);

//    @Override
    public boolean valid(Method method) {
        boolean validParams, validAnnotation;

        if (method.getAnnotations().length == 0) {
            return false;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();

        validAnnotation = AnnotationUtils.hasAnnotation(method, ProcessState.class, true);

        if (parameterTypes.length != 3) {
            throw new InvalidParamsException("State handler method invalid parameters!");
        }

        validParams = allMatchParameter(parameterTypes);

        if (!validParams && validAnnotation) {
            throw new InvalidParamsException("State handler method invalid parameters!");
        }

        return validParams && validAnnotation;
    }

    private boolean allMatchParameter(Class<?>[] classes) {
        return Arrays.stream(classes).allMatch(clazz -> validTypes.stream()
                .anyMatch(valid -> valid.equals(clazz) || valid.isAssignableFrom(clazz)));
    }

//    @Override
    public Class<? extends Annotation> getType() {
        return ProcessState.class;
    }

//    @Override
    public boolean hasSuperAnnotation() {
        return true;
    }

//    @Override
    public boolean hasAny() {
        return false;
    }
}
