package org.khasanof.springbootstarterfluent.core.executors.execution;

import lombok.SneakyThrows;
import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.execution
 * @since 8/13/2023 9:19 PM
 */
@Component(CommonExecutionAdapter.NAME)
public class CommonExecutionAdapter implements ApplicationContextAware {

    public static final String NAME = "commonExecutionAdapter";

    private final List<Execution> executions = new ArrayList<>();
    private Execution simpleExecution;

    public void execution(MethodV1Event event) {
        if (Objects.isNull(event.getInvokerModel().getAdditionalParam())) {
            try {
                simpleExecution.run(event);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            AdditionalParamType paramType = event.getInvokerModel().getAdditionalParam().getType();
            for (Execution execution : executions) {
                if (execution.getType().equals(paramType)) {
                    try {
                        execution.run(event);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(Execution.class).forEach((s, execution) -> {
            if (Objects.isNull(execution.getType())) {
                simpleExecution = execution;
            } else {
                executions.add(execution);
            }
        });
    }
}
