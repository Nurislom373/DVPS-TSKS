package org.khasanof.springbootstarterfluent.core.collector;

import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector
 * @since 8/19/2023 3:06 PM
 */
public interface HandleTypeCollector {

    default InvokerResult getHandleAnyMethod(HandleType handleType) {
        throw new RuntimeException("Not implemented!");
    }

    default Set<InvokerResult> getAllHandleAnyMethod(HandleType handleType) {
        throw new RuntimeException("Not implemented!");
    }

}
