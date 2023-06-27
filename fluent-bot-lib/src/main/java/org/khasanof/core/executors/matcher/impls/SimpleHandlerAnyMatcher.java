package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.enums.HandleType;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleAny;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 25.06.2023 22:50
 */
public class SimpleHandlerAnyMatcher extends GenericMatcher<HandleAny, HandleType> {

    @Override
    public boolean matcher(HandleAny annotation, HandleType value) {
        return annotation.type().equals(value);
    }

    @Override
    public Class getType() {
        return HandleAny.class;
    }
}
