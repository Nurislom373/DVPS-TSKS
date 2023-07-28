package org.khasanof.core.collector;

import org.khasanof.core.collector.flattenPackage.PackageCollector;
import org.khasanof.core.collector.flattenPackage.impls.RecursiveFlattenPackageCollector;
import org.khasanof.core.config.Config;
import org.khasanof.core.config.FluentConfig;
import org.khasanof.core.enums.ClassLevelTypes;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.ProcessType;
import org.khasanof.main.annotation.ExceptionController;
import org.khasanof.main.annotation.StateController;
import org.khasanof.main.annotation.UpdateController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
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
public class MainClassloader implements Config {

    private final PackageCollector flattenPackageCollector = new RecursiveFlattenPackageCollector();
    private final Set<Class<? extends Annotation>> classLevelAnnotations = new HashSet<>();
    private final FluentConfig config = FluentConfig.getInstance();

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
                .filter(aClass -> hasAnnotationClassLevel(aClass, classLevelAnnotations))
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

    private boolean hasAnnotationClassLevel(Class aClass, Set<Class<? extends Annotation>> annotations) {
        return annotations.stream().anyMatch(aClass::isAnnotationPresent);
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

    @Override
    public void runnable() {
        ProcessType processType = config.getConfig().getProcessType();
        if (processType.equals(ProcessType.BOTH)) {
            this.classLevelAnnotations.addAll(ClassLevelTypes.getAllAnnotations());
        } else {
            if (processType.equals(ProcessType.STATE)) {
                this.classLevelAnnotations.add(StateController.class);
            } else if (processType.equals(ProcessType.UPDATE)) {
                this.classLevelAnnotations.add(UpdateController.class);
            }
            this.classLevelAnnotations.add(ExceptionController.class);
        }
    }

    @Override
    public ProcessType processType() {
        return ProcessType.BOTH;
    }
}
