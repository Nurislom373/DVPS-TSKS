package org.khasanof.core.collector.questMethod.impls;

import org.khasanof.core.collector.impls.CommonMethodAdapter;
import org.khasanof.core.collector.questMethod.QuestMethod;
import org.khasanof.core.enums.HandleClasses;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.executors.matcher.CompositeMatcher;

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

    final CommonMethodAdapter annotationCollector = new CommonMethodAdapter();
    final CompositeMatcher matcher = new CompositeMatcher();

    @Override
    public Map.Entry<Method, Class> getMethodValueAnn(Object value, HandleClasses type) {
        System.out.printf("Enter value - %s, type - %s \n", value, type);
        CompletableFuture<Map.Entry<Method, Class>> supplyAsync;
        if (type.isHasSubType()) {
            supplyAsync = CompletableFuture.supplyAsync(
                    () -> annotationCollector.getCollectMap()
                            .get(type).entrySet()
                            .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                    value, type.getType()))
                            .findFirst().orElse(null)).thenComposeAsync(s -> CompletableFuture.supplyAsync(() -> {
                if (Objects.isNull(s)) {
                    return annotationCollector.getCollectMap().get(type.getSubHandleClasses()).entrySet()
                            .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                    value, type.getSubHandleClasses().getType()))
                            .findFirst().orElse(null);
                }
                return s;
            }));
        } else {
            supplyAsync = CompletableFuture.supplyAsync(
                    () -> annotationCollector.getCollectMap()
                            .get(type).entrySet()
                            .stream().filter(aClass -> matcher.chooseMatcher(aClass.getKey(),
                                    value, type.getType()))
                            .findFirst().orElse(null));
        }
        return supplyAsync.join();
    }

    @Override
    public Map.Entry<Method, Class> getHandleAnyMethod(HandleType handleType) {
        return annotationCollector.getCollectMap().get(HandleClasses.HANDLE_ANY).entrySet()
                .stream().filter(clazz -> matcher.chooseMatcher(clazz.getKey(), handleType))
                .findFirst().orElse(null);
    }


}
