package org.khasanof.springbootstarterfluent.core.executors.matcher;

import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.06.2023
 * <br/>
 * Time: 22:02
 * <br/>
 * Package: org.khasanof.core.executors.matcher
 */
@Component
public class CompositeMatcher {

    public final Map<Class<? extends Annotation>, Supplier<GenericMatcher>> supplierMap = new HashMap<>();
    private final AdapterMatcher adapterMatcher;

    public CompositeMatcher(AdapterMatcher adapterMatcher) {
        this.adapterMatcher = adapterMatcher;
        setSupplierMap();
    }

    @SuppressWarnings("unchecked")
    public boolean chooseMatcher(Method method, Object value, Class<? extends Annotation> annotation) {
        return supplierMap.get(annotation).get()
                .matcher(method.getAnnotation(annotation), value);
    }

    @SuppressWarnings("unchecked")
    public boolean chooseMatcher(Method method, HandleType handleType) {
        return supplierMap.get(HandleAny.class)
                .get().matcher(method.getAnnotation(HandleAny.class), handleType);
    }

    void setSupplierMap() {
        adapterMatcher.setUp(HandleClasses.getAllAnnotations(), supplierMap);
    }

}
