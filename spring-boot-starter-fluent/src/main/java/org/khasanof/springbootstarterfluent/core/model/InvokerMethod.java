package org.khasanof.springbootstarterfluent.core.model;

import lombok.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model
 * @since 8/8/2023 9:31 PM
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvokerMethod {
    private Annotation annotation;
    private Class<? extends Annotation> annotationType;
    private Method method;
    private Object reference;
}
