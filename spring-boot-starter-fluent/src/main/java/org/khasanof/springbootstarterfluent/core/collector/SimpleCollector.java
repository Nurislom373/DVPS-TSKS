package org.khasanof.springbootstarterfluent.core.collector;

import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 12:30
 * <br/>
 * Package: org.khasanof.core.collector
 */
@Component
public class SimpleCollector extends AbstractCollector implements Collector {

    public SimpleCollector(CommonMethodAdapter commonMethodAdapter, QuestMethod questMethod) {
        super(commonMethodAdapter, questMethod);
    }

    @Override
    public Map.Entry<Method, Object> getMethodValueAnn(Object value, Class<? extends Annotation> annotation) {
        return questMethod.getMethodValueAnn(value, HandleClasses.getHandleWithType(annotation));
    }

    @Override
    public Map.Entry<Method, Object> getHandleAnyMethod(HandleType handleType) {
        return questMethod.getHandleAnyMethod(handleType);
    }

    @Override
    public Map<Method, Object> getAllHandleAnyMethod(HandleType handleType) {
        return questMethod.getAllHandleAnyMethod(handleType);
    }

    @Override
    public boolean hasHandle(Class<? extends Annotation> annotation) {
        return methodAdapter.hasHandle(annotation);
    }

}
