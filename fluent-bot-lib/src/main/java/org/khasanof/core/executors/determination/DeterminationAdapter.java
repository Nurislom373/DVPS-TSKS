package org.khasanof.core.executors.determination;

import org.khasanof.core.utils.MethodUtils;
import org.khasanof.core.utils.ReflectionUtils;
import org.reflections.Reflections;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination
 * @since 16.07.2023 19:20
 */
public class DeterminationAdapter {

    private final Reflections reflections = ReflectionUtils.getReflections(true);

    public void fillMap(Map<Integer, List<BiConsumer<Update, Map<Method, Class>>>> map,
                        List<Object> list) {
        Set<Class<? extends OrderFunction>> types = reflections.getSubTypesOf(OrderFunction.class);
        types.stream().forEach(type -> {
            OrderFunction orderFunction = MethodUtils.createInstanceDefaultConstructor(type);
            if (map.containsKey(orderFunction.getOrder())) {
                map.get(orderFunction.getOrder()).add(orderFunction.accept(list));
            } else {
                map.put(orderFunction.getOrder(), new ArrayList<>(){{
                    add(orderFunction.accept(list));
                }});
            }
        });
    }

}
