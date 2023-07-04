package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.exception.HandleException;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 04.07.2023 22:12
 */
public class SimpleExceptionMatcher extends GenericMatcher<HandleException, Object> {

    @Override
    public boolean matcher(HandleException annotation, Object value) {
        return Arrays.stream(annotation.value())
                .anyMatch(any -> any.isAssignableFrom(value.getClass()));
    }

    @Override
    public Class<HandleException> getType() {
        return HandleException.class;
    }
}
