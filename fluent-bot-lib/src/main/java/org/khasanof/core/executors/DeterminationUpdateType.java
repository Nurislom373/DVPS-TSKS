package org.khasanof.core.executors;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.Proceed;
import org.khasanof.main.annotation.methods.HandleAny;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors
 * @since 24.06.2023 20:00
 */
public class DeterminationUpdateType {

    private final Collector collector;
    private final HandleAnyFunctionMatcher anyFunctionMatcher = new HandleAnyFunctionMatcher();

    public DeterminationUpdateType(Collector collector) {
        this.collector = collector;
    }

    public Map<Method, Class> determination(Update update) {
        Map<Method, Class> methods = new LinkedHashMap<>();
        if (collector.hasHandleAnyMethod()) {
            Map.Entry<HandleType, Object> typeEntry = handleTypeEntry(update);
            Map.Entry<Method, Class> handleAnyMethod = collector.getHandleAnyMethod(typeEntry.getKey());
            if (Objects.nonNull(handleAnyMethod)) {
                methods.put(handleAnyMethod.getKey(), handleAnyMethod.getValue());
                Proceed proceed = handleAnyMethod.getKey().getAnnotation(HandleAny.class)
                        .proceed();
                if (proceed.equals(Proceed.PROCEED)) {
                    pushMatchMethod(update, methods);
                }
            } else {
                pushMatchMethod(update, methods);
            }
        } else {
            pushMatchMethod(update, methods);
        }
        return methods;
    }

    private void pushMatchMethod(Update update, Map<Method, Class> methods) {
        Map.Entry<Object, Class<? extends Annotation>> entry = switchAndGet(update);
        if (Objects.nonNull(entry)) {
            Map.Entry<Method, Class> classEntry = collector.getMethodValueAnn(entry.getKey(), entry.getValue());
            if (Objects.nonNull(classEntry))
                methods.put(classEntry.getKey(), classEntry.getValue());
            else
                System.out.println("Method not found!");
        }
    }

    private Map.Entry<Object, Class<? extends Annotation>> switchAndGet(Update update) {
        Map.Entry<HandleType, Object> objectEntry = anyFunctionMatcher.matchFunctions(update);
        if (HandleType.hasHandleAnnotation(objectEntry.getKey())) {
            return Map.entry(objectEntry.getValue(), objectEntry.getKey().getHandleClasses().getType());
        }
        return null;
    }

    private Map.Entry<HandleType, Object> handleTypeEntry(Update update) {
        return anyFunctionMatcher.matchFunctions(update);
    }

}
