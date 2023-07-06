package org.khasanof.core.utils;

import org.khasanof.core.model.MethodArgs;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.utils
 * @since 06.07.2023 21:28
 */
public abstract class MethodParamsUtils {

    public static Object[] sorter(Object[] objects, Class<?>[] parameterTypes) {
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            for (Object object : objects) {
                if (parameterType.equals(object.getClass()) ||
                        parameterType.isAssignableFrom(object.getClass())) {
                    args[i] = object;
                }
            }
        }

        return args;
    }

    public static Object[] classConvertToObjects(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .peek(fld -> fld.setAccessible(true))
                .map(fld -> {
                    try {
                        return fld.get(object);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).toList().toArray();
    }

}
