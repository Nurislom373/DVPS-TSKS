package org.khasanof.springbootstarterfluent.core.collector.loader;

import org.khasanof.springbootstarterfluent.core.config.ApplicationProperties;
import org.khasanof.springbootstarterfluent.core.config.Config;
import org.khasanof.springbootstarterfluent.core.config.FluentConfig;
import org.khasanof.springbootstarterfluent.core.enums.ClassLevelTypes;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.main.annotation.ExceptionController;
import org.khasanof.springbootstarterfluent.main.annotation.StateController;
import org.khasanof.springbootstarterfluent.main.annotation.UpdateController;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
@Component
public class MainClassloader implements Config, ResourceLoader {

    private final ApplicationProperties.Bot bot;
    private final ApplicationContext applicationContext;
    private final Set<Class<? extends Annotation>> classLevelAnnotations = new HashSet<>();

    public MainClassloader(ApplicationContext applicationContext, ApplicationProperties properties) {
        this.applicationContext = applicationContext;
        this.bot = properties.getBot();
    }

    @Override
    public Map<String, Object> getBeans() {
        Map<String, Object> objectMap = new HashMap<>();
        for (Class<? extends Annotation> classLevelAnnotation : classLevelAnnotations) {
            Map<String, Object> validBeansTake = validBeansTake(applicationContext.getBeansWithAnnotation(classLevelAnnotation));
            if (!validBeansTake.isEmpty()) {
                objectMap.putAll(validBeansTake);
            }
        }
        return objectMap;
    }

    private Map<String, Object> validBeansTake(Map<String, Object> beanMap) {
        return beanMap.entrySet().stream()
                .peek(System.out::println)
                .filter(entry -> Arrays.stream(entry.getValue().getClass().getDeclaredMethods())
                        .anyMatch(method -> hasAnnotationMethodLevel(method, HandleClasses.getAllAnnotations())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

    @Override
    public void runnable() {
        ProcessType processType = bot.getProcessType();
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
