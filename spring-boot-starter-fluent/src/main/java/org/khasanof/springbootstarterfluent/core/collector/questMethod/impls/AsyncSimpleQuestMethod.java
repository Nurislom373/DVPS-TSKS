package org.khasanof.springbootstarterfluent.core.collector.questMethod.impls;

import lombok.SneakyThrows;
import org.khasanof.springbootstarterfluent.core.collector.GenericMethodContext;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.executors.matcher.CompositeMatcher;
import org.khasanof.springbootstarterfluent.core.model.InvokerMethod;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 23.06.2023 23:46
 */
public class AsyncSimpleQuestMethod implements QuestMethod<HandleClasses> {

    private final GenericMethodContext<HandleClasses, Map<Method, Object>> methodContext;
    private final CompositeMatcher matcher;

    public AsyncSimpleQuestMethod(GenericMethodContext<HandleClasses, Map<Method, Object>> methodContext, CompositeMatcher matcher) {
        this.methodContext = methodContext;
        this.matcher = matcher;
    }

    @Override
    public InvokerResult getMethodValueAnn(Object value, HandleClasses type) {
        System.out.printf("Enter type - %s, value - %s \n", type, value);
        CompletableFuture<Map.Entry<Method, Object>> supplyAsync;
        if (type.isHasSubType()) {
            supplyAsync = CompletableFuture.supplyAsync(() -> methodContext.containsKey(type) ?
                            methodContext.getMethodsByGenericKey(type).entrySet()
                                    .parallelStream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                            value, type.getType()))
                                    .findFirst().orElse(null) : null)
                    .thenComposeAsync(s -> CompletableFuture.supplyAsync(() -> {
                        if (Objects.isNull(s)) {
                            return methodContext.containsKey(type.getSubHandleClasses()) ?
                                    methodContext.getMethodsByGenericKey(type.getSubHandleClasses()).entrySet()
                                            .parallelStream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                                    value, type.getSubHandleClasses().getType()))
                                            .findFirst().orElse(null) : null;
                        }
                        return s;
                    }));
        } else {
            supplyAsync = CompletableFuture.supplyAsync(
                    () -> methodContext.containsKey(type) ?
                            methodContext.getMethodsByGenericKey(type).entrySet()
                                    .parallelStream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                            value, type.getType()))
                                    .findFirst().orElse(null) : null);
        }
        return supplyAsync.thenApply(this::resultCreator).join();
    }

    @Override
    @SneakyThrows
    public InvokerResult getHandleAnyMethod(HandleType handleType) {
        return CompletableFuture.supplyAsync(() -> methodContext.containsKey(HandleClasses.HANDLE_ANY) ?
                methodContext.getMethodsByGenericKey(HandleClasses.HANDLE_ANY).entrySet().parallelStream().filter(
                                clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                        .findFirst().orElse(null) : null).thenApply(this::resultCreator).get();
    }

    @SneakyThrows
    @Override
    public Set<InvokerResult> getAllHandleAnyMethod(HandleType handleType) {
        return CompletableFuture.supplyAsync(() -> methodContext.containsKey(HandleClasses.HANDLE_ANY) ?
                methodContext.getMethodsByGenericKey(HandleClasses.HANDLE_ANY).entrySet().parallelStream().filter(
                                clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                        .map(this::resultCreator)
                        .collect(Collectors.toSet()) : null)
                .get();
    }

    private InvokerResult resultCreator(Map.Entry<Method, Object> entry) {
        if (Objects.isNull(entry)) {
            return null;
        }
        return new InvokerMethod(entry.getKey(), entry.getValue());
    }

}
