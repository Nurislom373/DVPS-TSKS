package org.khasanof.springbootstarterfluent.core.collector.questMethod.impls;

import org.khasanof.springbootstarterfluent.core.collector.SimpleMethodContext;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.executors.matcher.CompositeMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 24.06.2023 0:11
 */
public class SimpleQuestMethod implements QuestMethod {

    private final SimpleMethodContext commonMethodAdapter;
    private final CompositeMatcher matcher;

    public SimpleQuestMethod(SimpleMethodContext commonMethodAdapter, CompositeMatcher matcher) {
        this.commonMethodAdapter = commonMethodAdapter;
        this.matcher = matcher;
    }

    @Override
    public Map.Entry<Method, Object> getMethodValueAnn(Object value, HandleClasses type) {
        if (type.isHasSubType()) {
            if (commonMethodAdapter.getBeanMap().containsKey(type)) {
                Map.Entry<Method, Object> mainEntry = getMethodClassEntry(value, type, false);
                if (Objects.isNull(mainEntry)) {
                    return getSubHandleType(value, type);
                } else {
                    return mainEntry;
                }
            } else {
                return getSubHandleType(value, type);
            }
        }
        if (commonMethodAdapter.getBeanMap().containsKey(type)) {
            return getMethodClassEntry(value, type, false);
        }
        return null;
    }

    @Override
    public Map.Entry<Method, Object> getHandleAnyMethod(HandleType handleType) {
        return commonMethodAdapter.getBeanMap().get(HandleClasses.HANDLE_ANY).entrySet()
                .stream().filter(clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                .findFirst().orElse(null);
    }

    @Override
    public Map<Method, Object> getAllHandleAnyMethod(HandleType handleType) {
        return commonMethodAdapter.getBeanMap().get(HandleClasses.HANDLE_ANY).entrySet()
                .stream().filter(clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Method, Object> getSubHandleType(Object value, HandleClasses type) {
        if (commonMethodAdapter.getBeanMap().containsKey(type.getSubHandleClasses())) {
            return getMethodClassEntry(value, type.getSubHandleClasses(), true);
        } else {
            return null;
        }
    }

    private Map.Entry<Method, Object> getMethodClassEntry(Object value, HandleClasses type, boolean isSubType) {
        return commonMethodAdapter.getMethodsWithAnnotation(type.getType()).entrySet()
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
