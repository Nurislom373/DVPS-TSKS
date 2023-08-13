package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.invoker
 * @since 8/11/2023 10:00 PM
 */
@Component(value = DefaultInvokerMatcher.NAME)
public class DefaultInvokerMatcher {

    public static final String NAME = "defaultInvokerMatcher";

    public boolean messageScopeEq(InvokerModel invokerModel) {
        Annotation[] annotations = invokerModel.getClassEntry().getKey().getDeclaredAnnotations();
        return Arrays.stream(annotations).anyMatch(annotation -> {
            if (annotation.annotationType().equals(HandleMessage.class)) {
                HandleMessage handleMessage = (HandleMessage) annotation;
                return handleMessage.scope().equals(MatchScope.VAR_EXPRESSION);
            }
            return false;
        });
    }

}
