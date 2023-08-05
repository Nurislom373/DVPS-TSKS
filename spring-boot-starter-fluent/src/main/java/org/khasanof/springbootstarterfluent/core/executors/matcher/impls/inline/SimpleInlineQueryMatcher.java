package org.khasanof.springbootstarterfluent.core.executors.matcher.impls.inline;

import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.inline.HandleInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls.inline
 * @since 29.07.2023 21:26
 */
public class SimpleInlineQueryMatcher extends GenericMatcher<HandleInlineQuery, InlineQuery> {

    @Override
    public boolean matcher(HandleInlineQuery annotation, InlineQuery value) {
        return Arrays.stream(annotation.value())
                .anyMatch(annotationVal -> matchFunctions.get(Map.entry(annotation.match(), String.class))
                        .apply(annotationVal, value));
    }

    @Override
    public Class<HandleInlineQuery> getType() {
        return HandleInlineQuery.class;
    }
}
