package org.khasanof.springbootstarterfluent.core.executors.determination.impls;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.collector.SimpleCollector;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.enums.Proceed;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.executors.HandleFunctionsMatcher;
import org.khasanof.springbootstarterfluent.core.executors.determination.OrderFunction;
import org.khasanof.springbootstarterfluent.core.model.InvokerMethod;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination.impls
 * @since 16.07.2023 19:13
 */
@Slf4j
@Component(HandleAnyFunction.NAME)
public class HandleAnyFunction implements OrderFunction {

    public static final String NAME = "handleAnyFunction";

    @Override
    @SuppressWarnings("unchecked")
    public BiConsumer<Update, Set<InvokerResult>> accept(ApplicationContext applicationContext) {
        return ((update, invokerResults) -> {
            Collector<Class<? extends Annotation>> collector = applicationContext.getBean(SimpleCollector.NAME, Collector.class);

            if (collector.hasHandle(HandleAny.class)) {
                HandleFunctionsMatcher matcher = applicationContext.getBean(HandleFunctionsMatcher.class);
                Optional<Map.Entry<HandleType, Object>> optional = matcher.matchFunctions(update);

                optional.ifPresentOrElse((handleTypeObjectEntry -> {
                    Set<InvokerResult> allHandleAnyMethods = collector.getAllHandleAnyMethod(handleTypeObjectEntry.getKey());

                    if (Objects.nonNull(allHandleAnyMethods)) {

                        invokerResults.addAll(allHandleAnyMethods);
                        boolean hasValueNotProceedInMethods = hasValueNotProceedInMethods(allHandleAnyMethods);

                        if (hasValueNotProceedInMethods) {
                            FluentContext.determinationServiceBoolean.set(true);
                        }
                    }
                }), () -> log.warn("HandleType not found!"));
            }
        });
    }

    private boolean hasValueNotProceedInMethods(Set<InvokerResult> methods) {
        return methods.stream().map(result -> ((InvokerMethod) result).getMethod())
                .anyMatch(method -> {
                    HandleAny annotation = method.getAnnotation(HandleAny.class);
                    return annotation.proceed().equals(Proceed.NOT_PROCEED);
                });
    }

    @Override
    public Integer getOrder() {
        return 1;
    }

    @Override
    public ProcessType processType() {
        return ProcessType.BOTH;
    }
}
