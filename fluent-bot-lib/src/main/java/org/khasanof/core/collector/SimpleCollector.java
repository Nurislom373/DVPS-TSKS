package org.khasanof.core.collector;

import org.khasanof.core.collector.AbstractCollector;
import org.khasanof.core.collector.Collector;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.HandleType;

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
public class SimpleCollector extends AbstractCollector implements Collector {

    @Override
    public Map.Entry<Method, Class> getMethodValueAnn(Object value, Class<? extends Annotation> annotation) {
        return questMethod.getMethodValueAnn(value, HandleClasses.getHandleWithType(annotation));
    }

    @Override
    public Map.Entry<Method, Class> getHandleAnyMethod(HandleType handleType) {
        return questMethod.getHandleAnyMethod(handleType);
    }

    @Override
    public boolean hasHandleAnyMethod() {
        return methodAdapter.hasHandleAnyMethod();
    }

}
