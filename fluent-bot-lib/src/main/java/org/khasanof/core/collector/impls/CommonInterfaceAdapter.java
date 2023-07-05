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

    private final Reflections reflections = ReflectionUtils.getReflections();

    public Class getInterfaceSubclass(Class interfaze) {
        System.out.println("interfaze = " + interfaze);
        var types = reflections.get(SubTypes.of(interfaze).asClass());
        System.out.println("types.size() = " + types.size());
        if (types.isEmpty()) return null;
        Class<?> next = types.iterator().next();
        System.out.println("next = " + next);
        if (!Modifier.isAbstract(next.getModifiers()))
            return next;
        else
            return null;
    }

}
