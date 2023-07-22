package org.khasanof.core.collector.methodChecker;

import org.khasanof.core.utils.AnnotationUtils;
import org.khasanof.main.annotation.extra.TGPermission;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.methodChecker
 * @since 21.07.2023 22:26
 */
public class SimpleInterMethodChecker {

    public boolean valid(Method method) {
        Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();

        Predicate<Member> predicate = ReflectionUtils.withName(method.getName())
                .and(ReflectionUtils.withParameters(method.getParameterTypes()))
                .and(ReflectionUtils.withParametersCount(method.getParameterCount()));

        if (interfaces.length > 1) {
            Method absMethod = Arrays.stream(interfaces)
                    .map(interfaze -> ReflectionUtils.getMethods(interfaze, predicate))
                    .flatMap(Collection::stream).findFirst().orElse(null);

            if (Objects.nonNull(absMethod)) {
                absMethod.setAccessible(true);
                return AnnotationUtils.hasAnnotation(absMethod, TGPermission.class, true);
            }
            return false;
        }

        Set<Method> methods = ReflectionUtils.getMethods(method.getDeclaringClass().getSuperclass(), predicate);
        if (!methods.isEmpty()) {
            Method absMethod = methods.iterator().next();

            if (Objects.nonNull(absMethod)) {
                absMethod.setAccessible(true);
                return AnnotationUtils.hasAnnotation(absMethod, TGPermission.class, true);
            }
            return false;
        }

        return false;
    }

    private boolean checkMethod(Method var1, Method var2, boolean isAbstract) {
        boolean eqName = false, eqReturnType = false, eqParamCount = false, hasAbstract = false;

        if (isAbstract) {
            hasAbstract = Modifier.isAbstract(var1.getModifiers());
        }

        if (var1.getName().equals(var2.getName())) {
            eqName = true;
        }
        if (var1.getReturnType().equals(var2.getReturnType()) ||
                var1.getReturnType().isAssignableFrom(var2.getReturnType())) {
            eqReturnType = true;
        }
        if (var1.getParameterCount() == var2.getParameterCount()) {
            eqParamCount = true;
        }

        if (!isAbstract) {
            return eqName && eqReturnType && eqParamCount;
        } else {
            return eqName && eqReturnType && eqParamCount && hasAbstract;
        }
    }

}
