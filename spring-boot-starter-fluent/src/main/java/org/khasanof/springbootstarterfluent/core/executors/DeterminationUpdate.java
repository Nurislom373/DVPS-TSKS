package org.khasanof.springbootstarterfluent.core.executors;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.custom.BreakerForEach;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.enums.Proceed;
import org.khasanof.springbootstarterfluent.core.executors.determination.DeterminationService;
import org.khasanof.springbootstarterfluent.core.executors.determination.SimpleDeterminationService;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.core.state.StateRepository;
import org.khasanof.springbootstarterfluent.core.utils.UpdateUtils;
import org.khasanof.springbootstarterfluent.main.annotation.extra.HandleState;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors
 * @since 24.06.2023 20:00
 */
@Component
public class DeterminationUpdate {

    private final DeterminationService determinationService;

    public DeterminationUpdate(DeterminationService determinationService) {
        this.determinationService = determinationService;
    }

    public Set<InvokerResult> determinationV2(Update update) {
        Set<InvokerResult> invokerResults = new LinkedHashSet<>();
        List<BiConsumer<Update, Set<InvokerResult>>> list = determinationService.getDeterminations();
        BreakerForEach.forEach(list.stream(), ((updateMapBiConsumer, breaker) -> {
            if (!FluentContext.determinationServiceBoolean.get()) {
                updateMapBiConsumer.accept(update, invokerResults);
            } else {
                FluentContext.determinationServiceBoolean.set(true);
            }
        }), () -> FluentContext.determinationServiceBoolean.set(false));
        return invokerResults;
    }

}
