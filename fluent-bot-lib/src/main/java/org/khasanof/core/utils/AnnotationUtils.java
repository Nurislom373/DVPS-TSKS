package org.khasanof.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.utils
 * @since 12.07.2023 23:41
 */
public abstract class AnnotationUtils {

    public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotation, boolean isSuper) {
        if (Objects.nonNull(method)) {
            if (isSuper) {
                return Arrays.stream(method.getAnnotations())
                        .anyMatch(any -> any.annotationType()
                                .isAnnotationPresent(annotation));
            }
            return method.isAnnotationPresent(annotation);
        }
        return false;
    }

}
