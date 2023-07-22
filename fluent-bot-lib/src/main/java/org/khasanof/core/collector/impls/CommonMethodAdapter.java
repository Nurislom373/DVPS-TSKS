package org.khasanof.core.collector.impls;

import lombok.Getter;
import org.khasanof.core.collector.ClassloaderPackageCollector;
import org.khasanof.core.collector.loader.HandleScannerLoader;
import org.khasanof.core.collector.methodChecker.AbstractMethodChecker;
import org.khasanof.core.collector.methodChecker.MethodCheckerAdapter;
import org.khasanof.core.collector.methodChecker.SimpleMethodChecker;
import org.khasanof.core.enums.HandleClasses;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
public class CommonMethodAdapter {

    private final ClassloaderPackageCollector classloader = new ClassloaderPackageCollector();
    private final HandleScannerLoader handleScannerLoader = new HandleScannerLoader();
    private final Map<HandleClasses, Map<Method, Class>> collectMap = new HashMap<>();
    private final AbstractMethodChecker methodChecker = new SimpleMethodChecker();
    private final MethodCheckerAdapter methodCheckerAdapter = new MethodCheckerAdapter();
    private final CommonInterfaceAdapter interfaceAdapter = new CommonInterfaceAdapter();

    public CommonMethodAdapter() {
        setMethodClassMap();
    }

    public Map<Method, Class> methodsWithAnnotation(Class<? extends Annotation> annotation) {
        return collectMap.get(HandleClasses.getHandleWithType(annotation));
    }

    public boolean hasHandle(Class<? extends Annotation> annotation) {
        return collectMap.containsKey(HandleClasses.getHandleWithType(annotation));
    }

    void setMethodClassMap() {
        for (Iterator<Class> iterator = getScanner().iterator(); iterator.hasNext();) {
            final Class clazz = iterator.next();
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                if (methodCheckerAdapter.valid(method)) {
                    HandleClasses key = getMethodAnnotation(method);
                    if (collectMap.containsKey(key)) {
                        Class classInstance = getClassInstance(clazz);
                        if (Objects.nonNull(classInstance)) {
                            collectMap.get(key).put(method, classInstance);
                        }
                    } else {
                        collectMap.put(key, new HashMap<>() {{
                            Class classInstance = getClassInstance(clazz);
                            if (Objects.nonNull(classInstance)) {
                                put(method, classInstance);
                            }
                        }});
                    }
                }
            });
        }
        collectMap.forEach((key, value) -> System.out.println(key + " : " + value.size()));
    }

    private Class getClassInstance(Class clazz) {
        return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()) ?
                interfaceAdapter.getInterfaceSubclass(clazz) : clazz;
    }

    private Set<Class> getScanner() {
        Set<Class> allValidClasses = classloader.getAllClasses(handleScannerLoader.getBasePackage());
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
