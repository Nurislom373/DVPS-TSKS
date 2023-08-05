package org.khasanof.springbootstarterfluent.core.executors;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.executors.invoker.Invoker;
import org.khasanof.springbootstarterfluent.core.model.MethodArgs;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors
 * @since 04.07.2023 21:41
 */
public class CommonExceptionExecutor {

    private final Collector collector;

    public CommonExceptionExecutor(Collector collector) {
        this.collector = collector;
    }

    public void director(MethodArgs args) throws Throwable {
        if (collector.hasHandle(HandleException.class)) {
            Map.Entry<Method, Class> entry = collector.getMethodValueAnn(args.throwable(), HandleException.class);
            if (Objects.nonNull(entry)) {
                Invoker.invoke(entry, args);
            } else {
                throw args.throwable();
            }
        } else {
            throw args.throwable();
        }
    }

}
