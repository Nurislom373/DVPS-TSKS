package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessages;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher
 * @since 24.06.2023 1:00
 */
public class SimpleMessagesMatcher extends GenericMatcher<HandleMessages, String> {

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
