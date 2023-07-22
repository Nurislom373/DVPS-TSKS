package org.khasanof.core.executors.determination.impls;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.custom.FluentContext;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.Proceed;
import org.khasanof.core.executors.HandleAnyFunctionMatcher;
import org.khasanof.core.executors.determination.DeterminationService;
import org.khasanof.core.executors.determination.OrderFunction;
import org.khasanof.core.utils.MethodUtils;
import org.khasanof.main.annotation.methods.HandleAny;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination.impls
 * @since 16.07.2023 19:13
 */
public class HandleAnyFunction implements OrderFunction {

    @Override
    public BiConsumer<Update, Map<Method, Class>> accept(List<Object> list) {
        return ((update, methods) -> {
            Collector collector = MethodUtils.getArg(list, Collector.class);
            if (collector.hasHandle(HandleAny.class)) {
                HandleAnyFunctionMatcher anyFunctionMatcher = MethodUtils.getArg(list, HandleAnyFunctionMatcher.class);
                Map.Entry<HandleType, Object> entry = anyFunctionMatcher.matchFunctions(update);
                Map.Entry<Method, Class> handleAnyMethod = collector.getHandleAnyMethod(entry.getKey());
                if (Objects.nonNull(handleAnyMethod)) {
                    methods.put(handleAnyMethod.getKey(), handleAnyMethod.getValue());
                    Proceed proceed = handleAnyMethod.getKey().getAnnotation(HandleAny.class)
                            .proceed();
                    if (proceed.equals(Proceed.NOT_PROCEED)) {
                        FluentContext.determinationServiceBoolean.set(true);
                    }
                }

            }
        });
    }

    @Override
    public DeterminationService.Order getOrder() {
        return DeterminationService.Order.HIGH;
    }
}
