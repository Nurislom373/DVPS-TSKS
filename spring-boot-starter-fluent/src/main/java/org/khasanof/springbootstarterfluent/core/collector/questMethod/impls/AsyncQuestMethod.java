package org.khasanof.springbootstarterfluent.core.collector.questMethod.impls;

import lombok.SneakyThrows;
import org.khasanof.springbootstarterfluent.core.collector.MethodContext;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.executors.matcher.CompositeMatcher;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 23.06.2023 23:46
 */
public class AsyncQuestMethod implements QuestMethod {

    private final MethodContext methodContext;
    private final CompositeMatcher matcher;

    public AsyncQuestMethod(MethodContext methodContext, CompositeMatcher matcher) {
        this.methodContext = methodContext;
        this.matcher = matcher;
    }

    @Override
    public Map.Entry<Method, Object> getMethodValueAnn(Object value, HandleClasses type) {
        System.out.printf("Enter type - %s, value - %s \n", type, value);
        CompletableFuture<Map.Entry<Method, Object>> supplyAsync;
        if (type.isHasSubType()) {
            supplyAsync = CompletableFuture.supplyAsync(() -> methodContext.containsKey(type) ?
                            methodContext.getMethodsWithHandleClass(type).entrySet()
                                    .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                            value, type.getType()))
                                    .findFirst().orElse(null) : null)
                    .thenComposeAsync(s -> CompletableFuture.supplyAsync(() -> {
                        if (Objects.isNull(s)) {
                            return methodContext.containsKey(type.getSubHandleClasses()) ?
                                    methodContext.getMethodsWithHandleClass(type.getSubHandleClasses()).entrySet()
                                            .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                                    value, type.getSubHandleClasses().getType()))
                                            .findFirst().orElse(null) : null;
                        }
                        return s;
                    }));
        } else {
            supplyAsync = CompletableFuture.supplyAsync(
                    () -> methodContext.containsKey(type) ?
                            methodContext.getMethodsWithHandleClass(type).entrySet()
                                    .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                            value, type.getType()))
                                    .findFirst().orElse(null) : null);
        }
        return supplyAsync.join();
    }

    @Override
    @SneakyThrows
    public Map.Entry<Method, Object> getHandleAnyMethod(HandleType handleType) {
        return CompletableFuture.supplyAsync(() -> methodContext.containsKey(HandleClasses.HANDLE_ANY) ?
                methodContext.getMethodsWithHandleClass(HandleClasses.HANDLE_ANY).entrySet().stream().filter(
                                clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                        .findFirst().orElse(null) : null).get();
    }

    @SneakyThrows
    @Override
    public Map<Method, Object> getAllHandleAnyMethod(HandleType handleType) {
        return CompletableFuture.supplyAsync(() -> methodContext.containsKey(HandleClasses.HANDLE_ANY) ?
                methodContext.getMethodsWithHandleClass(HandleClasses.HANDLE_ANY).entrySet().stream().filter(
                                clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null)
                .get();
    }

}
