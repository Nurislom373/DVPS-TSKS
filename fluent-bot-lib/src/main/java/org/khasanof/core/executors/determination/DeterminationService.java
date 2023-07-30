package org.khasanof.core.executors.determination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class DeterminationService {

    private final List<Object> objects = new ArrayList<>();
    private final Map<Integer, List<BiConsumer<Update, Map<Method, Class>>>> orderListMap = new TreeMap<>();

    public DeterminationService(List<Object> list) {
        this.objects.addAll(list);
        DeterminationAdapter determinationAdapter = new DeterminationAdapter();
        determinationAdapter.fillMap(orderListMap, objects);
    }

    public List<BiConsumer<Update, Map<Method, Class>>> getDeterminationsByOrder() {
        return orderListMap.values().stream().filter(Objects::nonNull)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void addObject(Object o) {
        if (Objects.nonNull(o)) {
            this.objects.add(o);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum Order {

        LOW(1),
        MID(5),
        HIGH(10);

        private final int level;

        public static List<Order> getOrders() {
            List<Order> values = new ArrayList<>(List.of(values()));
            Collections.reverse(values);
            return values;
        }
    }

}
