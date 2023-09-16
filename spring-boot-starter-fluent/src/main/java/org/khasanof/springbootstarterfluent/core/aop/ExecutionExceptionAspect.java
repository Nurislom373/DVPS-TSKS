package org.khasanof.springbootstarterfluent.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.event.exceptionDirector.ExceptionDirectorEvent;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.aop
 * @since 8/13/2023 11:11 PM
 */
@Aspect
@Component
public class ExecutionExceptionAspect {

    private final ApplicationEventPublisher publisher;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ExecutionExceptionAspect(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Pointcut("execution(* org.khasanof.springbootstarterfluent.core.executors.execution.Execution.run(..))")
    void executionRunMethodPointcut(){}

    @AfterThrowing(value = "executionRunMethodPointcut()", throwing = "ex")
    private void afterThrowing(JoinPoint joinPoint, Throwable ex) throws Throwable {
        FluentContext.updateExecutorBoolean.set(true);
        if (ex.getClass().equals(InvocationTargetException.class)) {
            MethodV1Event event = MethodUtils.getArg(joinPoint.getArgs(), MethodV1Event.class);
            Object[] args = event.getInvokerModel().getArgs();
            AbsSender sender = MethodUtils.getArg(args, AbsSender.class);
            Update update = MethodUtils.getArg(args, Update.class);
            publisher.publishEvent(new ExceptionDirectorEvent(this, update, sender, ex.getCause()));
        } else {
            throw ex;
        }
    }

}
