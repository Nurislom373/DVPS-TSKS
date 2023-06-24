package org.khasanof.core.collector.impls;

import lombok.Getter;
import org.khasanof.core.collector.ClassloaderPackageCollector;
import org.khasanof.core.collector.loader.HandleScannerLoader;
import org.khasanof.core.collector.methodChecker.AbstractMethodChecker;
import org.khasanof.core.collector.methodChecker.SimpleMethodChecker;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.main.annotation.HandlerScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 11:28
 * <br/>
 * Package: org.khasanof.core.collector
 */
@Getter
public class AnnotationCollector {

    private final ClassloaderPackageCollector classloader = new ClassloaderPackageCollector();
    private final HandleScannerLoader handleScannerLoader = new HandleScannerLoader();
    private final Map<HandleClasses, Map<Method, Class>> collectMap = new HashMap<>();
    private final AbstractMethodChecker methodChecker = new SimpleMethodChecker();

    public AnnotationCollector() {
        setMethodClassMap();
    }

    public Map<Method, Class> methodsWithAnnotation(Class<? extends Annotation> annotation) {
        return collectMap.get(HandleClasses.getHandleWithType(annotation));
    }

    void setMethodClassMap() {
        for (Iterator<Class> iterator = getScanner().iterator(); iterator.hasNext();) {
            Class clazz = iterator.next();
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                if (methodChecker.valid(method)) {
                    HandleClasses key = getMethodAnnotation(method);
                    if (collectMap.containsKey(key)) {
                        collectMap.get(key).put(method, clazz);
                    } else {
                        collectMap.put(key, new HashMap<>() {{
                            put(method, clazz);
                        }});
                    }
                }
            });
        }
        collectMap.forEach((key, value) -> System.out.println(key + " : " + value.size()));
    }

    private Set<Class> getScanner() {
        HandlerScanner handlerScanner = (HandlerScanner) handleScannerLoader.findAllClassesWithHandlerScannerClass()
                .getAnnotation(HandlerScanner.class);
        handleScannerLoader.setBasePackage(handlerScanner.value());
        Set<Class> allValidClasses = classloader.getAllClasses(handlerScanner.value());
        return allValidClasses;
    }

    private HandleClasses getMethodAnnotation(Method method) {
        Annotation[] annotations = method.getAnnotations();
        if (annotations.length == 0) {
            return null;
        } else {
            return HandleClasses.getHandleWithType(
                    HandleClasses.getAllAnnotations()
                            .stream().map(method::getAnnotation)
                            .filter(Objects::nonNull).map(Annotation::annotationType)
                            .findFirst().orElse(null));
        }
    }

}
