package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.springframework.stereotype.Component;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 25.06.2023 22:50
 */
@Component
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
