package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.exceptions.InvalidParamsException;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;
import org.khasanof.springbootstarterfluent.main.annotation.extra.BotVariable;
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

    private final Collector collector;
    private final InvokerFunctions invokerFunctions;
    private final ApplicationEventPublisher applicationEventPublisher;

    public InvokerExecutor(Collector collector, InvokerFunctions invokerFunctions, ApplicationEventPublisher applicationEventPublisher) {
        this.collector = collector;
        this.invokerFunctions = invokerFunctions;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void invoke(InvokerModel invokerModel) {
        if (invokerModel.isHasClassEntry()) {
            try {
                absInvoker(invokerModel);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                try {
                    exceptionDirector(e.getCause(), invokerModel);
                    FluentContext.updateExecutorBoolean.set(true);
                } catch (Throwable ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void absInvoker(InvokerModel invokerModel) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (Objects.isNull(invokerModel.getAdditionalParam())) {
            checkListParams(invokerModel.getMethodParams(), invokerModel.getArgs());
        }

        Map.Entry<Method, Object> classEntry = invokerModel.getClassEntry();

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

    private void execute(InvokerModel invokerModel, Map.Entry<Method, Object> classEntry, Method method) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        applicationEventPublisher.publishEvent(new MethodV1Event(this, invokerModel, classEntry, method));
    }

    private void checkListParams(List<Class<?>> params, Object[] args) {
        boolean allMatch = Arrays.stream(args).allMatch(arg -> params.contains(arg.getClass()) || params.stream()
                .anyMatch(any -> any.isAssignableFrom(arg.getClass())));
        if (!allMatch) {
            throw new InvalidParamsException("Param type doesn't match expected param!");
        }
    }

    private void exceptionDirector(Throwable throwable, InvokerModel prevInvoker) throws Throwable {
        Map.Entry<Method, Object> exceptionHandleMethod = getExceptionHandleMethod(throwable);
        if (Objects.isNull(exceptionHandleMethod)) {
            throw throwable;
        }
        InvokerModel invokerModel = invokerFunctions.fillAndGet(exceptionHandleMethod, throwable,
                MethodUtils.getArg(prevInvoker.getArgs(), Update.class), MethodUtils.getArg(
                        prevInvoker.getArgs(), AbsSender.class));
        invoke(invokerModel);
    }

    private Map.Entry<Method, Object> getExceptionHandleMethod(Throwable throwable) throws Throwable {
        if (collector.hasHandle(HandleException.class)) {
            return collector.getMethodValueAnn(throwable, HandleException.class);
        }
        throw throwable;
    }

}
