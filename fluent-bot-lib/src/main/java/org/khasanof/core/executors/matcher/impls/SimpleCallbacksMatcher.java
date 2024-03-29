package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleCallbacks;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher
 * @since 24.06.2023 0:57
 */
public class SimpleCallbacksMatcher extends GenericMatcher<HandleCallbacks, String> {

    final SimpleCallbackMatcher matcher = new SimpleCallbackMatcher();

    @Override
    public boolean matcher(HandleCallbacks annotation, String value) {
        return Arrays.stream(annotation.values())
                .anyMatch(handleCallback -> matcher.matcher(handleCallback, value));
    }

    @Override
    public Class<HandleCallbacks> getType() {
        return HandleCallbacks.class;
    }

}
