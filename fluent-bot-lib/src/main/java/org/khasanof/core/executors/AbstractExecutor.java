package org.khasanof.core.executors;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.collector.SimpleCollector;
import org.khasanof.core.custom.FluentContext;
import org.khasanof.core.model.MethodArgs;
import org.khasanof.core.utils.MethodUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 13:01
 * <br/>
 * Package: org.khasanof.core.executors
 */
public abstract class AbstractExecutor {

    protected final Collector collector = new SimpleCollector();
    private final CommonExceptionExecutor exceptionExecutor = new CommonExceptionExecutor(collector);

    protected void invoke(Map.Entry<Method, Class> entry, MethodArgs args) {
        try {
            if (Objects.nonNull(entry)) {
                Method method = entry.getKey();
                method.setAccessible(true);
                Object[] objects = classCastParams(method.getParameterTypes(), args);
                method.invoke(entry.getValue().newInstance(), objects);
            } else {
                System.out.println("Method not found!");
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            try {
                exceptionExecutor.director(new MethodArgs(args.update(), args.sender(), e.getCause()));
                FluentContext.booleanLocal.set(true);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected void invoke(Map.Entry<Method, Class> entry, Object... args) {
        try {
            if (Objects.nonNull(entry)) {
                Method method = entry.getKey();
                method.setAccessible(true);
                Object[] objects = MethodUtils.sorter(args, entry.getKey().getParameterTypes());
                method.invoke(entry.getValue().newInstance(), objects);
            } else {
                System.out.println("Method not found!");
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            try {
                Object[] newArray = new Object[args.length + 1];
                System.arraycopy(args, 0, newArray, 0, args.length);
                newArray[newArray.length - 1] = e.getCause();
                exceptionExecutor.director(MethodUtils.argsToClass(newArray, MethodArgs.class));
                FluentContext.booleanLocal.set(true);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private Object[] classCastParams(Class<?>[] classes, MethodArgs args) {
        Object[] objs = new Object[2];
        if (classes[0].equals(Update.class)) {
            objs[0] = args.update();
            objs[1] = args.sender();
        } else {
            objs[0] = args.sender();
            objs[1] = args.update();
        }
        return objs;
    }

}
