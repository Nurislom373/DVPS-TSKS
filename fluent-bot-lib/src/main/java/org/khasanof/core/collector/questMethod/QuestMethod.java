package org.khasanof.core.collector.questMethod;

import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.HandleType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 23.06.2023 23:43
 */
public interface QuestMethod {

    Map.Entry<Method, Class> getMethodValueAnn(String value, HandleClasses type);

    Map.Entry<Method, Class> getHandleAnyMethod(HandleType handleType);


}
