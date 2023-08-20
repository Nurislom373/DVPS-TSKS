package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.enums.InvokerType;
import org.khasanof.springbootstarterfluent.core.model.InvokerMethod;
import org.khasanof.springbootstarterfluent.core.model.InvokerObject;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.invoker
 * @since 8/20/2023 3:49 PM
 */
@Component
public class InvokerResultService {

    public Map.Entry<Method, Object> getResultEntry(InvokerResult result) {
        if (result.getType().equals(InvokerType.CLASS)) {
            InvokerObject invokerObject = (InvokerObject) result;
            return new AbstractMap.SimpleEntry<>(MethodUtils.getClassMethodByName(invokerObject.getReference(),
                    invokerObject.getExecutionMethodName()), invokerObject.getReference());
        } else {
            InvokerMethod invokerMethod = (InvokerMethod) result;
            return new AbstractMap.SimpleEntry<>(invokerMethod.getMethod(), invokerMethod.getReference());
        }
    }

}
