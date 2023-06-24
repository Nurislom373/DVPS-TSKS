package org.khasanof.core.collector;

import org.khasanof.core.collector.flattenPackage.PackageCollector;
import org.khasanof.core.collector.flattenPackage.impls.RecursiveFlattenPackageCollector;
import org.khasanof.core.collector.flattenPackage.impls.SimpleFlattenPackageCollector;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.main.annotation.HandleUpdate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
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
public class ClassloaderPackageCollector {

    private final PackageCollector flattenPackageCollector = new RecursiveFlattenPackageCollector();

    public Set<Class> getAllClasses(String packageName) {
        Set<Class> collect = flattenPackageCollector.getFolders(packageName)
                .stream().map(this::findAllClassesUsingClassLoader)
                .filter(Objects::nonNull).flatMap(Collection::stream)
                .collect(Collectors.toSet());
        System.out.println("collect.size() = " + collect.size());
        return validClassGet(collect);
    }

    private Set<Class> validClassGet(Set<Class> classes) {
        return classes.stream()
                .peek(System.out::println)
                .filter(aClass -> hasAnnotationClassLevel(aClass, HandleUpdate.class))
                .filter(clazz -> Arrays.stream(clazz.getDeclaredMethods())
                        .anyMatch(method -> hasAnnotationMethodLevel(method, HandleClasses.getAllAnnotations())))
                .collect(Collectors.toSet());
    }

    private Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .peek(line -> System.out.println("Line : " + line))
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName.replaceAll("/", ".")))
                .peek(line -> System.out.println("File Line : " + line))
                .collect(Collectors.toSet());
    }

    private boolean hasAnnotationClassLevel(Class aClass, Class<? extends Annotation> annotation) {
        System.out.println("aClass = " + aClass);
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
        System.out.println("className = " + className);
        System.out.println("packageName = " + packageName);
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
            return null;
        }
    }

}
