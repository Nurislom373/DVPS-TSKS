package org.khasanof.springbootstarterfluent.core.collector.questMethod.impls;

import org.khasanof.springbootstarterfluent.core.collector.GenericMethodContext;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;
import org.khasanof.springbootstarterfluent.core.model.InvokerMethod;
import org.khasanof.springbootstarterfluent.core.model.InvokerObject;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector.questMethod.impls
 * @since 8/19/2023 2:16 PM
 */
public class AsyncStateQuestMethod implements QuestMethod<Enum> {

    private final GenericMethodContext<Enum, Map.Entry<Method, Object>> methodContext;

    public AsyncStateQuestMethod(GenericMethodContext<Enum, Map.Entry<Method, Object>> methodContext) {
        this.methodContext = methodContext;
    }

    @Override
    public InvokerResult getMethodValueAnn(Object value, Enum param) {
        return resultCreator(methodContext.getMethodsByT(param));
    }

    @Override
    public boolean containsKey(Enum param) {
        return methodContext.containsKey(param);
    }

    private InvokerResult resultCreator(Map.Entry<Method, Object> entry) {
        if (Objects.isNull(entry)) {
            return null;
        }
        return new InvokerObject(entry.getValue(), "onReceive");
    }
}
