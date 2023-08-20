package org.khasanof.springbootstarterfluent.core.collector.questMethod;

import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 23.06.2023 23:43
 */
public interface QuestMethod<P> {

    InvokerResult getMethodValueAnn(Object value, P param);

    default boolean containsKey(P param) {
        return false;
    }

    default InvokerResult getHandleAnyMethod(HandleType handleType) {
        return null;
    }

    default Set<InvokerResult> getAllHandleAnyMethod(HandleType handleType) {
        return null;
    }

}
