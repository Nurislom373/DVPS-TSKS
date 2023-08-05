package org.khasanof.springbootstarterfluent.core.collector;

import lombok.Getter;
import org.khasanof.springbootstarterfluent.core.collector.loader.HandleScannerLoader;
import org.khasanof.springbootstarterfluent.core.collector.loader.MainClassloader;
import org.khasanof.springbootstarterfluent.core.collector.loader.ResourceLoader;
import org.khasanof.springbootstarterfluent.core.collector.methodChecker.MethodCheckerAdapter;
import org.khasanof.springbootstarterfluent.core.config.ApplicationConfigContext;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.springframework.stereotype.Component;

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
@Component
public class CommonMethodAdapter {

    private final HandleScannerLoader handleScannerLoader = new HandleScannerLoader();
    private final Map<HandleClasses, Map<Method, Class>> collectMap = new HashMap<>();
    private final Map<HandleClasses, Map<Method, Object>> beanMap = new HashMap<>();
    private final ApplicationConfigContext context = ApplicationConfigContext.getConfigInstance();
    private final CommonInterfaceAdapter interfaceAdapter = new CommonInterfaceAdapter();
    private final ResourceLoader resourceLoader;

    public CommonMethodAdapter(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        setMethodClassMap();
    }

    public Map<Method, Object> methodsWithAnnotation(Class<? extends Annotation> annotation) {
        return beanMap.get(HandleClasses.getHandleWithType(annotation));
    }

    public boolean hasHandle(Class<? extends Annotation> annotation) {
        return beanMap.containsKey(HandleClasses.getHandleWithType(annotation));
    }

    void setMethodClassMap() {
        MethodCheckerAdapter methodCheckerAdapter = context.getInstance(MethodCheckerAdapter.class);
        for (Iterator<Object> iterator = resourceLoader.getBeans().values().iterator(); iterator.hasNext(); ) {
            final Class<?> clazz = iterator.next().getClass();
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                if (methodCheckerAdapter.valid(method)) {
                    HandleClasses key = getMethodAnnotation(method);
                    if (beanMap.containsKey(key)) {
                        beanMap.get(key).put(method, iterator.next());
                    } else {
                        beanMap.put(key, new HashMap<>() {{
                            put(method, iterator.next());
                        }});
                    }
                }
            });
        }
        beanMap.forEach((key, value) -> System.out.println(key + " : " + value.size()));
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
