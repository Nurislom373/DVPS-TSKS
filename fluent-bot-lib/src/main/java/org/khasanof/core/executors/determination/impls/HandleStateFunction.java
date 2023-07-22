package org.khasanof.core.executors.determination.impls;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.executors.determination.DeterminationService;
import org.khasanof.core.executors.determination.OrderFunction;
import org.khasanof.core.state.SimpleState;
import org.khasanof.core.state.StateRepository;
import org.khasanof.core.utils.MethodUtils;
import org.khasanof.core.utils.UpdateUtils;
import org.khasanof.main.annotation.extra.HandleState;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination.impls
 * @since 16.07.2023 19:05
 */
public class HandleStateFunction implements OrderFunction {

    @Override
    public BiConsumer<Update, Map<Method, Class>> accept(List<Object> list) {
        return ((update, methods) -> {
            StateRepository stateRepository = MethodUtils.getArg(list, StateRepository.class);
            Long id = UpdateUtils.getUserId(update);
            boolean has = stateRepository.hasUserId(id);
            if (!has) {
                stateRepository.addUser(UpdateUtils.getFrom(update));
            }
            SimpleState state = stateRepository.getSimpleState(id);
            if (Objects.nonNull(state)) {
                Collector collector = MethodUtils.getArg(list, Collector.class);
                Map.Entry<Method, Class> classEntry = collector.getMethodValueAnn(state.getCore(),
                        HandleState.class);
                if (Objects.nonNull(classEntry)) {
                    methods.put(classEntry.getKey(), classEntry.getValue());
                }
            }
        });
    }

    @Override
    public DeterminationService.Order getOrder() {
        return DeterminationService.Order.MID;
    }
}
