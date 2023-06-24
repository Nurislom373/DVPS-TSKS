package org.khasanof.core.executors.matcher;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors.matcher
 * @since 24.06.2023 1:14
 */
public class AdapterMatcher {

    final Set<GenericMatcher> matchers = new HashSet<>();
    final Reflections reflections = new Reflections("org.khasanof");

    public AdapterMatcher() {
        setMatchers();
    }

    public GenericMatcher findMatcher(Class<? extends Annotation> annotation) {
        return matchers.stream().filter(matcher -> matcher.getType().equals(annotation))
                .findFirst().orElseThrow(() -> new RuntimeException("Matcher not found!"));
    }

    void setMatchers() {
        for (Class<? extends GenericMatcher> aClass : reflections.getSubTypesOf(GenericMatcher.class)) {
            try {
                matchers.add(aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
