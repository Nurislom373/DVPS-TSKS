package org.khasanof.springbootstarterfluent.core.collector;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.collector.loader.ResourceLoader;
import org.khasanof.springbootstarterfluent.core.collector.methodChecker.MethodCheckerAdapter;
import org.khasanof.springbootstarterfluent.core.config.CommonFluentConfigRunner;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

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
@Slf4j
@Getter
@Component
@DependsOn(value = {CommonFluentConfigRunner.NAME})
public class CommonMethodAdapter implements InitializingBean {

    private final Map<HandleClasses, Map<Method, Object>> beanMap = new HashMap<>();
    private final ResourceLoader resourceLoader;
    private final MethodCheckerAdapter checkerAdapter;

    public CommonMethodAdapter(ResourceLoader resourceLoader, MethodCheckerAdapter checkerAdapter) {
        this.resourceLoader = resourceLoader;
        this.checkerAdapter = checkerAdapter;
    }

    public Map<Method, Object> methodsWithAnnotation(Class<? extends Annotation> annotation) {
        return beanMap.get(HandleClasses.getHandleWithType(annotation));
    }

    public boolean hasHandle(Class<? extends Annotation> annotation) {
        return beanMap.containsKey(HandleClasses.getHandleWithType(annotation));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setMethodClassMap();
    }

    void setMethodClassMap() {
        resourceLoader.getBeans().values().forEach(bean -> {
            final Class<?> clazz = bean.getClass();
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
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
        beanMap.forEach((key, value) -> log.info("{} : {}", key, value.size()));
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
