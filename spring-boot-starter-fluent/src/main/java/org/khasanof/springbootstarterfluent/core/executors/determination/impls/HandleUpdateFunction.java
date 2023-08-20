package org.khasanof.springbootstarterfluent.core.executors.determination.impls;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.collector.SimpleCollector;
import org.khasanof.springbootstarterfluent.core.collector.SimpleMethodContextClass;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.executors.HandleAnyFunctionMatcher;
import org.khasanof.springbootstarterfluent.core.executors.determination.OrderFunction;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination.impls
 * @since 16.07.2023 18:58
 */
@Slf4j
@Component(HandleUpdateFunction.NAME)
public class HandleUpdateFunction implements OrderFunction {

    public static final String NAME = "handleUpdateFunction";

    @Override
    @SuppressWarnings("unchecked")
    public BiConsumer<Update, Set<InvokerResult>> accept(ApplicationContext applicationContext) {
        return ((update, invokerResults) -> {
            HandleAnyFunctionMatcher anyFunctionMatcher = applicationContext.getBean(HandleAnyFunctionMatcher.class);
            Map.Entry<HandleType, Object> objectEntry = anyFunctionMatcher.matchFunctions(update);
            Map.Entry<Object, Class<? extends Annotation>> entry = null;
            if (HandleType.hasHandleAnnotation(objectEntry.getKey())) {
                entry = Map.entry(objectEntry.getValue(), objectEntry.getKey().getHandleClasses().getType());
            }
            if (Objects.nonNull(entry)) {
                Collector<Class<? extends Annotation>> collector = applicationContext.getBean(SimpleCollector.NAME, Collector.class);
                InvokerResult classEntry = collector.getMethodValueAnn(entry.getKey(), entry.getValue());
                if (Objects.nonNull(classEntry)) {

                    invokerResults.add(classEntry);
                } else {
                    log.warn("Method not found!");
                }
            }
        });
    }

    @Override
    public Integer getOrder() {
        return 10;
    }
}
