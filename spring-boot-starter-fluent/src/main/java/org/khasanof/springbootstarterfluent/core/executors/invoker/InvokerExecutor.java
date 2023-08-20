package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.exceptions.InvalidParamsException;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.core.model.InvokerModelV2;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.invoker
 * @since 16.07.2023 15:01
 */
@Component
public class InvokerExecutor implements Invoker {

    private final Collector<Class<? extends Annotation>> collector;
    private final InvokerFunctions invokerFunctions;
    private final InvokerResultService resultService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public InvokerExecutor(Collector<Class<? extends Annotation>> collector, InvokerFunctions invokerFunctions, InvokerResultService resultService, ApplicationEventPublisher applicationEventPublisher) {
        this.collector = collector;
        this.invokerFunctions = invokerFunctions;
        this.resultService = resultService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void invoke(InvokerModel invokerModel) {
//        if (invokerModel.isHasClassEntry()) {
//            try {
//                absInvoker(invokerModel);
//            } catch (InstantiationException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e) {
//                try {
//                    exceptionDirector(e.getCause(), invokerModel);
//                    FluentContext.updateExecutorBoolean.set(true);
//                } catch (Throwable ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }
    }

    @Override
    public void invokeV2(InvokerModelV2 invokerModelV2) {
        try {
            absInvoker(invokerModelV2);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            try {
                exceptionDirector(e.getCause(), invokerModelV2);
                FluentContext.updateExecutorBoolean.set(true);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void absInvoker(InvokerModelV2 invokerModel) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (Objects.isNull(invokerModel.getAdditionalParam())) {
            checkListParams(invokerModel.getMethodParams(), invokerModel.getArgs());
        }

        Map.Entry<Method, Object> classEntry = resultService.getResultEntry(invokerModel.getInvokerReference());

        Method method = classEntry.getKey();
        method.setAccessible(true);

        if (invokerModel.isCanBeNoParam()) {
            if (method.getParameterCount() == 0) {
                method.invoke(classEntry.getValue());
            } else {
                execute(invokerModel, classEntry, method);
            }
        } else {
            execute(invokerModel, classEntry, method);
        }
    }

    private void execute(InvokerModelV2 invokerModel, Map.Entry<Method, Object> classEntry, Method method) {
        applicationEventPublisher.publishEvent(new MethodV1Event(this, invokerModel, classEntry, method));
    }

    private void checkListParams(List<Class<?>> params, Object[] args) {
        boolean allMatch = Arrays.stream(args).allMatch(arg -> params.contains(arg.getClass()) || params.stream()
                .anyMatch(any -> any.isAssignableFrom(arg.getClass())));
        if (!allMatch) {
            throw new InvalidParamsException("Param type doesn't match expected param!");
        }
    }

    private void exceptionDirector(Throwable throwable, InvokerModelV2 prevInvoker) throws Throwable {
        InvokerResult exceptionHandleMethod = getExceptionHandleMethod(throwable);
        if (Objects.isNull(exceptionHandleMethod)) {
            throw throwable;
        }
        InvokerModelV2 invokerModel = invokerFunctions.fillAndGetV2(exceptionHandleMethod, throwable,
                MethodUtils.getArg(prevInvoker.getArgs(), Update.class), MethodUtils.getArg(
                        prevInvoker.getArgs(), AbsSender.class));
        invokeV2(invokerModel);
    }

    private InvokerResult getExceptionHandleMethod(Throwable throwable) throws Throwable {
        if (collector.hasHandle(HandleException.class)) {
            return collector.getMethodValueAnn(throwable, HandleException.class);
        }
        throw throwable;
    }

}
