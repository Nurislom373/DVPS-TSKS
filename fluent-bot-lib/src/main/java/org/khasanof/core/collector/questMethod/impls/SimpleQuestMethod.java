package org.khasanof.core.collector.questMethod.impls;

import org.khasanof.core.collector.impls.AnnotationCollector;
import org.khasanof.core.collector.questMethod.QuestMethod;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.executors.matcher.CompositeMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 24.06.2023 0:11
 */
public class SimpleQuestMethod implements QuestMethod {

    private final AnnotationCollector annotationCollector = new AnnotationCollector();
    private final CompositeMatcher matcher = new CompositeMatcher();

    @Override
    public Map.Entry<Method, Class> getMethodValueAnn(String value, HandleClasses type) {
        return annotationCollector.methodsWithAnnotation(type.getType()).entrySet()
                .stream().filter(aClass -> methodHasVal(aClass.getKey(), value, type.getType()))
                .findFirst().orElse(null);
    }

    private boolean methodHasVal(Method method, String value, Class<? extends Annotation> annotation) {
        return matcher.chooseMatcher(method, value, annotation);
    }

}
