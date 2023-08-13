package org.khasanof.springbootstarterfluent.core.event.methodInvoke;

import lombok.*;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.springframework.context.ApplicationEvent;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.event.methodInvoke
 * @since 8/13/2023 8:59 PM
 */
@Getter
@Setter
@ToString
public class MethodV1Event extends ApplicationEvent {

    private InvokerModel invokerModel;
    private Map.Entry<Method, Object> classEntry;
    private Method method;

    public MethodV1Event(Object source, InvokerModel invokerModel, Map.Entry<Method, Object> classEntry, Method method) {
        super(source);
        this.invokerModel = invokerModel;
        this.classEntry = classEntry;
        this.method = method;
    }
}
