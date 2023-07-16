package org.khasanof.core.collector.questMethod.impls;

import org.khasanof.core.collector.impls.CommonMethodAdapter;
import org.khasanof.core.collector.questMethod.QuestMethod;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.executors.matcher.CompositeMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 24.06.2023 0:11
 */
public class SimpleQuestMethod implements QuestMethod {

    private final CommonMethodAdapter commonMethodAdapter = new CommonMethodAdapter();
    private final CompositeMatcher matcher = new CompositeMatcher();

    @Override
    public Map.Entry<Method, Class> getMethodValueAnn(Object value, HandleClasses type) {
        if (type.isHasSubType()) {
            if (commonMethodAdapter.getCollectMap().containsKey(type)) {
                Map.Entry<Method, Class> mainEntry = getMethodClassEntry(value, type, false);
                if (Objects.isNull(mainEntry)) {
                    return getSubHandleType(value, type);
                } else {
                    return mainEntry;
                }
            } else {
                return getSubHandleType(value, type);
            }
        }
        if (commonMethodAdapter.getCollectMap().containsKey(type)) {
            return getMethodClassEntry(value, type, false);
        }
        return null;
    }

    @Override
    public Map.Entry<Method, Class> getHandleAnyMethod(HandleType handleType) {
        return commonMethodAdapter.getCollectMap().get(HandleClasses.HANDLE_ANY).entrySet()
                .stream().filter(clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                .findFirst().orElse(null);
    }

    private Map.Entry<Method, Class> getSubHandleType(Object value, HandleClasses type) {
        if (commonMethodAdapter.getCollectMap().containsKey(type.getSubHandleClasses())) {
            return getMethodClassEntry(value, type.getSubHandleClasses(), true);
        } else {
            return null;
        }
    }

    private Map.Entry<Method, Class> getMethodClassEntry(Object value, HandleClasses type, boolean isSubType) {
        return commonMethodAdapter.methodsWithAnnotation(type.getType()).entrySet()
                .stream().filter(aClass -> methodHasVal(aClass.getKey(), value, getAnnotation(type, isSubType)))
                .findFirst().orElse(null);
    }

    private Class<? extends Annotation> getAnnotation(HandleClasses type, boolean isSubType) {
        if (isSubType) {
            return type.getSubHandleClasses().getType();
        }
        return type.getType();
    }

    private boolean methodHasVal(Method method, Object value, Class<? extends Annotation> annotation) {
        return matcher.chooseMatcher(method, value, annotation);
    }

}
