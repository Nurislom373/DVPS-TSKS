package org.khasanof.springbootstarterfluent.core.executors.execution.executions;

import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.executors.execution.AbstractExecution;
import org.khasanof.springbootstarterfluent.core.executors.execution.Execution;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.execution.executions
 * @since 8/20/2023 5:17 PM
 */
@Component
public class StateParamExecution extends AbstractExecution implements Execution {

    @Override
    public void run(MethodV1Event methodV1Event) throws InvocationTargetException, IllegalAccessException {
        defaultExecution(methodV1Event);
    }

    @Override
    public AdditionalParamType getType() {
        return AdditionalParamType.STATE_PARAM;
    }
}
