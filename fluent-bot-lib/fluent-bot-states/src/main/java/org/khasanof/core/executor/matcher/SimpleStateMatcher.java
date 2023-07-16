package org.khasanof.core.executor.matcher;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.core.state.StateCore;
import org.khasanof.main.annotation.extra.HandleState;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls.state
 * @since 09.07.2023 19:02
 */
public class SimpleStateMatcher extends GenericMatcher<HandleState, StateCore> {

    @Override
    public boolean matcher(HandleState annotation, StateCore value) {
        System.out.println("value = " + value);
        return annotation.value().equals(value.getCurrentState());
    }

    @Override
    public Class<HandleState> getType() {
        return HandleState.class;
    }
}
