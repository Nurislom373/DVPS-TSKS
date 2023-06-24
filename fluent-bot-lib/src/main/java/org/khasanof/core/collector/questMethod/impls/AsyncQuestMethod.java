package org.khasanof.core.collector.questMethod.impls;

import lombok.SneakyThrows;
import org.khasanof.core.collector.impls.AnnotationCollector;
import org.khasanof.core.collector.questMethod.QuestMethod;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.executors.matcher.CompositeMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.collector.questMethod
 * @since 23.06.2023 23:46
 */
public class AsyncQuestMethod implements QuestMethod {

    final AnnotationCollector annotationCollector = new AnnotationCollector();
    final CompositeMatcher matcher = new CompositeMatcher();

    @Override
    public Map.Entry<Method, Class> getMethodValueAnn(String value, HandleClasses type) {
        System.out.printf("Enter value - %s, type - %s \n", value, type);
        CompletableFuture<Map.Entry<Method, Class>> supplyAsync;
        if (type.equals(HandleClasses.HANDLE_CALLBACK)) {
            supplyAsync = CompletableFuture.supplyAsync(() -> annotationCollector.getCollectMap()
                    .get(HandleClasses.HANDLE_CALLBACK).entrySet()
                    .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                            value, HandleClasses.HANDLE_CALLBACK.getType()))
                    .findFirst().orElse(null)).thenCompose(s -> CompletableFuture.supplyAsync(() -> {
                if (Objects.isNull(s)) {
                    return annotationCollector.getCollectMap().get(HandleClasses.HANDLE_CALLBACKS).entrySet()
                            .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                    value, HandleClasses.HANDLE_CALLBACKS.getType()))
                            .findFirst().orElse(null);
                }
                return s;
            }));
        } else {
            supplyAsync = CompletableFuture.supplyAsync(() -> annotationCollector.getCollectMap()
                    .get(HandleClasses.HANDLE_MESSAGE).entrySet()
                    .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                            value, HandleClasses.HANDLE_MESSAGE.getType()))
                    .findFirst().orElse(null)).thenCompose(s -> CompletableFuture.supplyAsync(() -> {
                if (Objects.isNull(s)) {
                    return annotationCollector.getCollectMap().get(HandleClasses.HANDLE_MESSAGES).entrySet()
                            .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                    value, HandleClasses.HANDLE_MESSAGES.getType()))
                            .findFirst().orElse(null);
                }
                return s;
            }));
        }
        return supplyAsync.join();
    }


}
