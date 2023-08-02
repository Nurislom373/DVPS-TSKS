package org.khasanof.core.collector.methodChecker.impls;

import org.khasanof.core.collector.methodChecker.AbstractMethodChecker;
import org.khasanof.core.exceptions.InvalidExpressionException;
import org.khasanof.core.exceptions.InvalidParamsException;
import org.khasanof.core.executors.expression.ExpressionVariables;
import org.khasanof.core.executors.expression.VariableExpressionMatcher;
import org.khasanof.core.utils.AnnotationUtils;
import org.khasanof.core.utils.ReflectionUtils;
import org.khasanof.main.annotation.methods.HandleMessage;
import org.khasanof.main.annotation.methods.HandleMessages;
import org.khasanof.main.annotation.process.ProcessFile;
import org.khasanof.main.annotation.process.ProcessUpdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 23:24
 * <br/>
 * Package: org.khasanof.core.collector.methodChecker
 */
public class SimpleMethodChecker extends AbstractMethodChecker {

    private final Set<Class<?>> classes = ReflectionUtils.getSubTypesSuperAnnotation(ProcessUpdate.class);
    private final ExpressionVariables expressionVariables = new VariableExpressionMatcher();

    @Override
    public boolean valid(Method method) {
        boolean annotationValid;
        int length = method.getAnnotations().length;

        if (length == 0) {
            return false;
        } else {
            AtomicInteger matchCount = new AtomicInteger();
            Arrays.stream(method.getAnnotations())
                    .forEach(annotation -> {
                        if (classes.contains(annotation.annotationType())) {
                            matchCount.getAndIncrement();
                        }
                    });
            annotationValid = matchCount.get() == 1;
        }

        int parameterCount = method.getParameterCount();

        boolean profileFile = AnnotationUtils.hasAnnotation(method, ProcessFile.class, true);

        if (profileFile) {
            return hasProcessFile(method, annotationValid);
        }

        return match(method, annotationValid, parameterCount);
    }

    private boolean match(Method method, boolean annotationValid, int parameterCount) {
        boolean parameterValid;
        if (parameterCount != 2) {
            if (!annotationValid) {
                return false;
            }
        } else {
            parameterValid = paramsTypeCheckV2(method.getParameterTypes(), MAIN_PARAMS);
            if (!parameterValid) {
                throw new RuntimeException("There is an error in the method parameters with handle annotations!");
            }
        }
        return annotationValid;
    }

    private boolean variableExpMatch(Method method, boolean annotationValid) {
        HandleMessage annotation = method.getAnnotation(HandleMessage.class);
        boolean expression = expressionVariables.isExpression(annotation.value());
        if (!expression) {
            throw new InvalidExpressionException("Invalid Expression!");
        }

        List<String> list = Arrays.asList(expressionVariables.getExpression(annotation.value()));

        int parameterCount = method.getParameterCount();

        return annotationValid;
    }

    @Override
    public Class<? extends Annotation> getType() {
        return ProcessUpdate.class;
    }

    @Override
    public boolean hasSuperAnnotation() {
        return true;
    }

    @Override
    public boolean hasAny() {
        return false;
    }

    private boolean hasProcessFile(Method method, boolean annotationValid) {
        boolean parameterValid;
        int parameterCount = method.getParameterCount();

        if (parameterCount < 2 || parameterCount > 3) {
            throw new InvalidParamsException("There is an error in the method parameters with handle annotations!");
        } else {
            if (parameterCount == 3) {
                Class<?>[] mainParams = new Class[MAIN_PARAMS.length + 1];
                System.arraycopy(MAIN_PARAMS, 0, mainParams, 0, MAIN_PARAMS.length);
                mainParams[mainParams.length - 1] = InputStream.class;

                parameterValid = paramsTypeCheckV3(method.getParameterTypes(), mainParams);
            } else {
                parameterValid = paramsTypeCheckV2(method.getParameterTypes(), MAIN_PARAMS);
            }

            if (!parameterValid) {
                throw new RuntimeException("There is an error in the method parameters with handle annotations!");
            }
        }

        return annotationValid;
    }

}
