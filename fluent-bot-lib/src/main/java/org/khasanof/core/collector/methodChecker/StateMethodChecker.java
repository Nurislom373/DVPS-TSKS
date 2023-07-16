package org.khasanof.core.collector.methodChecker;

import org.khasanof.core.exceptions.InvalidParamsException;
import org.khasanof.main.inferaces.state.State;
import org.khasanof.main.annotation.extra.HandleState;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.methodChecker
 * @since 09.07.2023 19:55
 */
public class StateMethodChecker extends AbstractMethodChecker {

    private final List<Class<?>> validTypes = List.of(State.class, Update.class, AbsSender.class);

    @Override
    public boolean valid(Method method) {
        boolean validParams, validAnnotation;

        if (method.getAnnotations().length == 0) {
            return false;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();

        validAnnotation = hasAnnotation(method, HandleState.class);

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
}
