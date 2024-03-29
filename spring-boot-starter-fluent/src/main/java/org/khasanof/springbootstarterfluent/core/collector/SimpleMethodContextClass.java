package org.khasanof.springbootstarterfluent.core.collector;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.collector.loader.ResourceLoader;
import org.khasanof.springbootstarterfluent.core.collector.methodChecker.MethodCheckerAdapter;
import org.khasanof.springbootstarterfluent.core.config.FluentConfigRunner;
import org.khasanof.springbootstarterfluent.core.enums.ClassLevelTypes;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.event.methodContext.MethodCollectedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@link SimpleMethodContextClass} class collects the methods from the classes corresponding
 * to the {@link ClassLevelTypes} enum.
 * <p>
 * <br/>
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 11:28
 * <br/>
 * Package: org.khasanof.core.collector
 */
@Slf4j
@Getter
@Component(SimpleMethodContextClass.NAME)
public class SimpleMethodContextClass implements SimpleMethodContext {

    public static final String NAME = "simpleMethodContextClass";
    private final ResourceLoader resourceLoader;
    private final MethodCheckerAdapter checkerAdapter;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<HandleClasses, Map<Method, Object>> beanMap = new HashMap<>();

    public SimpleMethodContextClass(ResourceLoader resourceLoader, MethodCheckerAdapter checkerAdapter, ApplicationEventPublisher eventPublisher) {
        this.resourceLoader = resourceLoader;
        this.checkerAdapter = checkerAdapter;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Map<Method, Object> getMethodsWithAnnotation(Class<? extends Annotation> annotation) {
        return beanMap.get(HandleClasses.getHandleWithType(annotation));
    }

    @Override
    public Map<Method, Object> getMethodsByGenericKey(HandleClasses classes) {
        return beanMap.get(classes);
    }

    @Override
    public boolean containsKey(HandleClasses key) {
        return beanMap.containsKey(key);
    }

    @Override
    public boolean containsKey(Class<? extends Annotation> annotation) {
        return beanMap.containsKey(HandleClasses.getHandleWithType(annotation));
    }

    @Override
    public void assembleMethods() {
        log.info("simple method assemble start!");
        setMethodClassMap();
    }

    void setMethodClassMap() {
        resourceLoader.getBeans().values().forEach(bean -> {
            final Class<?> clazz = bean.getClass();
            List<Method> methods = new ArrayList<>();
            if (hasInterface(clazz)) {
                methods.addAll(getInterfaceMethods(clazz));
            }
            if (clazz.getDeclaredMethods().length >= 1) {
                methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            }
            methods.forEach(method -> {
                if (checkerAdapter.valid(method)) {
                    HandleClasses key = getMethodAnnotation(method);
                    if (beanMap.containsKey(key)) {
                        beanMap.get(key).put(method, bean);
                    } else {
                        beanMap.put(key, new HashMap<>() {{
                            put(method, bean);
                        }});
                    }
                }
            });
        });
        pushEvent();
    }

    private boolean hasInterface(Class<?> clazz) {
        return clazz.getInterfaces().length >= 1;
    }

    private List<Method> getInterfaceMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        if (clazz.getInterfaces().length >= 1) {
            Arrays.stream(clazz.getInterfaces())
                    .forEach(in -> methods.addAll(Arrays.asList(in.getDeclaredMethods())));
        }
        return methods;
    }

    private void pushEvent() {
        Map<HandleClasses, Integer> map = beanMap.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(),
                        entry.getValue().size())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        eventPublisher.publishEvent(new MethodCollectedEvent(this, map));
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
