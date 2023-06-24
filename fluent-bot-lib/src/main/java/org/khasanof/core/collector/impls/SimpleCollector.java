package org.khasanof.core.collector.impls;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.collector.questMethod.QuestMethod;
import org.khasanof.core.collector.questMethod.impls.AsyncQuestMethod;
import org.khasanof.core.enums.HandleClasses;

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
public class SimpleCollector implements Collector {

    private final QuestMethod questMethod = new AsyncQuestMethod();

    @Override
    public Map.Entry<Method, Class> getMethodValueAnn(String value, Class<? extends Annotation> annotation) {
        return questMethod.getMethodValueAnn(value, HandleClasses.getHandleWithType(annotation));
    }

}
