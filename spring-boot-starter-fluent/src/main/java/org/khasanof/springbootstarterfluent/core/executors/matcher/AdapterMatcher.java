package org.khasanof.springbootstarterfluent.core.executors.matcher;

import org.khasanof.springbootstarterfluent.core.utils.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors.matcher
 * @since 24.06.2023 1:14
 */
@Component
public class AdapterMatcher {

    final Set<GenericMatcher> matchers = new HashSet<>();
    final Reflections reflections = ReflectionUtils.getReflections();

    public AdapterMatcher() {
        setMatchers();
    }

    public void setUp(Set<Class<? extends Annotation>> classes, Map<Class<? extends Annotation>, Supplier<GenericMatcher>> supplierMap) {
        classes.stream().map(clazz -> new AbstractMap.SimpleEntry<>(clazz, (Supplier<GenericMatcher>) () -> findMatcher(clazz)))
                .forEach(classSupplierSimpleEntry -> supplierMap.put(classSupplierSimpleEntry.getKey(),
                        classSupplierSimpleEntry.getValue()));
    }

    public GenericMatcher findMatcher(Class<? extends Annotation> annotation) {
        return matchers.stream().filter(matcher -> matcher.getType().equals(annotation))
                .findFirst().orElseThrow(() -> new RuntimeException("Matcher not found!"));
    }

    void setMatchers() {
        for (Class<? extends GenericMatcher> aClass : reflections.getSubTypesOf(GenericMatcher.class)) {
            try {
                if (!Modifier.isAbstract(aClass.getModifiers())) {
                    matchers.add(aClass.newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
