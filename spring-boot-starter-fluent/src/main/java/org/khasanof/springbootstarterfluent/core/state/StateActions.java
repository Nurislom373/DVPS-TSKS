package org.khasanof.springbootstarterfluent.core.state;

import java.util.Arrays;
import java.util.List;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state
 * @since 8/15/2023 9:18 PM
 */
public interface StateActions<S extends Enum> extends Action, DefaultNext<S> {

    S state();

    @Override
    default boolean updateHandlersProceed() {
        return false;
    }

    default S nextState(S state) {
        return getNext(state, state());
    }

}
