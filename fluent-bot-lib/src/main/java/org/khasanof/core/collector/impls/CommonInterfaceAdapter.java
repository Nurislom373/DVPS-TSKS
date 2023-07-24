package org.khasanof.core.collector.impls;

import org.khasanof.core.utils.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.impls
 * @since 28.06.2023 15:53
 */
public class CommonInterfaceAdapter {

    private final Reflections reflections = ReflectionUtils.getReflections(false);

    public Class getInterfaceSubclass(Class interfaze) {
        var types = reflections.get(SubTypes.of(interfaze).asClass());
        if (types.isEmpty()) return null;
        Class<?> next = types.iterator().next();
        return !Modifier.isAbstract(next.getModifiers()) ? next : null;
    }

}
