package org.khasanof.springbootstarterfluent.core.executors.determination.impls;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.enums.Proceed;
import org.khasanof.springbootstarterfluent.core.executors.HandleAnyFunctionMatcher;
import org.khasanof.springbootstarterfluent.core.executors.determination.OrderFunction;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
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
                Map<Method, Class> allHandleAnyMethod = collector.getAllHandleAnyMethod(entry.getKey());

                if (Objects.nonNull(allHandleAnyMethod)) {

                    methods.putAll(allHandleAnyMethod);
                    boolean notProceedMethods = hasNotProceedMethods(allHandleAnyMethod);

                    if (notProceedMethods) {
                        FluentContext.determinationServiceBoolean.set(true);
                    }
                }

            }
        });
    }

    private boolean hasNotProceedMethods(Map<Method, Class> methods) {
        return methods.keySet().stream().anyMatch(method -> {
            HandleAny annotation = method.getAnnotation(HandleAny.class);
            return annotation.proceed().equals(Proceed.NOT_PROCEED);
        });
    }

    @Override
    public Integer getOrder() {
        return 1;
    }
}
