package org.khasanof.springbootstarterfluent.core.executors.determination;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination
 * @since 16.07.2023 17:00
 */
public class SimpleDeterminationService implements DeterminationService {

    private final List<Object> objects = new ArrayList<>();
    private final Map<Integer, List<BiConsumer<Update, Map<Method, Object>>>> orderListMap = new TreeMap<>();

    public SimpleDeterminationService(List<Object> list) {
        this.objects.addAll(list);
        DeterminationAdapter determinationAdapter = new DeterminationAdapter();
        determinationAdapter.fillMap(orderListMap, objects);
    }

    @Override
    public List<BiConsumer<Update, Map<Method, Object>>> getDeterminations() {
        return orderListMap.values().stream().filter(Objects::nonNull)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void addObject(Object o) {
        if (Objects.nonNull(o)) {
            this.objects.add(o);
        }
    }

}
