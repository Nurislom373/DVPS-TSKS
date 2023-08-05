package org.khasanof.springbootstarterfluent.core.executors.matcher;

import org.khasanof.springbootstarterfluent.core.enums.MultiMatchScope;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher
 * @since 07.07.2023 22:37
 */
public abstract class MultiGenericMatcher<T extends Annotation, S extends Annotation, V> extends GenericMatcher<T, V> {

    protected final GenericMatcher matcher;

    protected final Map<MultiMatchScope, BiFunction<Stream<S>, Predicate<S>, Boolean>>
            multiMatchScopeFunctionMap = new HashMap<>();

    protected MultiGenericMatcher(GenericMatcher matcher) {
        this.matcher = matcher;
        setMultiMatchScopeFunctionMap();
    }

    void setMultiMatchScopeFunctionMap() {
        multiMatchScopeFunctionMap.put(MultiMatchScope.ANY_MATCH, Stream::anyMatch);
        multiMatchScopeFunctionMap.put(MultiMatchScope.ALL_MATCH, Stream::allMatch);
        multiMatchScopeFunctionMap.put(MultiMatchScope.NONE_MATCH, Stream::noneMatch);
    }
}
