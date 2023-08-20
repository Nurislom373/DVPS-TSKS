package org.khasanof.springbootstarterfluent.core.event;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.event.exceptionDirector.ExceptionDirectorEvent;
import org.khasanof.springbootstarterfluent.core.executors.invoker.DefaultInvokerFunctions;
import org.khasanof.springbootstarterfluent.core.executors.invoker.Invoker;
import org.khasanof.springbootstarterfluent.core.executors.invoker.InvokerFunctions;
import org.khasanof.springbootstarterfluent.core.model.InvokerModelV2;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.event
 * @since 8/20/2023 5:55 PM
 */
@Slf4j
@Component
public class ExceptionDirectorEventListener implements ApplicationListener<ExceptionDirectorEvent> {

    private final Invoker invoker;
    private final InvokerFunctions invokerFunctions;
    private final Collector<Class<? extends Annotation>> collector;

    public ExceptionDirectorEventListener(Invoker invoker, InvokerFunctions invokerFunctions, Collector<Class<? extends Annotation>> collector) {
        this.invoker = invoker;
        this.invokerFunctions = invokerFunctions;
        this.collector = collector;
    }

    @Override
    public void onApplicationEvent(ExceptionDirectorEvent event) {
        if (collector.hasHandle(HandleException.class)) {
            InvokerResult result = collector.getMethodValueAnn(event.getThrowable(), HandleException.class);
            InvokerModelV2 modelV2 = invokerFunctions.fillAndGetV2(result, event.getUpdate(), event.getAbsSender(),
                    event.getThrowable());
            invoker.invokeV2(modelV2);
        }
    }

}
