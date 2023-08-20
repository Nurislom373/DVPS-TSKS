package org.khasanof.springbootstarterfluent.core.model;

import lombok.*;
import org.khasanof.springbootstarterfluent.core.enums.InvokerType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model
 * @since 8/8/2023 9:31 PM
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvokerMethod implements InvokerResult {

    private Method method;
    private Object reference;

    @Override
    public InvokerType getType() {
        return InvokerType.METHOD;
    }
}
