package org.khasanof.core.utils;

import org.khasanof.core.collector.loader.HandleScannerLoader;
import org.reflections.Reflections;

import java.util.List;

/**
 * @author Nurislom
 * @see org.khasanof.core.utils
 * @since 05.07.2023 22:33
 */
public abstract class ReflectionUtils {

    private static final HandleScannerLoader loader = new HandleScannerLoader();
    private static final Reflections reflections = new Reflections(loader.getBasePackage());
    private static final Reflections systemReflections = new Reflections("org.khasanof");

    public static Reflections getReflections(boolean isSystem) {
        if (isSystem) {
            return systemReflections;
        }
        return reflections;
    }

    public static <T> List<T> getSubTypesObject(Class<T> clazz) {
        return (List<T>) systemReflections.getSubTypesOf(clazz).stream().map(clz -> {
            try {
                return clz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
