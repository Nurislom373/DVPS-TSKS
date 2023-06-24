package org.khasanof.core.executors.matcher;

import org.khasanof.core.executors.matcher.impls.SimpleCallbackMatcher;
import org.khasanof.core.executors.matcher.impls.SimpleMessageMatcher;
import org.khasanof.main.annotation.HandleCallback;
import org.khasanof.main.annotation.HandleCallbacks;
import org.khasanof.main.annotation.HandleMessage;
import org.khasanof.main.annotation.HandleMessages;

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

    public boolean chooseMatcher(Method method, String value, Class<? extends Annotation> annotation) {
        return supplierMap.get(annotation).get()
                .matcher(method.getAnnotation(annotation), value);
    }

    void setSupplierMap() {
        supplierMap.put(HandleCallback.class, SimpleCallbackMatcher::new);
        supplierMap.put(HandleMessage.class, SimpleMessageMatcher::new);
        supplierMap.put(HandleCallbacks.class, () -> adapterMatcher.findMatcher(HandleCallbacks.class));
        supplierMap.put(HandleMessages.class, () -> adapterMatcher.findMatcher(HandleMessages.class));
    }

}
