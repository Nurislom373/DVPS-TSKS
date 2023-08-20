package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.core.model.InvokerModelV2;
import org.khasanof.springbootstarterfluent.core.model.MethodArgs;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;

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

    void invoke(InvokerModel invokerModel);

    void invokeV2(InvokerModelV2 invokerModelV2);

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
        Object[] objects = MethodUtils.sorter(MethodUtils.classConvertToObjects(args),
                method.getParameterTypes());
        method.invoke(entry.getValue().newInstance(), objects);
    }

}
