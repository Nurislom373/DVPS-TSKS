package org.khasanof.springbootstarterfluent.core.executors.execution.executions;

import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.executors.execution.Execution;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.main.annotation.extra.BotVariable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.execution.executions
 * @since 8/13/2023 9:42 PM
 */
@Component
public class VarExpressionExecution implements Execution {

    @Override
    @SuppressWarnings("unchecked")
    public void run(MethodV1Event methodV1Event) throws InvocationTargetException, IllegalAccessException {
        AdditionalParamType paramType = methodV1Event.getInvokerModel().getAdditionalParam().getType();
        Object[] args = methodV1Event.getInvokerModel().getArgs();

        Object stringMap = Arrays.stream(methodV1Event.getInvokerModel().getArgs())
                .filter(o -> o.getClass().equals(paramType.getParmaType()))
                .findFirst().orElseThrow(() -> new RuntimeException("Match object not found!"));

        Object[] objects = mapGetValues((Map<String, String>) stringMap, methodV1Event.getClassEntry().getKey());
        int length = methodV1Event.getInvokerModel().getArgs().length;

        Object[] array = Arrays.stream(args).filter(o -> !o.getClass().equals(HashMap.class)).toArray();
        Object[] copy = Arrays.copyOf(array, length + objects.length);
        System.arraycopy(objects, 0, copy, length, objects.length);

        methodV1Event.getClassEntry().getKey().invoke(methodV1Event.getClassEntry().getValue(),
                MethodUtils.cleanerV2(copy));
    }

    @Override
    public AdditionalParamType getType() {
        return AdditionalParamType.VAR_EXPRESSION_PARAM;
    }

    private Object[] mapGetValues(Map<String, String> stringMap, Method method) {
        List<String> variables = new ArrayList<>();
        Arrays.stream(method.getParameterAnnotations()).forEach(annotations -> {
            Annotation fAnn = Arrays.stream(annotations)
                    .filter(annotation -> annotation.annotationType().equals(BotVariable.class))
                    .findFirst().orElse(null);
            if (Objects.nonNull(fAnn)) {
                BotVariable botVariable = (BotVariable) fAnn;
                String var = stringMap.get(botVariable.value());
                if (Objects.nonNull(var)) {
                    variables.add(var);
                }
            }
        });
        return variables.toArray();
    }

}
