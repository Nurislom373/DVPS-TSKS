package org.khasanof.springbootstarterfluent.core.executors.determination.impls;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.executors.HandleAnyFunctionMatcher;
import org.khasanof.springbootstarterfluent.core.executors.determination.DeterminationService;
import org.khasanof.springbootstarterfluent.core.executors.determination.OrderFunction;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination.impls
 * @since 16.07.2023 18:58
 */
public class HandleUpdateFunction implements OrderFunction {

    @Override
    public BiConsumer<Update, Map<Method, Class>> accept(List<Object> list) {
        return ((update, methods) -> {
            HandleAnyFunctionMatcher anyFunctionMatcher = MethodUtils.getArg(list,
                    HandleAnyFunctionMatcher.class);
            Map.Entry<HandleType, Object> objectEntry = anyFunctionMatcher.matchFunctions(update);
            Map.Entry<Object, Class<? extends Annotation>> entry = null;
            if (HandleType.hasHandleAnnotation(objectEntry.getKey())) {
                entry = Map.entry(objectEntry.getValue(), objectEntry.getKey().getHandleClasses().getType());
            }
            if (Objects.nonNull(entry)) {
                Collector collector = MethodUtils.getArg(list, Collector.class);
                Map.Entry<Method, Class> classEntry = collector.getMethodValueAnn(entry.getKey(), entry.getValue());
                if (Objects.nonNull(classEntry))
                    methods.put(classEntry.getKey(), classEntry.getValue());
                else
                    System.out.println("Method not found!");
            }
        });
    }

    @Override
    public Integer getOrder() {
        return 10;
    }
}
