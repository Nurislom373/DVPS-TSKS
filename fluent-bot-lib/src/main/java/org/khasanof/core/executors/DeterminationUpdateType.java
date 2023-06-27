package org.khasanof.core.executors;

import org.checkerframework.checker.units.qual.C;
import org.khasanof.core.collector.Collector;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.Proceed;
import org.khasanof.main.annotation.methods.HandleAny;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap;
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
            Map.Entry<Map.Entry<Class<?>, HandleType>, Object> typeEntry = handleTypeEntry(update);
            Map.Entry<Method, Class> handleAnyMethod = collector.getHandleAnyMethod(typeEntry.getKey().getValue());
            if (Objects.nonNull(handleAnyMethod)) {
                methods.put(handleAnyMethod.getKey(), handleAnyMethod.getValue());
                Proceed proceed = handleAnyMethod.getKey().getAnnotation(HandleAny.class).proceed();
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
        Map.Entry<String, Class<? extends Annotation>> entry = switchAndGet(update);
        Map.Entry<Method, Class> classEntry = collector.getMethodValueAnn(entry.getKey(), entry.getValue());
        methods.put(classEntry.getKey(), classEntry.getValue());
    }

    private Map.Entry<String, Class<? extends Annotation>> switchAndGet(Update update) {
        if (update.hasMessage()) {
            return new AbstractMap.SimpleEntry<>(update.getMessage().getText().trim(),
                    HandleClasses.HANDLE_MESSAGE.getType());
        } else {
            return new AbstractMap.SimpleEntry<>(update.getCallbackQuery().getData(),
                    HandleClasses.HANDLE_CALLBACK.getType());
        }
    }

    private Map.Entry<Map.Entry<Class<?>, HandleType>, Object> handleTypeEntry(Update update) {
        return anyFunctionMatcher.matchFunctions(update);
    }

}
