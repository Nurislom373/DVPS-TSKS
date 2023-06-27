package org.khasanof.core.executors;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.collector.impls.SimpleCollector;
import org.khasanof.core.model.MethodArgs;
import org.khasanof.core.sender.AbstractSender;
import org.khasanof.main.inferaces.sender.Sender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

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
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
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
