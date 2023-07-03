package org.khasanof.core.executors.matcher;

import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.HandleType;
import org.khasanof.main.annotation.methods.HandleAny;

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
public class CompositeMatcher {

    public final Map<Class<? extends Annotation>, Supplier<GenericMatcher>> supplierMap = new HashMap<>();
    private final AdapterMatcher adapterMatcher = new AdapterMatcher();

    public CompositeMatcher() {
        setSupplierMap();
    }

    public boolean chooseMatcher(Method method, Object value, Class<? extends Annotation> annotation) {
        return supplierMap.get(annotation).get()
                .matcher(method.getAnnotation(annotation), value);
    }

    public boolean chooseMatcher(Method method, HandleType handleType) {
        return supplierMap.get(HandleAny.class)
                .get().matcher(method.getAnnotation(HandleAny.class), handleType);
    }

    void setSupplierMap() {
        adapterMatcher.setUp(HandleClasses.getAllAnnotations(), supplierMap);
    }

}
