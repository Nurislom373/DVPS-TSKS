package org.khasanof.core.executors;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.collector.impls.SimpleCollector;
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

    protected void invoke(Map.Entry<Method, Class> entry, AbstractSender sender) {
        try {
            if (Objects.nonNull(entry)) {
                Method method = entry.getKey();
                method.setAccessible(true);
                Object[] objects = classCastParams(method.getParameterTypes(), sender);
                method.invoke(entry.getValue().newInstance(), objects);
            } else {
                System.out.println("Method not found!");
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] classCastParams(Class<?>[] classes, AbstractSender sender) {
        if (classes.length == 1) {
            return new Object[]{sender};
        } else {
            Object[] objs = new Object[2];
            if (classes[0].equals(Update.class)) {
                objs[0] = sender.getUpdate();
                objs[1] = sender.getSender();
            } else {
                objs[0] = sender.getSender();
                objs[1] = sender.getUpdate();
            }
            return objs;
        }
    }

    private boolean methodArgsCheck(Method method) {
        int parameterCount = method.getParameterCount();
        if (parameterCount > 2) throw new RuntimeException("Invalid Parameters!");
        return paramsTypeCheck(method.getParameterTypes());
    }

    private boolean paramsTypeCheck(Class<?>[] classes) {
        Class<?> firstParam = classes[0];
        boolean isSender = false, isUpdate = false, isAbsSender = false;
        if (firstParam.equals(Sender.class)) {
            if (classes.length == 1) {
                isSender = true;
            }
            return isSender;
        } else if (firstParam.equals(Update.class) || firstParam.equals(AbsSender.class)) {
            if (firstParam.equals(Update.class)) isUpdate = true;
            else isAbsSender = true;
            Class<?> secondParam = classes[1];
            if (secondParam.equals(Update.class) || secondParam.equals(AbsSender.class)) {
                if (secondParam.equals(Update.class)) isUpdate = true;
                else isAbsSender = true;
                return isAbsSender && isUpdate;
            }
        } else {
            return false;
        }
        return false;
    }

}
