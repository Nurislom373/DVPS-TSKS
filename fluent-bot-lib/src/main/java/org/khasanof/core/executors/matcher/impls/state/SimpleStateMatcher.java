package org.khasanof.core.executors.matcher.impls.state;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.core.state.StateCore;
import org.khasanof.main.annotation.extra.HandleState;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls.state
 * @since 09.07.2023 19:02
 */
// TODO this class is one of the state classes
public class SimpleStateMatcher {
//    @Override
    public boolean matcher(HandleState annotation, StateCore value) {
        return annotation.value().equals(value.getCurrentState());
    }

//    @Override
    public Class<HandleState> getType() {
        return HandleState.class;
    }
}
