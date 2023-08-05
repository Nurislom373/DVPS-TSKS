package org.khasanof.springbootstarterfluent.core.collector.questMethod;

import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 23.06.2023 23:43
 */
public interface QuestMethod {

    Map.Entry<Method, Object> getMethodValueAnn(Object value, HandleClasses type);

    Map.Entry<Method, Object> getHandleAnyMethod(HandleType handleType);

    Map<Method, Object> getAllHandleAnyMethod(HandleType handleType);

}
