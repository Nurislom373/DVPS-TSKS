package org.khasanof.springbootstarterfluent.core.executors.determination;

import org.khasanof.springbootstarterfluent.core.custom.ProcessTypeResolver;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.core.utils.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination
 * @since 16.07.2023 19:20
 */
public class DeterminationAdapter {

    public void fillMap(Map<Integer, List<BiConsumer<Update, Set<InvokerResult>>>> map,
                        ApplicationContext applicationContext, ProcessType processType) {
        applicationContext.getBeansOfType(OrderFunction.class)
                .values().stream()
                .filter((orderFunction -> ProcessTypeResolver.hasAcceptProcessType(orderFunction, processType)))
                .forEach((orderFunction) -> {
                    if (map.containsKey(orderFunction.getOrder())) {
                        map.get(orderFunction.getOrder()).add(orderFunction.accept(applicationContext));
                    } else {
                        map.put(orderFunction.getOrder(), new ArrayList<>() {{
                            add(orderFunction.accept(applicationContext));
                        }});
                    }
                });
    }

}
