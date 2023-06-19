package org.khasanof.core.collector.impls;

import org.khasanof.core.collector.Collector;
import org.khasanof.main.annotation.HandleCallback;
import org.khasanof.main.annotation.HandleMessage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final Map<Class<? extends Annotation>, Map<Method, Class>> keyMethods;
    private static final HasAnnotationCollector hasAnnotationCollector = new HasAnnotationCollector();

    static {
        keyMethods = new ConcurrentHashMap<>(){{
            put(HandleMessage.class, hasAnnotationCollector.methodsWithAnnotation(HandleMessage.class));
            put(HandleCallback.class, hasAnnotationCollector.methodsWithAnnotation(HandleCallback.class));
        }};
        keyMethods.entrySet().forEach(System.out::println);
    }

    @Override
    public Map.Entry<Method, Class> getMethodValue(String value) {
        return keyMethods.entrySet()
                .stream()
                .flatMap(classSetEntry -> classSetEntry.getValue().entrySet()
                        .stream().filter(any -> methodHasVal(any.getKey(), value, classSetEntry.getKey())))
                .findFirst().orElseThrow(() -> new RuntimeException("Method not found!"));
    }

    @Override
    public Map.Entry<Method, Class> getMethodValueAnn(String value, Class<? extends Annotation> annotation) {
        return keyMethods.get(annotation).entrySet()
                .stream().filter(aClass -> methodHasVal(aClass.getKey(), value, annotation))
                .findFirst().orElse(null);
    }

    private boolean methodHasVal(Method method, String value, Class<? extends Annotation> annotation) {
        if (annotation.equals(HandleCallback.class)) {
            return methodHasValCallback(method, value);
        } else if (annotation.equals(HandleMessage.class)) {
            return methodHasValMessage(method, value);
        }
        return false;
    }

    private boolean methodHasValMessage(Method method, String value) {
        return method.getAnnotation(HandleMessage.class).value().equals(value);
    }

    private boolean methodHasValCallback(Method method, String value) {
        return method.getAnnotation(HandleCallback.class).value().equals(value);
    }


}
