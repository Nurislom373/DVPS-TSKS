package org.khasanof.springbootstarterfluent.core.executors;

import org.khasanof.springbootstarterfluent.core.custom.BreakerForEach;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.executors.determination.DeterminationService;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
