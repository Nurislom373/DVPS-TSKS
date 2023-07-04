package org.khasanof.core.executors;

import org.khasanof.core.model.MethodArgs;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors
 * @since 04.07.2023 22:22
 */
public interface Invoker {

    static void invoke(Map.Entry<Method, Class> entry, MethodArgs args) {
        try {
            if (Objects.nonNull(entry)) {
                absInvoke(entry, args);
            } else {
                System.out.println("Method not found!");
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void absInvoke(Map.Entry<Method, Class> entry, MethodArgs args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Method method = entry.getKey();
        method.setAccessible(true);
        Object[] objects = classConvertToObjects(args);
        method.invoke(entry.getValue().newInstance(), objects);
    }

    private static Object[] classConvertToObjects(MethodArgs args) throws IllegalAccessException {
        Object[] objects = new Object[args.getClass().getDeclaredFields().length];
        Field[] declaredFields = args.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.getType().equals(Throwable.class)) {
                objects[0] = declaredField.get(args);
            } else if (declaredField.getType().equals(Update.class)) {
                objects[1] = declaredField.get(args);
            } else if (declaredField.getType().equals(AbsSender.class)) {
                objects[2] = declaredField.get(args);
            }
        }
        return objects;
    }

}
