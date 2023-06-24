package org.khasanof.core.executors;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.collector.impls.SimpleCollector;
import org.khasanof.core.enums.ExecutorType;
import org.khasanof.core.enums.HandleClasses;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors
 * @since 24.06.2023 20:00
 */
public class DeterminationUpdateType {

    private final Collector collector;

    public DeterminationUpdateType(Collector collector) {
        this.collector = collector;
    }

    Map.Entry<Map.Entry<Method, Class>, ExecutorType> determination(Update update) {
        Map.Entry<String, Class<? extends Annotation>> entry = switchAndGet(update);
        return Map.entry(collector.getMethodValueAnn(entry.getKey(), entry.getValue()),
                ExecutorType.classToType(entry.getValue()));
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

}
