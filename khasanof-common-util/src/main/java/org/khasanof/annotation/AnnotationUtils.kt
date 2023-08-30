package org.khasanof.annotation

import org.khasanof.base.BaseUtils
import java.lang.reflect.Method
import java.util.Arrays
import java.util.Objects

fun hasAnnotation(method: Method, annotation: Class<out Annotation>, isSuper: Boolean): Boolean {
    BaseUtils.checkArgsIsNonNull(method, annotation, isSuper)
    if (isSuper) {
        return Arrays.stream(method.annotations)
            .anyMatch { any ->
                Objects.equals(any.annotationClass, annotation)
            }
    }
    return method.isAnnotationPresent(annotation)
}
