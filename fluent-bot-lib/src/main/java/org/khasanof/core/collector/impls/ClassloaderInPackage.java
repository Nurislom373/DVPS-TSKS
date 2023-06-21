package org.khasanof.core.collector.impls;

import com.google.common.reflect.ClassPath;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.main.annotation.HandleUpdate;
import org.khasanof.main.annotation.HandlerScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 11:32
 * <br/>
 * Package: org.khasanof.core.collector
 */
public class ClassloaderInPackage {

    public Set<Class> getAllValidClasses(String packageName) {
        Set<Class> classes = findAllClassesUsingClassLoader(packageName);
        return validClassGet(classes);
    }

    private Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Set<Class> validClassGet(Set<Class> classes) {
        return classes.stream()
                .filter(aClass -> hasAnnotationClassLevel(aClass, HandleUpdate.class))
                .filter(clazz -> Arrays.stream(clazz.getDeclaredMethods())
                        .anyMatch(method -> hasAnnotationMethodLevel(method, HandleClasses.getAllAnnotations())))
                .collect(Collectors.toSet());
    }

    public Class findAllClassesWithHandlerScannerClass() {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream().map(clazz -> clazz.load())
                    .filter(this::hasAnnotationClassLevel)
                    .findFirst().orElseThrow(() -> new RuntimeException("Class not found!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasAnnotationClassLevel(Class clazz) {
        return clazz.isAnnotationPresent(HandlerScanner.class);
    }

    private boolean hasAnnotationClassLevel(Class aClass, Class<? extends Annotation> annotation) {
        return aClass.isAnnotationPresent(annotation);
    }

    private boolean hasAnnotationMethodLevel(Method method, Set<Class<? extends Annotation>> annotations) {
        int length = method.getAnnotations().length;
        if (length == 0) {
            return false;
        } else if (length == 1) {
            return annotations.stream()
                    .anyMatch(method::isAnnotationPresent);
        } else {
            int count = 0;
            for (Annotation annotation : method.getAnnotations()) {
                if (annotations.contains(annotation.annotationType())) {
                    count++;
                }
            }
            if (count == 1) {
                return true;
            }
            throw new RuntimeException("handle annotations are required to be single!");
        }
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

}
