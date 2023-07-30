package org.khasanof.core.executors.invoker;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.custom.FluentContext;
import org.khasanof.core.exceptions.InvalidParamsException;
import org.khasanof.core.model.InvokerModel;
import org.khasanof.core.utils.MethodUtils;
import org.khasanof.main.annotation.exception.HandleException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.invoker
 * @since 16.07.2023 15:01
 */
public class InvokerExecutor implements Invoker {

    private final Collector collector;
    private final InvokerFunctions invokerFunctions = new InvokerFunctions();

    public InvokerExecutor(Collector collector) {
        this.collector = collector;
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
        checkListParams(invokerModel.getMethodParams(), invokerModel.getArgs());
        Map.Entry<Method, Class> classEntry = invokerModel.getClassEntry();

        if (invokerModel.isHasMainParam() && !invokerModel.isInputSystem()) {
            if (InvokerFunctions.HANDLE_UPDATE_W_PROCESS_FL.equals(invokerModel.getName())) {
                Method method = invokerModel.getClassEntry().getKey();
                if (method.getParameterCount() > 2) {
                    getMainParamAndFillArgs(invokerModel);
                }
            } else {
                getMainParamAndFillArgs(invokerModel);
            }
        }

        Method method = classEntry.getKey();
        method.setAccessible(true);

        if (invokerModel.isCanBeNoParam()) {
            if (method.getParameterCount() == 0) {
                method.invoke(classEntry.getValue().newInstance());
            } else {
                execute(invokerModel, classEntry, method);
            }
        } else {
            execute(invokerModel, classEntry, method);
        }
    }

    private static void execute(InvokerModel invokerModel, Map.Entry<Method, Class> classEntry, Method method) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] objects = MethodUtils.sorterV2(invokerModel.getArgs(), method.getParameterTypes());
        method.invoke(classEntry.getValue().newInstance(), objects);
    }

    private void checkListParams(List<Class<?>> params, Object[] args) {
        boolean allMatch = Arrays.stream(args).allMatch(arg -> params.contains(arg.getClass()) || params.stream()
                .anyMatch(any -> any.isAssignableFrom(arg.getClass())));
        if (!allMatch) {
            throw new InvalidParamsException("Param type doesn't match expected param!");
        }
    }

    private void getMainParamAndFillArgs(InvokerModel invokerModel) {
        InvokerModel.MainParam mainParam = invokerModel.getMainParam();
        Object[] args = invokerModel.getArgs();
        Object apply = mainParam.getValueFunction()
                .apply(MethodUtils.getArg(invokerModel.getArgs(), Update.class));
        if (Objects.nonNull(apply)) {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = apply;
        }
    }

    private void exceptionDirector(Throwable throwable, InvokerModel prevInvoker) throws Throwable {
        Map.Entry<Method, Class> exceptionHandleMethod = getExceptionHandleMethod(throwable);
        if (Objects.isNull(exceptionHandleMethod)) {
            throw throwable;
        }
        InvokerModel invokerModel = invokerFunctions.fillAndGet(exceptionHandleMethod, throwable,
                MethodUtils.getArg(prevInvoker.getArgs(), Update.class), MethodUtils.getArg(
                        prevInvoker.getArgs(), AbsSender.class));
        invoke(invokerModel);
    }

    private Map.Entry<Method, Class> getExceptionHandleMethod(Throwable throwable) throws Throwable {
        if (collector.hasHandle(HandleException.class)) {
            return collector.getMethodValueAnn(throwable, HandleException.class);
        }
        throw throwable;
    }

}
