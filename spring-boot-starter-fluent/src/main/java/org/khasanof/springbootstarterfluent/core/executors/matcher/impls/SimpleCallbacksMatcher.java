package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleCallbacks;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher
 * @since 24.06.2023 0:57
 */
@Component
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
