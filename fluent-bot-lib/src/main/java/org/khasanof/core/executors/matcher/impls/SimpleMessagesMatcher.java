package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.HandleMessages;

import java.util.Arrays;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors.matcher
 * @since 24.06.2023 1:00
 */
public class SimpleMessagesMatcher extends GenericMatcher<HandleMessages> {

    final SimpleMessageMatcher matcher = new SimpleMessageMatcher();

    @Override
    public boolean matcher(HandleMessages annotation, String value) {
        return Arrays.stream(annotation.values())
                .anyMatch(handleMessage -> matcher.matcher(handleMessage, value));
    }

    @Override
    public Class<HandleMessages> getType() {
        return HandleMessages.class;
    }
}
