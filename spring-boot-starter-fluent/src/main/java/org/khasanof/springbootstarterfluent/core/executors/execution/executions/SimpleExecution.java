package org.khasanof.springbootstarterfluent.core.executors.execution.executions;

import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.executors.execution.Execution;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.execution.executions
 * @since 8/13/2023 9:28 PM
 */
@Component
public class SimpleExecution implements Execution {

    @Override
    public void run(MethodV1Event methodV1Event) throws InvocationTargetException, IllegalAccessException {
        Object[] objects = MethodUtils.sorterV2(methodV1Event.getInvokerModel().getArgs(),
                methodV1Event.getMethod().getParameterTypes());
        methodV1Event.getMethod().invoke(methodV1Event.getClassEntry().getValue(), objects);
    }

    @Override
    public AdditionalParamType getType() {
        return null;
    }
}
