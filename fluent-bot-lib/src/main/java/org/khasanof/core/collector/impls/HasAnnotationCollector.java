package org.khasanof.core.collector.impls;

import org.khasanof.main.annotation.HandlerScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 11:28
 * <br/>
 * Package: org.khasanof.core.collector
 */
public class HasAnnotationCollector {

    private final ClassloaderInPackage classloader = new ClassloaderInPackage();

    public Map<Method, Class> methodsWithAnnotation(Class<? extends Annotation> annotation){
        HandlerScanner handlerScanner = (HandlerScanner) classloader.findAllClassesWithHandlerScannerClass()
                .getAnnotation(HandlerScanner.class);
        return classloader.findAllClassesUsingClassLoader(handlerScanner.value())
                .stream().filter(f -> hasAnnotation(annotation, f))
                .flatMap(m -> getHasAnnotationMethods(annotation, m))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean hasAnnotation(Class<? extends Annotation> annotation, Class clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .anyMatch(any -> any.isAnnotationPresent(annotation));
    }

    private Stream<Map.Entry<Method, Class>> getHasAnnotationMethods(Class<? extends Annotation> annotation, Class clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(f -> f.isAnnotationPresent(annotation))
                .map(method -> new AbstractMap.SimpleEntry<>(method, clazz));
    }

}
