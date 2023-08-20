package org.khasanof.springbootstarterfluent.core.collector;

import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 12:30
 * <br/>
 * Package: org.khasanof.core.collector
 */
@Component(SimpleCollector.NAME)
public class SimpleCollector extends AbstractCollector implements Collector<Class<? extends Annotation>> {

    public static final String NAME = "simpleCollector";

    public SimpleCollector(QuestMethod<HandleClasses> questMethod, AnnotationMethodContext<Map<Method, Object>> annotationContext) {
        super(questMethod, annotationContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public InvokerResult getMethodValueAnn(Object value, Class<? extends Annotation> annotation) {
        return questMethod.getMethodValueAnn(value, HandleClasses.getHandleWithType(annotation));
    }

    @Override
    public InvokerResult getHandleAnyMethod(HandleType handleType) {
        return questMethod.getHandleAnyMethod(handleType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<InvokerResult> getAllHandleAnyMethod(HandleType handleType) {
        return questMethod.getAllHandleAnyMethod(handleType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasHandle(Class<? extends Annotation> annotation) {
        return annotationContext.containsKey(annotation);
    }

}
